package com.syas.jbolt.model.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.syas.jbolt.ErrorDialog;
import com.syas.jbolt.R;
import com.syas.jbolt.database.JboltDBHelper;
import com.syas.jbolt.server.JboltCallback;
import com.syas.jbolt.server.PostCallback;
import com.syas.jbolt.server.Server;
import com.syas.jbolt.server.VerifyUpdate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class VerificationFragment extends Fragment implements JboltDBHelper.DBCallback<List<FacebookFriend>>, SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    @InjectView(R.id.verifyProgress)
    ProgressBar verifyProgress;

    @InjectView(R.id.noPeopleTextView)
    TextView noPeopleTextView;

    @InjectView(R.id.titleContainer)
    LinearLayout titleContainer;
    @InjectView(R.id.listview)
    ListView listview;

    List<FacebookFriend> friendList = new ArrayList<>();
    List<FacebookFriend> friendListCopy = new ArrayList<>();

    User u = UserService.getsUser(getActivity());

    private boolean contactingServer = false;

    public VerificationAdapter adapter;
    public boolean needsRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_verification, container, false);
        ButterKnife.inject(this, layout);

        if (savedInstanceState != null) {
            contactingServer = savedInstanceState.getBoolean("contactingServer");
        }

        return layout;
    }

    private void startLoading() {
        noPeopleTextView.setVisibility(View.GONE);
        titleContainer.setVisibility(View.GONE);
        listview.setVisibility(View.GONE);
        verifyProgress.setVisibility(View.VISIBLE);
    }

    private void stopLoadingSucess() {
        noPeopleTextView.setVisibility(View.GONE);
        verifyProgress.setVisibility(View.GONE);
        titleContainer.setVisibility(View.VISIBLE);
        listview.setVisibility(View.VISIBLE);
    }

    private void stopLoadingFail() {
        noPeopleTextView.setVisibility(View.VISIBLE);
        verifyProgress.setVisibility(View.GONE);
        titleContainer.setVisibility(View.GONE);
        listview.setVisibility(View.GONE);
    }

    private void setNeedsRefresh(boolean needsRefresh) {
        this.needsRefresh = needsRefresh;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onPause() {
        Log.e(null, "Paused");
        super.onPause();
        final Context context = getActivity();
        final List<FacebookFriend> diff = getDifference();
        contactingServer = true;
        Server
                .getJboltService()
                .verifyFacebookFriends(new VerifyUpdate(u.getAccessToken(), u.getId(), diff), new PostCallback(getActivity()) {
                    @Override
                    protected void handleResult(Void result) {
                        Log.e(null, "handleResult");
                        contactingServer = false;
                        new JboltDBHelper(context).addFacebookFriends(diff, null);
                    }
                });

    }

    @Override
    public void onResume() {
        Log.e(null, "Resuming");
        super.onResume();

        if (((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo() == null) {
            networkAlert(getActivity());
        } else {
            ErrorDialog.clear();
        }

        if (!contactingServer) {
            Context con = getActivity();
            if (con != null) {
                new JboltDBHelper(con).getFacebookFriends(u.getId(), this);
            }
        }
    }

    @Override
    public void onSuccess(List<FacebookFriend> result) {
        if (result == null || result.size() == 0) {
            updateVerificationPage();
        } else if(listview != null){
            friendList = result;
            friendListCopy = copyFriendlist(result);
            setAdapter();
            stopLoadingSucess();
        }
    }

    private void setAdapter() {
        if (adapter == null) {
            adapter = new VerificationAdapter(friendListCopy, u);
            listview.setAdapter(adapter);
        } else {
            notifyAdapter();
        }
    }

    private void notifyAdapter() {
        adapter.notifyDataSetChanged();
        adapter.sortFriendlist();
    }

    public void updateVerificationPage() {
        startLoading();
        Server.getJboltService()
                .getFacebookFriends(u.getAccessToken()
                        , u.getId(), new GetFacebookFriendsFromServer(getActivity()));
    }

    @Override
    public void onFail() {
        ErrorDialog.show(getActivity(), 599);
        setNeedsRefresh(true);
        stopLoadingFail();
    }

    private class GetFacebookFriendsFromServer extends JboltCallback<List<FacebookFriend>> {

        public GetFacebookFriendsFromServer(Activity context) {
            super(context);
        }

        @Override
        protected void handleError(int code) {
            ErrorDialog.show(getActivity(), 612);
            setNeedsRefresh(true);
            stopLoadingFail();
        }

        @Override
        protected void handleResult(List<FacebookFriend> result) {
            if (result != null && result.size() > 0) {
                setNeedsRefresh(false);
                new JboltDBHelper(mContext).addFacebookFriends(result, null);
                friendList = result;
                friendListCopy = copyFriendlist(result);
                setAdapter();
                stopLoadingSucess();
            } else {
                setNeedsRefresh(true);
                stopLoadingFail();
            }
        }
    }

    private List<FacebookFriend> copyFriendlist(List<FacebookFriend> in) {
        List<FacebookFriend> out = new ArrayList<>();
        for (FacebookFriend f : in) {
            out.add(copy(f));
        }
        return out;
    }

    private FacebookFriend copy(FacebookFriend in) {
        return new FacebookFriend(in.facebookId, in.friendId, in.firstName, in.lastName, in.isJewish, in.isSingle, in.religiousityLevel);
    }

    private List<FacebookFriend> getDifference() {
        List<FacebookFriend> copy = new ArrayList<>(friendListCopy);
        List<FacebookFriend> copy2 = new ArrayList<>(friendList);
        Iterator<FacebookFriend> itr = copy.iterator();
        while(itr.hasNext()) {
            FacebookFriend friend = itr.next();
            for (FacebookFriend f : copy2) {
                if (f.facebookId == friend.facebookId && !isDifferent(f, friend)) {
                    itr.remove();
                }
            }
        }
        return copy;
    }

    private boolean isDifferent(FacebookFriend a, FacebookFriend b) {
        return a.isJewish != b.isJewish || a.isSingle != b.isSingle || a.religiousityLevel != b.religiousityLevel;
    }

    private void networkAlert(final Context context) {
        Dialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(R.string.internet_is_off)
                .setPositiveButton(R.string.enable_internet, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        context.startActivity(intent);
                    }

                })
                .create();

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("contactingServer", contactingServer);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    /*
    *
    *           Search Functionality
    *
     */

    @Override
    public boolean onClose() {
        if(adapter != null){
            adapter.filter("");
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(adapter != null){
            adapter.filter(newText);
        }
        return false;
    }
}
