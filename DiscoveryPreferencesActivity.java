package com.syas.jbolt.model.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.syas.jbolt.BaseEditActivity;
import com.syas.jbolt.R;
import com.syas.jbolt.model.enums.DatingStatus;
import com.syas.jbolt.model.enums.Religiousity;
import com.syas.jbolt.model.gson.EditDiscoveryPreferences;
import com.syas.jbolt.views.DoubleSlider;

import java.lang.annotation.Annotation;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;

import static com.syas.jbolt.model.user.ActivityHelper.initAgeSlider;
import static com.syas.jbolt.model.user.ActivityHelper.initSpinner;
import static com.syas.jbolt.model.user.ActivityHelper.initSwitch;

public class DiscoveryPreferencesActivity extends BaseEditActivity implements SeekBar.OnSeekBarChangeListener{

    @InjectView(R.id.datingStatusSpinner)
    Spinner datingStatusSpinner;

    Religiousity [] prefs = {Religiousity.YESHIVISH,Religiousity.ORTHODOX,Religiousity.CONSERVATIVE,Religiousity.TRADITIONAL,
            Religiousity.REFORM,Religiousity.JUST_JEWISH,Religiousity.WILLING_TO_CONVERT};
    @InjectViews({R.id.yeshivishSwitch, R.id.orthodoxSwitch, R.id.conservativeSwitch,
            R.id.traditionalSwitch,R.id.reformSwitch,R.id.justJewishSwitch,R.id.convertSwitch})
    Switch[] religiousPreferences;
    @InjectViews({R.id.yeshivishContainer, R.id.orthodoxContainer, R.id.conservativeContainer,
            R.id.traditionalContainer,R.id.reformContainer, R.id.justJewishContainer, R.id.willingToConvertContainer})
    View[] preferenceContainers;

    @InjectView(R.id.discoverySwitch)
    Switch discoverySwitch;
    @InjectView(R.id.showFBSwitch)
    Switch showFBSwitch;
    @InjectView(R.id.eventModeSwitch)
    Switch eventModeSwitch;

    @InjectView(R.id.distanceSeekBar)
    SeekBar distanceSeekBar;
    @InjectView(R.id.distanceTextView)
    TextView distanceTextView;

    @InjectView(R.id.ageSlider)
    DoubleSlider ageSlider;
    @InjectView(R.id.ageTextView)
    TextView ageTextView;

    @Override
    protected void onCreate(Bundle save){
        super.onCreate(save);
        setContentView(R.layout.activity_discovery_preferences);
        ButterKnife.inject(this);

        setActionBarTitle(R.string.discovery_preferences);

        AppUser user = (AppUser) UserService.getsUser(this);
        try {
            if(user instanceof MaleAppUser){
                mDiff = new MaleAppUser();
                setUpViews((MaleAppUser)user,(MaleAppUser) mDiff);
            }else {
                mDiff = new FemaleAppUser();
                setUpViews((FemaleAppUser) user, (FemaleAppUser) mDiff);
            }
            setUpViews((AppUser) user, (AppUser) mDiff);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void setUpViews(MaleAppUser user, MaleAppUser empty) {
    }

    private void setUpViews(FemaleAppUser user, FemaleAppUser empty) {
    }

    private void setUpViews(AppUser user, AppUser empty) throws NoSuchFieldException {
        Class<?> superClazz = empty.getClass().getSuperclass();

        initSpinner(datingStatusSpinner, DatingStatus.values(), user.datingStatus, DatingStatus.MAPPER, superClazz.getDeclaredField("datingStatus"), empty);

        initReligiousPreferences(user,empty);

        initSwitch(discoverySwitch, empty, "isSearchable",user.isSearchable);
        initSwitch(eventModeSwitch, empty, "isEventMode",user.isEventMode);
        initSwitch(showFBSwitch, empty, "filterFriends", user.filterFriends);

        distanceSeekBar.setMax(100);
        distanceSeekBar.setProgress(user.searchRadius);
        distanceTextView.setText(user.searchRadius+" mi");
        empty.searchRadius = user.searchRadius;
        distanceSeekBar.setOnSeekBarChangeListener(this);

        empty.minAge = user.minAge;
        empty.maxAge = user.maxAge;
        initAgeSlider(ageSlider, ageTextView, empty);
    }

    private void initReligiousPreferences(AppUser u, AppUser empty){
        empty.religiousityPreferences = u.religiousityPreferences;
        CompoundButton.OnCheckedChangeListener listener = new ReligiousPreferencesListener(u.religiousityPreferences);

        int begin = 0;
        int end = 0;

        Religiousity r = u.getReligiousity();
        switch(r){
            case YESHIVISH:
                begin = 0; // begin at yeshivish
                end = 1; // end at orthodox
                break;
            case ORTHODOX:
                begin = 0;
                end = 3;
                break;
            case CONSERVATIVE:
            case TRADITIONAL:
                begin = 1;
                end = 5;
                break;
            case REFORM:
            case JUST_JEWISH:
                begin = 2;
                end = 5;
                break;
            case WILLING_TO_CONVERT:
                begin = end = 6;
                break;
        }


        for(int i = 0; i < religiousPreferences.length; i++){
            religiousPreferences[i].setTag(prefs[i]);
            if(i >= begin && i <= end) {
                religiousPreferences[i].setChecked(u.religiousityPreferences.contains(prefs[i]));
                religiousPreferences[i].setOnCheckedChangeListener(listener);
            }else{
                preferenceContainers[i].setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.e(null, new GsonBuilder().setPrettyPrinting().create().toJson(mDiff));
    }

    @Override
    public Class<? extends Annotation> getExlude() {
        return EditDiscoveryPreferences.class;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mDiff.searchRadius = progress;
        distanceTextView.setText(progress + " mi");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onBackPressed(){
        if(mDiff == null)
            super.onBackPressed();

        boolean shouldRefresh = true;
        Intent intent = new Intent();
        intent.putExtra("refresh",shouldRefresh);
        setResult(RESULT_OK,intent);
        finish();
    }
}