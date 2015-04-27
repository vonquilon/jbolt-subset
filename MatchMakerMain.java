package com.syas.jbolt;

import android.app.Activity;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.syas.jbolt.adapters.MatchMakerViewPagerAdapter;
import com.syas.jbolt.chat.ChatActivity;
import com.syas.jbolt.model.Match;
import com.syas.jbolt.model.user.AppUser;
import com.syas.jbolt.model.user.MatchMaker;
import com.syas.jbolt.model.user.User;
import com.syas.jbolt.model.user.UserService;
import com.syas.jbolt.model.user.VerificationFragment;
import com.syas.jbolt.server.JboltCallback;
import com.syas.jbolt.server.MatchmakerMatchResponse;
import com.syas.jbolt.server.Server;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MatchMakerMain extends ActionBarActivity implements TextWatcher, IServiceBinder {

    @InjectView(R.id.matchmakerDrawerLayout)
    DrawerLayout mDrawer;
    @InjectView(R.id.matchmaker_toolbar)
    Toolbar mToolbar;
    ActionBarDrawerToggle mDrawerToggle;

    @InjectView(R.id.matchMakerFlipper)
    ViewFlipper searchFlipper;

    @InjectView(R.id.matchMakerSearchBar)
    EditText matchMakerSearchBar;

    @InjectView(R.id.matchMakerPager)
    ViewPager matchMakerPager;

    @InjectView(R.id.tabs)
    PagerTabStrip tabs;

    private MatchMakerViewPagerAdapter adapter;

    private User user;

    private VerificationFragment verificationFragment;
    private LinearLayout menuButtonsContainer;

    private MainService.MainServiceBinder mBinder = null;
    private final ServiceConnection SERVICE_CONNECTION = new BaseMainActivity.MainServiceConnection(this, mBinder);

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.matchmaker_main);
        ButterKnife.inject(this);
        tabs.setPadding(5, 0, 5, 0);

        if (saved == null) {
            verificationFragment = new VerificationFragment();
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.matchmaker_container, verificationFragment)
                    .commit();
        }

        menuButtonsContainer = (LinearLayout) mToolbar
                .findViewById(R.id.action_bar_custom)
                .findViewById(R.id.buttonsContainer);

        setUpDrawerLayout();
        searchFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        searchFlipper.setOutAnimation(this,android.R.anim.fade_out);
        matchMakerSearchBar.addTextChangedListener(this);
    }

    private void setUpDrawerLayout(){
        setSupportActionBar(mToolbar);
        mDrawerToggle= new ActionBarDrawerToggle(this, mDrawer,mToolbar, R.string.app_name, R.string.app_name);
        mDrawer.setDrawerListener(mDrawerToggle);
        mToolbar.setNavigationIcon(R.drawable.menu_icon);
        mToolbar.setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu, menu);
        ImageView menuRefresh = (ImageView) menuButtonsContainer
                .findViewById(R.id.menuRefresh);
        menuRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificationFragment.updateVerificationPage();
            }
        });
        if (verificationFragment.needsRefresh) {
            menuRefresh.setVisibility(View.VISIBLE);
        } else {
            menuRefresh.setVisibility(View.GONE);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.chat_drawer){
            if(mDrawer.isDrawerOpen(Gravity.RIGHT))
                mDrawer.closeDrawer(Gravity.RIGHT);
            else
                mDrawer.openDrawer(Gravity.RIGHT);

            return true;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();

    }

    @Override
    public void onResume() {
        super.onResume();
        user = UserService.getsUser(this);
        Intent bind = new Intent(this,MainService.class);
        bindService(bind,SERVICE_CONNECTION,BIND_AUTO_CREATE);
        Server
                .getJboltService()
                .getMatchmakerMatches(user.getAccessToken(), user.getId(), new GetMatchesCallback(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mBinder != null)
            mBinder.setActivity(null);
        unbindService(SERVICE_CONNECTION);
    }

    @OnClick(R.id.matchMakerSearchButton)
    public void search() {
        matchMakerSearchBar.setText("");
        if(adapter != null)
            adapter.filter("");

        if(matchMakerSearchBar.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(INPUT_METHOD_SERVICE);
            if(imm != null && imm.isActive(matchMakerSearchBar)) {
                matchMakerSearchBar.requestFocus();
                imm.hideSoftInputFromWindow(matchMakerSearchBar.getWindowToken(), 0);
            }

        }
        searchFlipper.showNext();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (adapter != null) {
            adapter.filter(s);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private class GetMatchesCallback extends JboltCallback<MatchmakerMatchResponse> {

        public GetMatchesCallback(Activity context) {
            super(context);
        }

        @Override
        protected void handleError(int code) {
            ErrorDialog.show(mContext,code);
        }

        @Override
        protected void handleResult(MatchmakerMatchResponse result) {
            if (adapter == null) {
                adapter = new MatchMakerViewPagerAdapter(
                        getSupportFragmentManager()
                        , result
                        , (MatchMaker) user
                );
                matchMakerPager.setAdapter(adapter);
            } else {
                adapter.setResponse(result);
                adapter.filter(matchMakerSearchBar.getText());
            }
        }

    }


    @Override
    public void onDetachedFromWindow(){
        ErrorDialog.clear();
        super.onDetachedFromWindow();
    }

    @Override
    public boolean notifyAdapter(){
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }

        return true;
    }

    @Override
    public boolean updateAdapter(String type, Intent data) {
        if (!MainService.CHAT_MESSAGE.equals(type)) {
            updateAdapter();
            return true;
        }
        long matchId = Long.parseLong(data.getStringExtra("matchId"));
        long senderId = Long.parseLong(data.getStringExtra("senderId"));
        Match m = adapter.getMatch(matchId);
        m.hasMessage = true;
        if (senderId == m.female.getId()) {
            m.femaleHasMessage = true;
        } else {
            m.maleHasMessage = true;
        }
        adapter.notifyAdapters();
        return false;
    }

    @Override
    public boolean notifyAdapter(String type) {
        if (!MainService.CHAT_MESSAGE.equals(type)) {
            notifyAdapter();
            return true;
        }
        return false;
    }

    @Override
    public boolean updateAdapter(){
        user = UserService.getsUser(this);
        Server
                .getJboltService()
                .getMatchmakerMatches(user.getAccessToken(), user.getId(), new GetMatchesCallback(this));
        return true;
    }


    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        try {
            long matchId = Long.parseLong(intent.getStringExtra("matchId"));
            long senderId = Long.parseLong(intent.getStringExtra("senderId"));
            Match m = adapter.getMatch(matchId);
            AppUser otherUser = m.female.getId() == senderId ? m.female : m.male;

            if (m != null) {
                Intent goToChat = new Intent(this, ChatActivity.class);
                goToChat.putExtra(ChatActivity.APP_USER_ID_KEY,
                        new Gson().toJson(otherUser, AppUser.class));
                goToChat.putExtra(ChatActivity.MATCH_MAKER_ID_KEY,
                        new Gson().toJson(user, MatchMaker.class));
                goToChat.putExtra(ChatActivity.MATCH_OBJECT_ID_KEY,
                        new Gson().toJson(m, Match.class));
                goToChat.putExtra(ChatActivity.MATCH_ID_KEY, m.getId());
                goToChat.putExtra(ChatActivity.MY_ID_KEY, user.getId());
                startActivity(goToChat);
            }
        } catch (NumberFormatException e) {
            // do nothing
        }
    }

}
