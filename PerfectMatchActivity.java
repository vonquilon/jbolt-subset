package com.syas.jbolt.model.user;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.syas.jbolt.BaseActivity;
import com.syas.jbolt.R;
import com.syas.jbolt.model.enums.CulturalBackground;
import com.syas.jbolt.model.enums.EducationLevel;
import com.syas.jbolt.model.enums.EnumMapper;
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

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PerfectMatchActivity extends BaseActivity {

    @InjectView(R.id.preferredHeightTextView)
    TextView preferredHeightTextView;
    @InjectView(R.id.preferredBodyTypeTextView)
    TextView preferredBodyTypeTextView;
    @InjectView(R.id.preferredMaritalStatusTextView)
    TextView preferredMaritalStatusTextView;
    @InjectView(R.id.hasChildrenTextView)
    TextView hasChildrenTextView;
    @InjectView(R.id.smokingHabitsTextView)
    TextView smokingHabitsTextView;
    @InjectView(R.id.ethnicityPerfectTextView)
    TextView ethnicityPerfectTextView;
    @InjectView(R.id.culturePerfectTextView)
    TextView culturePerfectTextView;
    @InjectView(R.id.preferredIsraelTextView)
    TextView preferredIsraelTextView;
    @InjectView(R.id.minimumEducationTextView)
    TextView minimumEducationTextView;
    @InjectView(R.id.religiousOrientationTextView)
    TextView religiousOrientationTextView;
    @InjectView(R.id.kosherObservanceTextView)
    TextView kosherObservanceTextView;
    @InjectView(R.id.baalPerfectTextView)
    TextView baalPerfectTextView;
    @InjectView(R.id.dressPerfectTextView)
    TextView dressPerfectTextView;
    @InjectView(R.id.hairCoverPerfectTextView)
    TextView hairCoverPerfectTextView;
    @InjectView(R.id.torahStudyTextView)
    TextView torahStudyTextView;

    private Resources res;

    @Override
    protected void onCreate(Bundle save) {
        super.onCreate(save);
        setContentView(R.layout.activity_perfect_match);
        ButterKnife.inject(this);
        res = getResources();

        AppUser user = ProfilePageActivity.getUser(getIntent());
        setActionBarTitle(res.getString(R.string.user_perfect_match, user.firstName));
        if (user instanceof MaleAppUser) {
            if (((MaleAppUser) user).perfectMatch != null) {
                setUpViews(
                        ((MaleAppUser) user).perfectMatch
                );
            }
        } else {
            if (((FemaleAppUser) user).perfectMatch != null) {
                setUpViews(
                        ((FemaleAppUser) user).perfectMatch
                );
            }
        }
    }

    private void setUpViews(FemalePerfectMatch match) {
        setUpViews((PerfectUser) match);

        torahStudyTextView.setVisibility(View.GONE);

        preferredBodyTypeTextView.setText(getListAsString(
                match.femaleBodyTypePreference, FemaleBodyType.MAPPER, R.string.body_type_perfect_match
        ));
        dressPerfectTextView.setText(getListAsString(
                match.femaleDressPreference, FemaleDress.MAPPER, R.string.dress_perfect_match
        ));
        hairCoverPerfectTextView.setText(getListAsString(
                match.hairCoveringListPreference, HairCovering.MAPPER, R.string.hair_cover_perfect_match
        ));
    }

    private void setUpViews(MalePerfectMatch match) {
        setUpViews((PerfectUser) match);

        dressPerfectTextView.setVisibility(View.GONE);
        hairCoverPerfectTextView.setVisibility(View.GONE);

        preferredBodyTypeTextView.setText(getListAsString(
                match.maleBodyTypePreference, MaleBodyType.MAPPER, R.string.body_type_perfect_match
        ));
        torahStudyTextView.setText(getListAsString(
                match.torahStudyPreference, FrequencyOfTorahStudy.MAPPER, R.string.torah_study_perfect_match
        ));
    }

    private void setUpViews(PerfectUser match) {
        preferredHeightTextView.setText(ProfilePageActivity.toBlackString(
                res.getString(R.string.preferred_height)
                , Utils.cmsToInches(match.minHeightInCms) + " - " + Utils.cmsToInches(match.maxHeightInCms)
        ));
        preferredMaritalStatusTextView.setText(getListAsString(
                match.maritalStatus, MaritalStatus.MAPPER, R.string.preferred_marital_status_perfect_match
        ));
        ProfilePageActivity.setYesNoTextView(
                hasChildrenTextView
                , match.goOutWithSomeoneWhoHasChildren
                , res.getString(R.string.has_children)
        );
        smokingHabitsTextView.setText(getListAsString(
                match.smokingPreference, SmokingHabits.MAPPER, R.string.preferred_smoking_habits_perfect_match
        ));
        ethnicityPerfectTextView.setText(getListAsString(
                match.ethnicityPreference, Ethnicity.MAPPER, R.string.ethnicity_perfect_match
        ));
        culturePerfectTextView.setText(getListAsString(
                match.culturalBackgroundPreference, CulturalBackground.MAPPER, R.string.cultural_background_perfect_match
        ));
        preferredIsraelTextView.setText(getListAsString(
                match.wantToGoToIsraelPreference, WantToGoToIsrael.MAPPER, R.string.move_to_israel_perfect_match
        ));
        if (match.educationLevel != null) {
            minimumEducationTextView.setText(ProfilePageActivity.toBlackString(
                    res.getString(R.string.minimum_education_perfect_match)
                    , EducationLevel.MAPPER.get(match.educationLevel)
            ));
        }
        religiousOrientationTextView.setText(getListAsString(
                match.religiousityPreference, Religiousity.MAPPER, R.string.religious_orientation_perfect_match
        ));
        kosherObservanceTextView.setText(getListAsString(
                match.keepKosherPreference, IKeepKosher.MAPPER, R.string.kosher_observance_perfect_match
        ));
        ProfilePageActivity.setYesNoTextView(
                baalPerfectTextView
                , match.datingBalTeshuva
                , res.getString(R.string.ok_baal_teshuva)
        );
    }

    private <T> Spanned getListAsString(List<T> vals, EnumMapper<T> mapper, int strId) {
        String temp = "";
        String resStr = res.getString(strId);

        if (vals != null) {
            temp += mapper.get(vals.get(0));
            for (int i = 1; i < vals.size(); i++) {
                temp += ", " + mapper.get(vals.get(i));
            }
            return ProfilePageActivity.toBlackString(resStr, temp);
        } else {
            return new SpannableString(resStr);
        }
    }
}