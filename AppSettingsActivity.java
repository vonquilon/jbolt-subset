package com.syas.jbolt.model.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;

import com.facebook.Session;
import com.facebook.SessionState;
import com.google.gson.GsonBuilder;
import com.syas.jbolt.BaseEditActivity;
import com.syas.jbolt.ErrorDialog;
import com.syas.jbolt.ImportDataActivity;
import com.syas.jbolt.PartnerSiteActivity;
import com.syas.jbolt.R;
import com.syas.jbolt.login.LoginActivity;
import com.syas.jbolt.model.gson.EditSettings;

import java.lang.annotation.Annotation;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.syas.jbolt.model.user.ActivityHelper.initSwitch;

public class AppSettingsActivity extends BaseEditActivity implements Session.StatusCallback {

    private static final String REPORT_ISSUE_SUBJECT = "Report an issue";
    private static final String FEEDBACK_SUBJECT = "FeedBack";
    private static final String AFFILIATE_SUBJECT = "Affiliate";

    @InjectView(R.id.notifyMeSwitch)
    Switch notifyMeSwitch;
    @InjectView(R.id.newMatchesSwitch)
    Switch newMatchesSwitch;
    @InjectView(R.id.messagesSwitch)
    Switch messagesSwitch;
    @InjectView(R.id.vibrationsSwitch)
    Switch vibrationsSwitch;

    @Override
    protected void onCreate(Bundle save) {
        super.onCreate(save);
        setContentView(R.layout.activity_app_settings);
        ButterKnife.inject(this);

        setActionBarTitle(R.string.app_settings);

        User user = UserService.getsUser(this);
        mDiff = user.isMale() ? new MaleAppUser() : new FemaleAppUser();

        try {
            setUpViews(user, mDiff);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void setUpViews(User user, AppUser empty) throws NoSuchFieldException {
        initSwitch(notifyMeSwitch, empty, "notifyMeWhenFriendsAreLiked", user.notifyMeWhenFriendsAreLiked);
        initSwitch(newMatchesSwitch, empty, "newMatches", user.newMatches);
        initSwitch(messagesSwitch, empty, "messages", user.messages);
        initSwitch(vibrationsSwitch, empty, "inAppVibrations", user.inAppVibrations);
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.e(null, new GsonBuilder().setPrettyPrinting().create().toJson(mDiff));
    }

    @Override
    public Class<? extends Annotation> getExlude() {
        return EditSettings.class;
    }

    @Override
    public void call(Session session, SessionState sessionState, Exception e) {
        onSessionStateChange(session, sessionState, e);
    }

    private void onSessionStateChange(Session session, SessionState state,
                                      Exception exception) {
        if (exception != null) {
            Log.e(null, exception.getMessage());
        } else {
            Log.e(null, "state " + state.toString());
        }
        if (state.isClosed()) {
            Log.e(null, "Logged out");
            clearSession();
        }
    }

    @OnClick(R.id.affiliateButton)
    public void affiliateClick(Button b) {
        Intent affiliate = new Intent(Intent.ACTION_SENDTO);
        affiliate
                .setData(Uri.parse("mailto:"))
                .putExtra(Intent.EXTRA_EMAIL, new String[]{"partners@jbolt.com"})
                .putExtra(Intent.EXTRA_SUBJECT, AFFILIATE_SUBJECT);
        if (affiliate.resolveActivity(b.getContext().getPackageManager()) != null)
            startActivity(affiliate);
    }

    @OnClick(R.id.importDataButton)
    public void importClick() {
        AppUser user = (AppUser) UserService.getsUser(this);
        if (!user.hasCompletedProfile() && user.canLinkProfile()) {
            startActivity(new Intent(this, ImportDataActivity.class));
        } else {
            ErrorDialog.show(this, 999);
        }
    }

    @OnClick(R.id.partnerButton)
    public void partnerClick() {
        startActivity(new Intent(this, PartnerSiteActivity.class));
    }

    @OnClick(R.id.reportIssueButton)
    public void reportClick(Button b) {
        Intent reportIntent = new Intent(Intent.ACTION_SENDTO);
        reportIntent
                .setData(Uri.parse("mailto:"))
                .putExtra(Intent.EXTRA_EMAIL, new String[]{"bugs@jbolt.com"})
                .putExtra(Intent.EXTRA_SUBJECT, REPORT_ISSUE_SUBJECT);
        if (reportIntent.resolveActivity(b.getContext().getPackageManager()) != null)
            startActivity(reportIntent);
    }

    @OnClick(R.id.feedbackButton)
    public void feedbackClick(Button b) {
        Intent feedbackIntent = new Intent(Intent.ACTION_SENDTO);
        feedbackIntent
                .setData(Uri.parse("mailto:"))
                .putExtra(Intent.EXTRA_EMAIL, new String[]{"suggestions@jbolt.com"})
                .putExtra(Intent.EXTRA_SUBJECT, FEEDBACK_SUBJECT);
        if (feedbackIntent.resolveActivity(b.getContext().getPackageManager()) != null)
            startActivity(feedbackIntent);
    }

    @OnClick(R.id.privacyButton)
    public void privacyClick() {
        startActivity(new Intent(this, PrivacyPolicyActivity.class));
    }

    @OnClick(R.id.termsButton)
    public void tosClick() {
        startActivity(new Intent(this, TermsOfServiceActivity.class));
    }

    @OnClick(R.id.logOut)
    public void logout() {
        UserService.logout(this);
        clearSession();
        Intent logout = new Intent(this, LoginActivity.class);
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(logout);
    }


    public void clearSession() {
        Session session = Session.getActiveSession();
        if (session != null) {
            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
            }
        } else {
            session = new Session(this);
            Session.setActiveSession(session);
            session.closeAndClearTokenInformation();
        }
    }
}
