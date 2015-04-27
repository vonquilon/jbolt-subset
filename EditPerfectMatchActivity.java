package com.syas.jbolt.model.user;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.syas.jbolt.BaseEditActivity;
import com.syas.jbolt.R;
import com.syas.jbolt.model.enums.CulturalBackground;
import com.syas.jbolt.model.enums.EducationLevel;
import com.syas.jbolt.model.enums.Ethnicity;
import com.syas.jbolt.model.enums.FemaleBodyType;
import com.syas.jbolt.model.enums.FemaleDress;
import com.syas.jbolt.model.enums.FrequencyOfTorahStudy;
import com.syas.jbolt.model.enums.HairCovering;
import com.syas.jbolt.model.enums.IKeepKosher;
import com.syas.jbolt.model.enums.MaleBodyType;
import com.syas.jbolt.model.enums.MaritalStatus;
import com.syas.jbolt.model.enums.Religiousity;
import com.syas.jbolt.model.enums.SmokingHabits;
import com.syas.jbolt.model.enums.WantToGoToIsrael;
import com.syas.jbolt.model.gson.EditPerfectMatch;
import com.syas.jbolt.views.DoubleSlider;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EditPerfectMatchActivity extends BaseEditActivity {

    @InjectView(R.id.addPreferredSmokingButton)
    ImageButton addPreferredSmokingButton;
    @InjectView(R.id.addPreferredBodyButton)
    ImageButton addPreferredBodyButton;
    @InjectView(R.id.addPreferredMaritalButton)
    ImageButton addPreferredMaritalButton;
    @InjectView(R.id.addPreferredEthnicityButton)
    ImageButton addPreferredEthnicityButton;
    @InjectView(R.id.addPreferredCultureButton)
    ImageButton addPreferredCultureButton;
    @InjectView(R.id.addPreferredReligionButton)
    ImageButton addPreferredReligionButton;
    @InjectView(R.id.addPreferredDressButton)
    ImageButton addPreferredDressButton;
    @InjectView(R.id.addPreferredHairCoverButton)
    ImageButton addPreferredHairCoverButton;
    @InjectView(R.id.addPreferredTorahStudyButton)
    ImageButton addPreferredTorahStudyButton;
    @InjectView(R.id.addPreferredKosherButton)
    ImageButton kosherButton;
    @InjectView(R.id.israelResponseButton)
    ImageButton israelResponseButton;

    @InjectView(R.id.preferredSmokingTextView)
    TextView preferredSmokingTextView;
    @InjectView(R.id.preferredBodyTextView)
    TextView preferredBodyTextView;
    @InjectView(R.id.preferredMaritalTextView)
    TextView preferredMaritalTextView;
    @InjectView(R.id.preferredEthnicityTextView)
    TextView preferredEthnicityTextView;
    @InjectView(R.id.preferredCultureTextView)
    TextView preferredCultureTextView;
    @InjectView(R.id.preferredReligionTextView)
    TextView preferredReligionTextView;
    @InjectView(R.id.preferredDressHolderTextView)
    TextView preferredDressHolderTextView;
    @InjectView(R.id.preferredHairCoverHolderTextView)
    TextView preferredHairCoverHolderTextView;
    @InjectView(R.id.preferredTorahStudyHolderTextView)
    TextView preferredTorahStudyHolderTextView;
    @InjectView(R.id.preferredKosherTextView)
    TextView kosherText;
    @InjectView(R.id.israelResponseTextView)
    TextView israelResponseTextView;
    @InjectView(R.id.okDatingBaalTeshuvaTextView)
    TextView okDatingBaalTeshuvaTextView;

    @InjectView(R.id.preferredDressTextView)
    TextView preferredDressTextView;
    @InjectView(R.id.preferredHairCoverTextView)
    TextView preferredHairCoverTextView;
    @InjectView(R.id.preferredTorahStudyTextView)
    TextView preferredTorahStudyTextView;
    @InjectView(R.id.heightRangeTextView)
    TextView heightRangeTextView;

    @InjectView(R.id.hasChildrenSpinner)
    Spinner hasChildrenSpinner;
    @InjectView(R.id.okDatingBaalTeshuvaSpinner)
    Spinner okDatingBaalTeshuvaSpinner;
    @InjectView(R.id.minimumEducationLevelSpinner)
    Spinner minimumEducationLevelSpinner;

    @InjectView(R.id.preferredHeightSlider)
    DoubleSlider preferredHeightSlider;

    private AppUser user;

    @Override
    public void onCreate(Bundle save){
        super.onCreate(save);
        setContentView(R.layout.activity_edit_perfect_match);
        ButterKnife.inject(this);

        setActionBarTitle(R.string.my_perfect_match);

        try {
            user = (AppUser) UserService.getsUser(this);
            if (user instanceof MaleAppUser) {
                MaleAppUser male = (MaleAppUser) user;
                if(male.perfectMatch == null)
                    male.perfectMatch = new FemalePerfectMatch();
                MaleAppUser empty = new MaleAppUser();
                empty.perfectMatch = new FemalePerfectMatch();
                mDiff = empty;
                setUpViews(((MaleAppUser) user).perfectMatch, empty.perfectMatch);
            } else {
                FemaleAppUser female = (FemaleAppUser) user;
                if(female.perfectMatch == null)
                    female.perfectMatch = new MalePerfectMatch();

                FemaleAppUser empty = new FemaleAppUser();
                empty.perfectMatch = new MalePerfectMatch();
                mDiff = empty;
                setUpViews(((FemaleAppUser) user).perfectMatch, empty.perfectMatch);
            }

        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public void setUpViews(FemalePerfectMatch match, FemalePerfectMatch empty) throws NoSuchFieldException, IllegalAccessException {
        setUpViews((PerfectUser)match,(PerfectUser)empty);

        if (match.femaleBodyTypePreference == null) {
            match.femaleBodyTypePreference = addAll(FemaleBodyType.values());
        }
        ActivityHelper.initMultiChoice(this, match.femaleBodyTypePreference, match.getClass().getDeclaredField("femaleBodyTypePreference"), empty, FemaleBodyType.values(),FemaleBodyType.MAPPER, preferredBodyTextView, addPreferredBodyButton);

        if (isReallyReligious(user)) {
            preferredDressTextView.setVisibility(View.VISIBLE);
            preferredDressHolderTextView.setVisibility(View.VISIBLE);
            addPreferredDressButton.setVisibility(View.VISIBLE);
            if (match.femaleDressPreference == null) {
                match.femaleDressPreference = getInitialPreferences(FemaleDress.values(), FemaleDress.SKIRTS);
            }
            ActivityHelper.initMultiChoice(this, match.femaleDressPreference, match.getClass().getDeclaredField("femaleDressPreference"), empty, FemaleDress.values(), FemaleDress.MAPPER, preferredDressHolderTextView, addPreferredDressButton);

            preferredHairCoverTextView.setVisibility(View.VISIBLE);
            preferredHairCoverHolderTextView.setVisibility(View.VISIBLE);
            addPreferredHairCoverButton.setVisibility(View.VISIBLE);
            if (match.hairCoveringListPreference == null) {
                match.hairCoveringListPreference = getInitialPreferences(HairCovering.values(), HairCovering.FULLY);
            }
            ActivityHelper.initMultiChoice(this, match.hairCoveringListPreference, match.getClass().getDeclaredField("hairCoveringListPreference"), empty, HairCovering.values(),HairCovering.MAPPER, preferredHairCoverHolderTextView, addPreferredHairCoverButton);
        } else {
            preferredDressTextView.setVisibility(View.GONE);
            preferredDressHolderTextView.setVisibility(View.GONE);
            addPreferredDressButton.setVisibility(View.GONE);

            preferredHairCoverTextView.setVisibility(View.GONE);
            preferredHairCoverHolderTextView.setVisibility(View.GONE);
            addPreferredHairCoverButton.setVisibility(View.GONE);
        }
    }


    public void setUpViews(MalePerfectMatch match, MalePerfectMatch empty) throws NoSuchFieldException, IllegalAccessException {
        setUpViews((PerfectUser)match,(PerfectUser)empty);

        if (match.maleBodyTypePreference == null) {
            match.maleBodyTypePreference = addAll(MaleBodyType.values());
        }
        ActivityHelper.initMultiChoice(this, match.maleBodyTypePreference, match.getClass().getDeclaredField("maleBodyTypePreference"), empty, MaleBodyType.values(),MaleBodyType.MAPPER, preferredBodyTextView, addPreferredBodyButton);

        if (isReallyReligious(user)) {
            preferredTorahStudyTextView.setVisibility(View.VISIBLE);
            preferredTorahStudyHolderTextView.setVisibility(View.VISIBLE);
            addPreferredTorahStudyButton.setVisibility(View.VISIBLE);

            if (((PerfectUser) match).torahStudyPreference == null) {
                ((PerfectUser) match).torahStudyPreference = addAll(FrequencyOfTorahStudy.values());
            }

            ActivityHelper.initMultiChoice(this, match.torahStudyPreference, match.getClass().getSuperclass().getDeclaredField("torahStudyPreference"), empty, FrequencyOfTorahStudy.values(), FrequencyOfTorahStudy.MAPPER, preferredTorahStudyHolderTextView, addPreferredTorahStudyButton);
        } else {
            preferredTorahStudyTextView.setVisibility(View.GONE);
            preferredTorahStudyHolderTextView.setVisibility(View.GONE);
            addPreferredTorahStudyButton.setVisibility(View.GONE);
        }
    }

    public void setUpViews(PerfectUser match, PerfectUser empty) throws NoSuchFieldException, IllegalAccessException {
        Class<?> superClazz = empty.getClass().getSuperclass();

        if (match.smokingPreference == null) {
            match.smokingPreference = addAll(SmokingHabits.values());
        }
        ActivityHelper.initMultiChoice(this, match.smokingPreference, superClazz.getDeclaredField("smokingPreference"), empty, SmokingHabits.values(),SmokingHabits.MAPPER, preferredSmokingTextView, addPreferredSmokingButton);
        if (match.maritalStatus == null) {
            match.maritalStatus = addAll(MaritalStatus.values());
        }
        ActivityHelper.initMultiChoice(this, match.maritalStatus, superClazz.getDeclaredField("maritalStatus"), empty, MaritalStatus.values(),MaritalStatus.MAPPER, preferredMaritalTextView, addPreferredMaritalButton);
        if (match.ethnicityPreference == null) {
            match.ethnicityPreference = addAll(Ethnicity.values());
        }
        ActivityHelper.initMultiChoice(this, match.ethnicityPreference, superClazz.getDeclaredField("ethnicityPreference"), empty, Ethnicity.values(),Ethnicity.MAPPER, preferredEthnicityTextView, addPreferredEthnicityButton);
        if (match.culturalBackgroundPreference == null) {
            match.culturalBackgroundPreference = addAll(CulturalBackground.values());
        }
        ActivityHelper.initMultiChoice(this, match.culturalBackgroundPreference, superClazz.getDeclaredField("culturalBackgroundPreference"), empty, CulturalBackground.values(),CulturalBackground.MAPPER, preferredCultureTextView, addPreferredCultureButton);

        if (match.religiousityPreference == null) {
            match.religiousityPreference = getInitialReligiousPreferences();
        }
        ActivityHelper.initMultiChoice(this, match.religiousityPreference, superClazz.getDeclaredField("religiousityPreference"),empty, Religiousity.values(),Religiousity.MAPPER, preferredReligionTextView, addPreferredReligionButton);

        if (match.keepKosherPreference == null) {
            match.keepKosherPreference = getInitialPreferences(IKeepKosher.values(), IKeepKosher.ALWAYS);
        }
        ActivityHelper.initMultiChoice(this, match.keepKosherPreference, superClazz.getDeclaredField("keepKosherPreference"), empty, IKeepKosher.values(), IKeepKosher.MAPPER, kosherText, kosherButton);
        if (match.wantToGoToIsraelPreference == null) {
            match.wantToGoToIsraelPreference = addAll(WantToGoToIsrael.values());
        }
        ActivityHelper.initMultiChoice(this, match.wantToGoToIsraelPreference, superClazz.getDeclaredField("wantToGoToIsraelPreference"), empty, WantToGoToIsrael.values(), WantToGoToIsrael.MAPPER, israelResponseTextView, israelResponseButton);

        ActivityHelper.initSpinner(superClazz.getDeclaredField("goOutWithSomeoneWhoHasChildren"), match.goOutWithSomeoneWhoHasChildren, empty, hasChildrenSpinner, null);

        if (isReallyReligious(user)) {
            ActivityHelper.initSpinner(superClazz.getDeclaredField("datingBalTeshuva"), match.datingBalTeshuva, empty, okDatingBaalTeshuvaSpinner, null);
        } else {
            okDatingBaalTeshuvaTextView.setVisibility(View.GONE);
            okDatingBaalTeshuvaSpinner.setVisibility(View.GONE);
        }

        ActivityHelper.initSpinner(minimumEducationLevelSpinner, EducationLevel.values(), match.educationLevel, EducationLevel.MAPPER, superClazz.getDeclaredField("educationLevel"), empty);

        empty.minHeightInCms = match.minHeightInCms;
        empty.maxHeightInCms = match.maxHeightInCms;
        ActivityHelper.initSlider(preferredHeightSlider, heightRangeTextView, empty);
    }

    private List<Religiousity> getInitialReligiousPreferences() {
        List<Religiousity> preference = new ArrayList<>();

        switch (user.religiousity) {
            case CONSERVATIVE:
                preference.add(Religiousity.ORTHODOX);
            case JUST_JEWISH:
            case REFORM:
            case TRADITIONAL:
                preference.add(Religiousity.CONSERVATIVE);
                preference.add(Religiousity.JUST_JEWISH);
                preference.add(Religiousity.REFORM);
                preference.add(Religiousity.TRADITIONAL);
                break;
            case ORTHODOX:
                preference.add(Religiousity.CONSERVATIVE);
            case YESHIVISH:
                preference.add(Religiousity.ORTHODOX);
                preference.add(Religiousity.YESHIVISH);
                break;
            case WILLING_TO_CONVERT:
                preference.add(Religiousity.WILLING_TO_CONVERT);
            default:
        }

        return preference;
    }

    private <T> List<T> getInitialPreferences(T[] vals, T defaultVal) {
        List<T> preference = new ArrayList<>();

        switch (user.religiousity) {
            case CONSERVATIVE:
            case JUST_JEWISH:
            case REFORM:
            case TRADITIONAL:
            case WILLING_TO_CONVERT:
                preference.addAll(addAll(vals));
                break;
            case YESHIVISH:
                preference.add(defaultVal);
            default:
        }

        return preference;
    }

    private <T> List<T> addAll(T[] vals) {
        List<T> all = new ArrayList<>();
        for (T val : vals) {
            all.add(val);
        }
        return all;
    }

    public static boolean isReallyReligious(AppUser appUser) {
        if (appUser.religiousity == null)
            return false;

        switch (appUser.religiousity) {
            case YESHIVISH:
            case ORTHODOX:
                return true;
            default:
                return false;
        }
    }

    @Override
    public Class<? extends Annotation> getExlude() {
        return EditPerfectMatch.class;
    }
}
