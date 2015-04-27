package com.syas.jbolt.model.user;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.syas.jbolt.R;
import com.syas.jbolt.model.enums.AmICohen;
import com.syas.jbolt.model.enums.CulturalBackground;
import com.syas.jbolt.model.enums.DatingStatus;
import com.syas.jbolt.model.enums.EducationLevel;
import com.syas.jbolt.model.enums.Ethnicity;
import com.syas.jbolt.model.enums.FemaleBodyType;
import com.syas.jbolt.model.enums.FemaleDress;
import com.syas.jbolt.model.enums.FrequencyOfTorahStudy;
import com.syas.jbolt.model.enums.GrandmotherJewish;
import com.syas.jbolt.model.enums.HairCovering;
import com.syas.jbolt.model.enums.IKeepKosher;
import com.syas.jbolt.model.enums.JewishEducation;
import com.syas.jbolt.model.enums.MaleBodyType;
import com.syas.jbolt.model.enums.MaritalStatus;
import com.syas.jbolt.model.enums.MatchStatus;
import com.syas.jbolt.model.enums.MoreChildren;
import com.syas.jbolt.model.enums.PoliticalOrientation;
import com.syas.jbolt.model.enums.Profession;
import com.syas.jbolt.model.enums.Religiousity;
import com.syas.jbolt.model.enums.SmokingHabits;
import com.syas.jbolt.model.enums.UserType;
import com.syas.jbolt.model.enums.WantToGoToIsrael;
import com.syas.jbolt.model.gson.EditProfile;
import com.syas.jbolt.model.gson.EditProfilePhotos;
import com.syas.jbolt.server.PostCallback;
import com.syas.jbolt.server.Server;
import com.syas.jbolt.server.UpdateMatch;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.syas.jbolt.model.user.Utils.cmsToInches;

public class ProfilePageActivity extends Activity implements MatchingStatusDialogFragment.MatchingStatusDialogOnClickListener, View.OnClickListener {

    public static final String BLACK_FONT_BEGIN = "<font color=#000000>";
    public static final String BLACK_FONT_END = "</font>";

    private static final String Y = "Yes";
    private static final String N = "No";

    public static final String SELF = "SELF";
    public static final String MATCHER = "MATCHER";
    public static final String MATCHED = "MATCHED";
    public static final String UNMATCHED = "UNMATCHED";

    public static final String VIEWER_STATUS = "com.syas.jbolt.VIEWER.STATUS";

    public static final String MATCHING_STATUS_DIALOG = "MATCHING_STATUS";
    public static final String BLOCK_DIALOG = "BLOCK";

    @InjectView(R.id.profilePageImage)
    ImageButton profilePageImage;

    @InjectView(R.id.maritalStatusImage)
    ImageView maritalStatusImage;

    @InjectView(R.id.userTextView)
    TextView userTextView;
    @InjectView(R.id.relationshipStatusTextView)
    TextView relationshipStatusTextView;
    @InjectView(R.id.cityStateZipTextView)
    TextView cityStateZipTextView;
    @InjectView(R.id.maritalStatusTextView)
    TextView maritalStatusTextView;
    @InjectView(R.id.phoneNumberTextView)
    TextView phoneNumberTextView;
    @InjectView(R.id.attributesTextView)
    TextView attributesTextView;
    @InjectView(R.id.hometownTextView)
    TextView hometownTextView;
    @InjectView(R.id.languagesTextView)
    TextView languagesTextView;
    @InjectView(R.id.moveToIsraelTextView)
    TextView moveToIsraelTextView;
    @InjectView(R.id.haveChildrenTextView)
    TextView haveChildrenTextView;
    @InjectView(R.id.howManyQuestionTextView)
    TextView howManyQuestionTextView;
    @InjectView(R.id.additionalChildrenTextView)
    TextView additionalChildrenTextView;
    @InjectView(R.id.politicalOrientationTextView)
    TextView politicalOrientationTextView;
    @InjectView(R.id.educationTextView)
    TextView educationTextView;
    @InjectView(R.id.degreeTextView)
    TextView degreeTextView;
    @InjectView(R.id.professionTextView)
    TextView professionTextView;
    @InjectView(R.id.jewishEducationTextView)
    TextView jewishEducationTextView;
    @InjectView(R.id.descriptionTextView)
    TextView descriptionTextView;
    @InjectView(R.id.religiosityTextView)
    TextView religiosityTextView;
    @InjectView(R.id.motherJewishTextView)
    TextView motherJewishTextView;
    @InjectView(R.id.kosherTextView)
    TextView kosherTextView;
    @InjectView(R.id.baalTeshuvaTextView)
    TextView baalTeshuvaTextView;
    @InjectView(R.id.aboutMeTextView)
    TextView aboutMeTextView;
    @InjectView(R.id.hairCoverProfileTextView)
    TextView hairCoverProfileTextView;
    @InjectView(R.id.dressProfileTextView)
    TextView dressProfileTextView;
    @InjectView(R.id.convertTextView)
    TextView convertTextView;
    @InjectView(R.id.canMarryCohenTextView)
    TextView canMarryCohenTextView;
    @InjectView(R.id.frequencyOfTorahTextView)
    TextView frequencyOfTorahTextView;

    @InjectView(R.id.canMarryCohenContainer)
    LinearLayout canMarryCohenContainer;
    @InjectView(R.id.relationshipStatusContainer)
    LinearLayout relationshipStatusContainer;
    @InjectView(R.id.phoneNumberContainer)
    LinearLayout phoneNumberContainer;
    @InjectView(R.id.cityStateZipContainer)
    LinearLayout cityStateZipContainer;
    @InjectView(R.id.convertContainer)
    LinearLayout convertContainer;
    @InjectView(R.id.hairCoverAndDressContainer)
    LinearLayout hairCoverAndDressContainer;
    @InjectView(R.id.motherJewishContainer)
    LinearLayout motherJewishContainer;

    private AppUser user;
    private String viewerStatus;
    private Intent intent;

    private CharSequence[] matchingStatusChoices, blockChoices;

    private String currMatchStatus;

    private LinearLayout menuButtonsContainer;

    @Override
    protected void onCreate(Bundle save){
        super.onCreate(save);
        setContentView(R.layout.activity_profile_page);
        ButterKnife.inject(this);
        intent = getIntent();
        user = getUser(intent);
        Log.e(null, "here6");
        if(save != null){
            Log.e(null, "here5");
            currMatchStatus = save.getString("currMatchStatus");
            viewerStatus = save.getString("viewerStatus");
            if (matchingStatusChoices == null) {
                CharSequence[] choices = getResources().getStringArray(R.array.matching_status_choices);
                if (!viewerStatus.equals(MATCHER)) {
                    matchingStatusChoices = new CharSequence[choices.length - 1];
                    System.arraycopy(choices, 0, matchingStatusChoices, 0, matchingStatusChoices.length);
                } else {
                    matchingStatusChoices = choices;
                }
            }
        }else {
            viewerStatus = intent.getStringExtra(VIEWER_STATUS);
            currMatchStatus = intent.getStringExtra("matchStatus");
        }

        menuButtonsContainer = (LinearLayout) getActionBar()
                .getCustomView()
                .findViewById(R.id.buttonsContainer);

        setUp();
    }

    @Override
    public void onSaveInstanceState(Bundle out){
        super.onSaveInstanceState(out);
        if(currMatchStatus != null && viewerStatus != null){
            out.putString("currMatchStatus",currMatchStatus);
            out.putString("viewerStatus",viewerStatus);
        }
    }

    private void setUp(){
        showYellowLocks();
        showRedLocks();
        setUpViews((AppUser) user);
        if (user instanceof MaleAppUser)
            setUpViews((MaleAppUser) user);
        else
            setUpViews((FemaleAppUser) user);

        switch (viewerStatus) {
            case SELF:
            case MATCHER:
                setUpViewsShowAll(user);
                break;
            case MATCHED:
                hideRedLocks();
                setUpViewsMatched(user);
                break;
            case UNMATCHED:
                hideRedLocks();
                hideYellowLocks();
                break;
        }
    }

    public static AppUser getUser(Intent intent){
        String json = intent.getStringExtra(Utils.JSON);
        Log.e(null,json);
        return new GsonBuilder()
                .registerTypeAdapter(AppUser.class, new UserDeserializer())
                .create()
                .fromJson(json, AppUser.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_profile, menu);
        menu.findItem(R.id.profile_menu)
                .setVisible(false)
                .setEnabled(false);
        MenuItem edit_profile = menu.findItem(R.id.edit_profile);
        MenuItem more_menu = menu.findItem(R.id.more_menu);
        if (currMatchStatus == null) {
            more_menu.setVisible(false).setEnabled(false);
        }
        if(user.getId() == UserService.getsUser(this).getId()) {
            more_menu.setVisible(false).setEnabled(false);
            return true;
        } else if (viewerStatus.equals(MATCHED)) {
            edit_profile.setVisible(false).setEnabled(false);
            ImageView menuBlock = (ImageView) menuButtonsContainer
                    .findViewById(R.id.menuBlock);
            menuBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (blockChoices == null)
                        blockChoices = getResources().getStringArray(R.array.block_choices);
                    Bundle bundle = new Bundle();
                    bundle.putCharSequenceArray(BlockDialogFragment.BLOCK_ID, blockChoices);
                    DialogFragment dialog = new BlockDialogFragment();
                    dialog.setArguments(bundle);
                    dialog.show(getFragmentManager(), BLOCK_DIALOG);
                }
            });
            menuBlock.setVisibility(View.VISIBLE);
            return true;
        } else if (viewerStatus.equals(MATCHER)) {
            edit_profile.setVisible(false).setEnabled(false);
            ImageView menuPerfectMatch = (ImageView) menuButtonsContainer
                    .findViewById(R.id.menuPerfectMatch);
            menuPerfectMatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfilePageActivity.this, PerfectMatchActivity.class);
                    intent.putExtra(
                            Utils.JSON
                            , ProfilePageActivity.this.intent.getStringExtra(Utils.JSON)
                    );
                    startActivity(intent);
                }
            });
            menuPerfectMatch.setVisibility(View.VISIBLE);
            
            return true;
        } else {
            edit_profile.setVisible(false).setEnabled(false);
            menu.findItem(R.id.empty)
                    .setVisible(true);
            return true;
        }

    }


    public static final int EDIT = 1232;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            case R.id.edit_profile: {
                Intent intent = new Intent(this, EditProfileActivity.class);
                startActivityForResult(intent, EDIT);
                break;
            }
            case R.id.more_menu: {
                if (matchingStatusChoices == null) {
                    CharSequence[] choices = getResources().getStringArray(R.array.matching_status_choices);
                    if (!viewerStatus.equals(MATCHER)) {
                        matchingStatusChoices = new CharSequence[choices.length - 1];
                        System.arraycopy(choices, 0, matchingStatusChoices, 0, matchingStatusChoices.length);
                    } else {
                        matchingStatusChoices = choices;
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putCharSequenceArray(MatchingStatusDialogFragment.MATCHING_STATUS_ID, matchingStatusChoices);
                bundle.putString(MatchingStatusDialogFragment.INITIAL_MATCHING_STATUS_ID, currMatchStatus);
                DialogFragment dialog = new MatchingStatusDialogFragment();
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), MATCHING_STATUS_DIALOG);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int request, int result, final Intent data){
        super.onActivityResult(request, result, data);
        if(request == EDIT && result == RESULT_OK) {
            new Thread() {
                public void run() {
                    try {
                        AppUser diff = getUser(data);
                        update(diff);
                        new Handler(Looper.getMainLooper())
                                .post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setUp();
                                    }
                                });
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    private void update(AppUser diff) throws Exception{
        Class<?> clazz = diff.getClass();
        if(!clazz.getCanonicalName().equals(user.getClass().getCanonicalName())){
            throw new RuntimeException("Trying to update with the wrong class");
        }
        do {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object val = field.get(diff);
                if(val != null && (field.getAnnotation(EditProfile.class) != null || field.getAnnotation(EditProfilePhotos.class) != null)){
                    Log.e(null, "Updating " + field.getName() + " to " + val);
                    field.set(user,val);
                }
            }
            clazz = clazz.getSuperclass();
            Log.e(null, clazz.getName());
        }while( !"java.lang.Object".equals(clazz.getName()));
        Log.e(null, "DONE");
    }

    private void setUpViews(MaleAppUser user) {
        String name = (user.firstName != null ? user.firstName.trim() : "") + " "
                + (user.lastName != null ? user.lastName.trim() : "");
        if (user.age > 0){
            name += ", " + user.age;
        }
        name += (UserService.getsUser(this).getUserType() == UserType.MATCH_MAKER ? " (M)" : "");
        userTextView.setText(name);

        if (attributesTextView.getText().length() != 0 && user.bodyType != null)
            attributesTextView.append(", ");
        attributesTextView.append(user.bodyType == null ? "" : MaleBodyType.MAPPER.get(user.bodyType));
        if (attributesTextView.getText().length() != 0 && user.smokingHabits != null)
            attributesTextView.append(", ");
        attributesTextView.append(user.smokingHabits == null ? "" : SmokingHabits.MAPPER.get(user.smokingHabits));
        if (attributesTextView.getText().length() == 0)
            attributesTextView.setVisibility(View.GONE);

        frequencyOfTorahTextView.setVisibility(View.VISIBLE);

        if (user.religiousity != null) {
            if (user.amICohen != null && user.religiousity != Religiousity.WILLING_TO_CONVERT) {
                final Drawable cohenIcon = getResources().getDrawable(R.drawable.cohen_profile_page);
                cohenIcon.setBounds(0, 0, religiosityTextView.getLineHeight() + 10, religiosityTextView.getLineHeight());
                String string;
                if (religiosityTextView.getVisibility() == View.GONE) {
                    religiosityTextView.setVisibility(View.VISIBLE);
                    religiosityTextView.setText("");
                    string = "   ";
                } else {
                    string = religiosityTextView.getText() + ",   ";
                }
                final SpannableStringBuilder ssb = new SpannableStringBuilder(string);
                final ImageSpan imageSpan = new ImageSpan(cohenIcon);
                ssb.setSpan(imageSpan, string.length() - 2, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                religiosityTextView.setText(ssb);
                religiosityTextView.append(AmICohen.MAPPER.get(user.amICohen));
            }

            if (EditPerfectMatchActivity.isReallyReligious(user)) {
                frequencyOfTorahTextView.setText(user.frequencyOfTorahStudy == null ? getTitle(R.string.torah_profile) :
                        toBlackString(getTitle(R.string.torah_profile), FrequencyOfTorahStudy.MAPPER.get(user.frequencyOfTorahStudy)));
            } else {
                frequencyOfTorahTextView.setVisibility(View.GONE);
            }
        }

        hairCoverAndDressContainer.setVisibility(View.GONE);
        canMarryCohenContainer.setVisibility(View.GONE);
    }

    private void setUpViews(FemaleAppUser user) {
        String name = (user.firstName != null ? user.firstName.trim() : "") + " "
                + (user.lastName != null ? user.lastName.trim() : "");
        if (user.age >  0){
            name += ", " + user.age;
        }
        name += (UserService.getsUser(this).getUserType() == UserType.MATCH_MAKER ? " (F)" : "");
        userTextView.setText(name);

        if (attributesTextView.getText().length() != 0 && user.bodyType != null)
            attributesTextView.append(", ");
        attributesTextView.append(user.bodyType == null ? "" : FemaleBodyType.MAPPER.get(user.bodyType));
        if (attributesTextView.getText().length() != 0 && user.smokingHabits != null)
            attributesTextView.append(", ");
        attributesTextView.append(user.smokingHabits == null ? "" : SmokingHabits.MAPPER.get(user.smokingHabits));
        if (attributesTextView.getText().length() == 0)
            attributesTextView.setVisibility(View.GONE);

        canMarryCohenContainer.setVisibility(View.VISIBLE);
        hairCoverAndDressContainer.setVisibility(View.VISIBLE);

        if (user.religiousity != null) {
            if (EditPerfectMatchActivity.isReallyReligious(user)) {
                setYesNoTextView(canMarryCohenTextView, ((FemaleAppUser) user).canIMarryCohen, getTitle(R.string.can_marry_cohen));

                hairCoverProfileTextView.setText(user.hairCovering == null ? getTitle(R.string.hair_covering_profile) :
                        toBlackString(getTitle(R.string.hair_covering_profile), HairCovering.MAPPER.get(user.hairCovering)));
                dressProfileTextView.setText(user.femaleDress == null ? getTitle(R.string.dress_profile) :
                        toBlackString(getTitle(R.string.dress_profile), FemaleDress.MAPPER.get(user.femaleDress)));
            } else {
                canMarryCohenContainer.setVisibility(View.GONE);
                hairCoverAndDressContainer.setVisibility(View.GONE);
            }
        }

        frequencyOfTorahTextView.setVisibility(View.GONE);
    }

    private void setUpViewsMatched(AppUser user) {
        phoneNumberContainer.setVisibility(View.VISIBLE);
        phoneNumberTextView.setText(user.cell == null ? "" : user.cell.trim());
        if (phoneNumberTextView.getText().length() == 0)
            phoneNumberContainer.setVisibility(View.GONE);

        final List<String> temp = new ArrayList<>();
        if (user.city != null) {
            if (!user.city.trim().isEmpty())
                temp.add(user.city);
        }
        if (user.state != null) {
            if (!user.state.trim().isEmpty())
                temp.add(user.state);
        }
        if (user.zip != null) {
            if (!user.zip.trim().isEmpty())
                temp.add(user.zip);
        }
        if (temp.size() > 0) {
            cityStateZipTextView.setText(temp.get(0));
            for (int i = 1; i < temp.size(); i++)
                cityStateZipTextView.append(", " + temp.get(i));
        } else {
            cityStateZipContainer.setVisibility(View.GONE);
        }
    }

    private void setUpViewsShowAll(AppUser user) {
        setUpViewsMatched(user);
        relationshipStatusTextView.setText(user.datingStatus == null ? getTitle(R.string.relationship_status) :
                toBlackString(getTitle(R.string.relationship_status), DatingStatus.MAPPER.get(user.datingStatus)));

        if (user.religiousity != null) {
            if (user.religiousity == Religiousity.WILLING_TO_CONVERT) {
                convertContainer.setVisibility(View.GONE);
            } else {
                convertContainer.setVisibility(View.VISIBLE);
                setYesNoTextView(convertTextView, user.areYouAConvert, getTitle(R.string.convert));
            }
        }
    }

    private void hideRedLocks() {
        relationshipStatusContainer.setVisibility(View.GONE);
        cityStateZipContainer.setVisibility(View.GONE);
        convertContainer.setVisibility(View.GONE);
    }

    private void hideYellowLocks() {
        phoneNumberContainer.setVisibility(View.GONE);
    }

    private void showRedLocks() {
        cityStateZipContainer.setVisibility(View.VISIBLE);
        convertContainer.setVisibility(View.VISIBLE);
    }

    private void showYellowLocks() {
        phoneNumberContainer.setVisibility(View.VISIBLE);
        relationshipStatusContainer.setVisibility(View.VISIBLE);
    }

    private void setUpViews(AppUser user) {
        ActivityHelper.initProfileImage(profilePageImage, user, this);
        profilePageImage.setOnClickListener(this);
        String tempString;

        if (user.maritalStatus == null) {
            maritalStatusTextView.setText(getTitle(R.string.marital_status));
            maritalStatusImage.setVisibility(View.GONE);
        } else {
            tempString = MaritalStatus.MAPPER.get(user.maritalStatus);
            maritalStatusImage.setVisibility(View.VISIBLE);
            if (tempString.equals(MaritalStatus.MAPPER.get(MaritalStatus.DIVORCED)))
                maritalStatusImage.setImageResource(R.drawable.divorced_profile_page);
            else if (tempString.equals(MaritalStatus.MAPPER.get(MaritalStatus.SINGLE)))
                maritalStatusImage.setImageResource(R.drawable.single_profile_page);
            else
                maritalStatusImage.setImageResource(R.drawable.widowed_profile_page);
            maritalStatusTextView.setText(toBlackString(getTitle(R.string.marital_status), MaritalStatus.MAPPER.get(user.maritalStatus)));
        }

        attributesTextView.setVisibility(View.VISIBLE);
        attributesTextView.setText(user.heightInCms < 1 ? "" : cmsToInches(user.heightInCms));

        hometownTextView.setText(user.locationWhereIGrewUp == null ? getTitle(R.string.hometown_profile) :
                toBlackString(getTitle(R.string.hometown_profile), user.locationWhereIGrewUp.trim()));

        List<String> lang = user.languages ==  null ? new ArrayList<String>() : user.languages;
        tempString =  lang.size() > 0 ? lang.get(0) :  "English";
        for (int i = 1; i < lang.size(); i ++)
            tempString += ", " + lang.get(i);
        languagesTextView.setText(toBlackString(getTitle(R.string.languages_profile), tempString));

        moveToIsraelTextView.setText(user.wantToGoToIsrael == null ? getTitle(R.string.want_to_move_to_israel) :
                toBlackString(getTitle(R.string.want_to_move_to_israel), WantToGoToIsrael.MAPPER.get(user.wantToGoToIsrael)));

        setYesNoTextView(haveChildrenTextView, user.doYouHaveChildren, getTitle(R.string.have_children));
        if (!user.doYouHaveChildren) {
            howManyQuestionTextView.setVisibility(View.GONE);
        }
        else {
            howManyQuestionTextView.setVisibility(View.VISIBLE);
            howManyQuestionTextView.setText(toBlackString(getTitle(R.string.how_many), Integer.toString(user.numberOfChildren)));
        }

        additionalChildrenTextView.setText(user.moreChildren == null ? getTitle(R.string.additional_children_profile) :
                toBlackString(getTitle(R.string.additional_children_profile), MoreChildren.MAPPER.get(user.moreChildren)));

        politicalOrientationTextView.setText(user.politicalOrientation == null ? getTitle(R.string.political_orientation_profile) :
                toBlackString(getTitle(R.string.political_orientation_profile), PoliticalOrientation.MAPPER.get(user.politicalOrientation)));

        final List<String> temp = new ArrayList<>();
        if (user.nameOfHighSchool != null) {
            if (!user.nameOfHighSchool.trim().isEmpty())
                temp.add(user.nameOfHighSchool);
        }
        if (user.nameOfCollege != null) {
            if (!user.nameOfCollege.trim().isEmpty())
                temp.add(user.nameOfCollege);
        }
        if (temp.size() > 0) {
            educationTextView.setVisibility(View.VISIBLE);
            educationTextView.setText(temp.get(0));
            for (int i = 1; i < temp.size(); i++)
                educationTextView.append(", " + temp.get(i));
        } else {
            educationTextView.setVisibility(View.GONE);
        }

        degreeTextView.setText(user.educationLevel == null ? getTitle(R.string.degree) :
                toBlackString(getTitle(R.string.degree), EducationLevel.MAPPER.get(user.educationLevel)));
        jewishEducationTextView.setText(user.jewishEducation == null ? getTitle(R.string.jewish_education_profile) :
                toBlackString(getTitle(R.string.jewish_education_profile), JewishEducation.MAPPER.get(user.jewishEducation)));
        professionTextView.setText(user.profession == null ? getTitle(R.string.profession_profile) :
                toBlackString(getTitle(R.string.profession_profile), Profession.MAPPER.get(user.profession)));
        descriptionTextView.setText(user.jobDescription == null ? getTitle(R.string.description) :
                toBlackString(getTitle(R.string.description), user.jobDescription.trim()));

        temp.clear();
        if (user.religiousity != null)
            temp.add(Religiousity.MAPPER.get(user.religiousity));
        if (user.culturalBackground != null)
            temp.add(CulturalBackground.MAPPER.get(user.culturalBackground));
        if (user.ethnicity != null)
            temp.add(Ethnicity.MAPPER.get(user.ethnicity));
        if (temp.size() > 0) {
            religiosityTextView.setVisibility(View.VISIBLE);
            religiosityTextView.setText(temp.get(0));
            for (int i = 1; i < temp.size(); i++)
                religiosityTextView.append(", " + temp.get(i));
        } else {
            religiosityTextView.setVisibility(View.GONE);
        }

        if (user.religiousity != null) {
            if (user.religiousity == Religiousity.WILLING_TO_CONVERT) {
                motherJewishContainer.setVisibility(View.GONE);
            } else {
                motherJewishContainer.setVisibility(View.VISIBLE);
                motherJewishTextView.setText(user.grandMotherJewish == null ? getTitle(R.string.jewish_mother_grand_mother) :
                        toBlackString(getTitle(R.string.jewish_mother_grand_mother), GrandmotherJewish.MAPPER.get(user.grandMotherJewish)));
            }
        }

        kosherTextView.setText(user.iKeepKosher == null ? getTitle(R.string.kosher) :
                toBlackString(getTitle(R.string.kosher), IKeepKosher.MAPPER.get(user.iKeepKosher)));

        if (user.religiousity != null) {
            if (EditPerfectMatchActivity.isReallyReligious(user)) {
                baalTeshuvaTextView.setVisibility(View.VISIBLE);
                setYesNoTextView(baalTeshuvaTextView, user.baalTeshuva, getTitle(R.string.baal_teshuva));
            } else {
                baalTeshuvaTextView.setVisibility(View.GONE);
            }
        }

        aboutMeTextView.setText(user.shortDescriptionOfYourself == null ? "" : user.shortDescriptionOfYourself.trim());
    }


    public static Spanned toBlackString(CharSequence string, String append) {
        return Html.fromHtml(string + " " + BLACK_FONT_BEGIN + append + BLACK_FONT_END);
    }

    public static void setYesNoTextView(TextView textView, boolean isYes, String str) {
        if (isYes)
            textView.setText(toBlackString(str, Y));
        else
            textView.setText(toBlackString(str, N));
    }

    private String getTitle(int id) {
        return getResources().getString(id);
    }

    @Override
    public void onClick(DialogFragment dialog, int which) {
        if(matchingStatusChoices == null)
            return;
        User u = UserService.getsUser(this);
        currMatchStatus = (String) matchingStatusChoices[which];
        Server.getJboltService()
                .updateMatch(new UpdateMatch(u.getId(), u.accessToken, intent.getLongExtra("matchId", 0)
                , MatchStatus.MAPPER.get(currMatchStatus)), new PostCallback(this));
    }

    @Override
    public void onClick(View v) {
        Intent goFullscreen = new Intent(this, FullScreenImageActivity.class);
        goFullscreen.putStringArrayListExtra("profile_images", new ArrayList<String>(user.profileImages));
        startActivity(goFullscreen);
    }
}