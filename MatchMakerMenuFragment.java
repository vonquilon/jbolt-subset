package com.syas.jbolt;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.Session;
import com.syas.jbolt.login.LoginActivity;
import com.syas.jbolt.model.user.FaqActivity;
import com.syas.jbolt.model.user.UserService;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MatchMakerMenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saved){
        View view = inflater.inflate(R.layout.matchmaker_menu_layout, parent, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.matchmaker_menu_verify)
    public void verifyClick(Button b) {
        closeDrawer();
    }

    @OnClick(R.id.matchmaker_menu_share)
    public void shareClick(Button b) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out the JBolt app, a love app.");
        shareIntent.setType("text/plain");
        if (shareIntent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    @OnClick(R.id.matchmaker_menu_faq)
    public void faqClick(Button b) {
        startActivity(new Intent(b.getContext(), FaqActivity.class));
    }

    @OnClick(R.id.matchMakerlogOut)
    public void logOut(Button b) {
        UserService.logout(getActivity());
        clearSession();
        Intent logout = new Intent(getActivity(), LoginActivity.class);
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(logout);
    }

    @OnClick(R.id.matchmaker_menu_matches)
    public void matchesClick() {
        MatchMakerMain act = (MatchMakerMain) getActivity();
        if(act != null){
            act.mDrawer.closeDrawers();
            act.mDrawer.openDrawer(Gravity.RIGHT);
            act.matchMakerPager.setCurrentItem(3);
        }
    }

    private void clearSession() {
        Session session = Session.getActiveSession();
        if (session != null) {
            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
            }
        } else {
            session = new Session(getActivity());
            Session.setActiveSession(session);
            session.closeAndClearTokenInformation();
        }
    }

    private void closeDrawer(){
        MatchMakerMain act = (MatchMakerMain) getActivity();
        if(act != null){
            act.mDrawer.closeDrawers();
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}