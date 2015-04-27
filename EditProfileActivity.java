package com.syas.jbolt.model.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.syas.jbolt.BaseEditActivity;
import com.syas.jbolt.EditPhotosActivity;
import com.syas.jbolt.R;
import com.syas.jbolt.model.enums.AmICohen;
import com.syas.jbolt.model.enums.CulturalBackground;
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
import com.syas.jbolt.model.enums.MoreChildren;
import com.syas.jbolt.model.enums.PoliticalOrientation;
import com.syas.jbolt.model.enums.Profession;
import com.syas.jbolt.model.enums.Religiousity;
import com.syas.jbolt.model.enums.SmokingHabits;
import com.syas.jbolt.model.enums.UserType;
import com.syas.jbolt.model.enums.WantToGoToIsrael;
import com.syas.jbolt.model.gson.EditProfile;

import java.lang.annotation.Annotation;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.syas.jbolt.model.user.ActivityHelper.initEditText;
import static com.syas.jbolt.model.user.ActivityHelper.initMultiChoice;
import static com.syas.jbolt.model.user.ActivityHelper.initProfileImage;
import static com.syas.jbolt.model.user.ActivityHelper.initSpinner;

public class EditProfileActivity extends BaseEditActivity {

    @InjectView(R.id.editButton)
    Button profileEditButton;

    @InjectView(R.id.addLanguagesButton)
    ImageButton addLanguagesButton;

    @InjectView(R.id.stateSpinner)
    Spinner stateSpinner;
    @InjectView(R.id.heightSpinner)
    Spinner heightSpinner;
    @InjectView(R.id.bodyTypeSpinner)
    Spinner bodyTypeSpinner;
    @InjectView(R.id.smokingHabitsSpinner)
    Spinner smokingHabitsSpinner;
    @InjectView(R.id.israelSpinner)
    Spinner israelSpinner;
    @InjectView(R.id.haveChildrenSpinner)
    Spinner haveChildrenSpinner;
    @InjectView(R.id.howManySpinner)
    Spinner howManySpinner;
    @InjectView(R.id.additionalChildrenSpinner)
    Spinner additionalChildrenSpinner;
    @InjectView(R.id.politicalSpinner)
    Spinner politicalSpinner;
    @InjectView(R.id.educationLevelSpinner)
    Spinner educationLevelSpinner;
    @InjectView(R.id.jewishEducationSpinner)
    Spinner jewishEducationSpinner;
    @InjectView(R.id.professionSpinner)
    Spinner professionSpinner;
    @InjectView(R.id.religionSpinner)
    Spinner religionSpinner;
    @InjectView(R.id.cultureSpinner)
    Spinner cultureSpinner;
    @InjectView(R.id.ethnicitySpinner)
    Spinner ethnicitySpinner;

    @InjectView(R.id.motherJewishSpinner)
    Spinner motherJewishSpinner;
    @InjectView(R.id.motherGrandMotherTextView)
    TextView motherGrandMotherTextView;

    @InjectView(R.id.convertSpinner)
    Spinner convertSpinner;
    @InjectView(R.id.amIConvertTextView)
    TextView amIConvertTextView;

    @InjectView(R.id.amICohenSpinner)
    Spinner amICohenSpinner;
    @InjectView(R.id.amICohenTextView)
    TextView amICohenTextView;

    @InjectView(R.id.marryCohenSpinner)
    Spinner marryCohenSpinner;
    @InjectView(R.id.marryCohenTextView)
    TextView marryCohenTextView;

    @InjectView(R.id.baalTeshuvaSpinner)
    Spinner baalTeshuvaSpinner;
    @InjectView(R.id.amIBaalTeshuvaTextView)
    TextView amIBaalTeshuvaTextView;

    @InjectView(R.id.kosherSpinner)
    Spinner kosherSpinner;

    @InjectView(R.id.hairCoverSpinner)
    Spinner hairCoverSpinner;
    @InjectView(R.id.hairCoverTextView)
    TextView hairCoverTextView;

    @InjectView(R.id.dressSpinner)
    Spinner dressSpinner;
    @InjectView(R.id.dressTextView)
    TextView dressTextView;

    @InjectView(R.id.torahSpinner)
    Spinner torahSpinner;
    @InjectView(R.id.torahTextView)
    TextView torahTextView;

    @InjectView(R.id.cellPhoneEditText)
    EditText cellPhoneEditText;
    @InjectView(R.id.cityEditText)
    EditText cityEditText;
    @InjectView(R.id.zipEditText)
    EditText zipEditText;

    @InjectView(R.id.hometownEditText)
    EditText hometownEditText;
    @InjectView(R.id.highSchoolEditText)
    EditText highschoolEditText;
    @InjectView(R.id.collegeEditText)
    EditText collegeEditText;
    @InjectView(R.id.jobDescriptionEditText)
    EditText jobDescriptionEditText;
    @InjectView(R.id.aboutEditText)
    EditText aboutEditText;
    @InjectView(R.id.languagesHolderTextView)
    TextView languagesHolderTextView;

    @InjectView((R.id.profileImage))
    ImageView profileImage;

    @InjectView((R.id.topTextView))
    TextView header;

    @InjectView(R.id.howManyContainer)
    LinearLayout howManyContainer;

    private AppUser appUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.inject(this);

        setActionBarTitle(R.string.my_profile);

        appUser = (AppUser)UserService.getsUser(this);
        if(appUser.hasCompletedProfile()){
            header.setVisibility(View.GONE);
        }

        try {

            if(appUser instanceof MaleAppUser){
                mDiff = new MaleAppUser();
                setUpViews((MaleAppUser)appUser,(MaleAppUser) mDiff);
            }else{
                mDiff = new FemaleAppUser();
                setUpViews((FemaleAppUser)appUser,(FemaleAppUser) mDiff);
            }
            setUpViews((AppUser) appUser, (AppUser) mDiff);

        }catch(Exception e){
            throw new RuntimeException(e);
        }

        if (appUser.religiousity != null) {
            if (appUser.religiousity == Religiousity.WILLING_TO_CONVERT) {
                showWillingToConvertDefaults();
            } else if (EditPerfectMatchActivity.isReallyReligious(appUser)) {
                showReallyReligiousDefaults();
            } else {
                showJustReligiousDefaults();
            }
        }
    }

    private void setUpViews(MaleAppUser user, MaleAppUser empty) throws Exception{
        initSpinner(bodyTypeSpinner, MaleBodyType.values(), user.bodyType, MaleBodyType.MAPPER,user.getClass().getDeclaredField("bodyType"), empty);
        initSpinner(amICohenSpinner, AmICohen.values(), user.amICohen, AmICohen.MAPPER, user.getClass().getDeclaredField("amICohen"), empty);

        torahTextView.setVisibility(View.VISIBLE);
        torahSpinner.setVisibility(View.VISIBLE);
        initSpinner(torahSpinner, FrequencyOfTorahStudy.values(), user.frequencyOfTorahStudy, FrequencyOfTorahStudy.MAPPER, user.getClass().getDeclaredField("frequencyOfTorahStudy"), empty);

        marryCohenTextView.setVisibility(View.GONE);
        marryCohenSpinner.setVisibility(View.GONE);
        hairCoverTextView.setVisibility(View.GONE);
        hairCoverSpinner.setVisibility(View.GONE);
        dressTextView.setVisibility(View.GONE);
        dressSpinner.setVisibility(View.GONE);
    }

    private void setUpViews(FemaleAppUser user, FemaleAppUser empty) throws Exception{
        initSpinner(bodyTypeSpinner, FemaleBodyType.values(), user.bodyType, FemaleBodyType.MAPPER, user.getClass().getDeclaredField("bodyType"), empty);

        hairCoverTextView.setVisibility(View.VISIBLE);
        hairCoverSpinner.setVisibility(View.VISIBLE);
        initSpinner(hairCoverSpinner, HairCovering.values(), user.hairCovering, HairCovering.MAPPER, user.getClass().getDeclaredField("hairCovering"), empty);

        dressTextView.setVisibility(View.VISIBLE);
        dressSpinner.setVisibility(View.VISIBLE);
        initSpinner(dressSpinner, FemaleDress.values(), user.femaleDress, FemaleDress.MAPPER, user.getClass().getDeclaredField("femaleDress"), empty);

        initSpinner(user.getClass().getDeclaredField("canIMarryCohen"), user.canIMarryCohen, empty, marryCohenSpinner, null);

        torahTextView.setVisibility(View.GONE);
        torahSpinner.setVisibility(View.GONE);

    }

    private void setUpViews(AppUser user, AppUser empty) throws Exception{
        initProfileImage(profileImage, user, this);

        Class<?> superClazz = empty.getClass().getSuperclass();

        initSpinner(smokingHabitsSpinner, SmokingHabits.values(), user.smokingHabits, SmokingHabits.MAPPER, superClazz.getDeclaredField("smokingHabits"), empty);
        initSpinner(israelSpinner, WantToGoToIsrael.values(), user.wantToGoToIsrael, WantToGoToIsrael.MAPPER, superClazz.getDeclaredField("wantToGoToIsrael"), empty);
        initSpinner(additionalChildrenSpinner, MoreChildren.values(), user.moreChildren, MoreChildren.MAPPER, superClazz.getDeclaredField("moreChildren"), empty);
        initSpinner(politicalSpinner, PoliticalOrientation.values(), user.politicalOrientation, PoliticalOrientation.MAPPER, superClazz.getDeclaredField("politicalOrientation"), empty);
        initSpinner(educationLevelSpinner, EducationLevel.values(), user.educationLevel, EducationLevel.MAPPER, superClazz.getDeclaredField("educationLevel"), empty);
        initSpinner(jewishEducationSpinner, JewishEducation.values(), user.jewishEducation, JewishEducation.MAPPER, superClazz.getDeclaredField("jewishEducation"), empty);
        initSpinner(professionSpinner, Profession.values(), user.profession, Profession.MAPPER, superClazz.getDeclaredField("profession"), empty);

        initReligiositySpinner(empty);

        initSpinner(cultureSpinner, CulturalBackground.values(), user.culturalBackground, CulturalBackground.MAPPER,superClazz.getDeclaredField("culturalBackground"), empty);
        initSpinner(ethnicitySpinner, Ethnicity.values(), user.ethnicity, Ethnicity.MAPPER, superClazz.getDeclaredField("ethnicity"), empty);
        initSpinner(motherJewishSpinner, GrandmotherJewish.values(), user.grandMotherJewish, GrandmotherJewish.MAPPER, superClazz.getDeclaredField("grandMotherJewish"), empty);

        if (user.iKeepKosher == null) {

        }
        initSpinner(kosherSpinner, IKeepKosher.values(), user.iKeepKosher, IKeepKosher.MAPPER, superClazz.getDeclaredField("iKeepKosher"),empty);

        initSpinner(superClazz.getDeclaredField("areYouAConvert"), user.areYouAConvert, empty, convertSpinner, null);
        initSpinner(superClazz.getDeclaredField("doYouHaveChildren"),user.doYouHaveChildren,empty,haveChildrenSpinner, howManyContainer);
        initSpinner(superClazz.getDeclaredField("baalTeshuva"),user.baalTeshuva,empty,baalTeshuvaSpinner, null);

        initEditText(this,user.cell,empty,superClazz.getDeclaredField("cell"),cellPhoneEditText);
        initEditText(this,user.city,empty,superClazz.getDeclaredField("city"),cityEditText);
        initEditText(this,user.zip,empty,superClazz.getDeclaredField("zip"),zipEditText);
        initEditText(this,user.locationWhereIGrewUp,empty,superClazz.getDeclaredField("locationWhereIGrewUp"),hometownEditText);
        initEditText(this,user.nameOfHighSchool,empty,superClazz.getDeclaredField("nameOfHighSchool"),highschoolEditText);
        initEditText(this,user.nameOfCollege,empty,superClazz.getDeclaredField("nameOfCollege"),collegeEditText);
        initEditText(this,user.jobDescription,empty,superClazz.getDeclaredField("jobDescription"),jobDescriptionEditText);
        initEditText(this,user.shortDescriptionOfYourself,empty,superClazz.getDeclaredField("shortDescriptionOfYourself"),aboutEditText);

        String[] languages = getResources().getStringArray(R.array.languages);
        initMultiChoice(this, user.languages, superClazz.getDeclaredField("languages"), empty, languages, null, languagesHolderTextView, addLanguagesButton);

        String[] states = getResources().getStringArray(R.array.states);
        initSpinner(stateSpinner, states, user.state, null, superClazz.getDeclaredField("state"),empty);

        initHowManySpinner(empty);
        initHeightSpinner(empty);
    }

    private void showWillingToConvertDefaults() {
        hideReallyReligiousDefaults();

        convertSpinner.setVisibility(View.GONE);
        amIConvertTextView.setVisibility(View.GONE);
        motherJewishSpinner.setVisibility(View.GONE);
        motherGrandMotherTextView.setVisibility(View.GONE);

        if (appUser.gender.equals("male")) {
            amICohenSpinner.setVisibility(View.GONE);
            amICohenTextView.setVisibility(View.GONE);
        }

        kosherSpinner.setSelection(4); // Almost never/Not at all
    }

    private void hideWillingToConvertDefaults() {
        convertSpinner.setVisibility(View.VISIBLE);
        amIConvertTextView.setVisibility(View.VISIBLE);
        motherJewishSpinner.setVisibility(View.VISIBLE);
        motherGrandMotherTextView.setVisibility(View.VISIBLE);

        if (appUser.gender.equals("male")) {
            amICohenSpinner.setVisibility(View.VISIBLE);
            amICohenTextView.setVisibility(View.VISIBLE);
        }
    }

    private void showJustReligiousDefaults() {
        hideWillingToConvertDefaults();
        hideReallyReligiousDefaults();
    }

    private void showReallyReligiousDefaults() {
        showJustReligiousDefaults();

        if (appUser.gender.equals("female")) {
            marryCohenTextView.setVisibility(View.VISIBLE);
            marryCohenSpinner.setVisibility(View.VISIBLE);
            hairCoverTextView.setVisibility(View.VISIBLE);
            hairCoverSpinner.setVisibility(View.VISIBLE);
            dressTextView.setVisibility(View.VISIBLE);
            dressSpinner.setVisibility(View.VISIBLE);

            if (religionSpinner.getSelectedItemPosition() != 2) { // Orthodox
                hairCoverSpinner.setSelection(1); // Fully
                dressSpinner.setSelection(0); // Skirts only
            }
        } else {
            torahTextView.setVisibility(View.VISIBLE);
            torahSpinner.setVisibility(View.VISIBLE);
        }

        baalTeshuvaSpinner.setVisibility(View.VISIBLE);
        amIBaalTeshuvaTextView.setVisibility(View.VISIBLE);

        kosherSpinner.setSelection(0); // Always
    }

    private void hideReallyReligiousDefaults() {
        if (appUser.gender.equals("female")) {
            marryCohenTextView.setVisibility(View.GONE);
            marryCohenSpinner.setVisibility(View.GONE);
            hairCoverTextView.setVisibility(View.GONE);
            hairCoverSpinner.setVisibility(View.GONE);
            dressTextView.setVisibility(View.GONE);
            dressSpinner.setVisibility(View.GONE);
        } else {
            torahTextView.setVisibility(View.GONE);
            torahSpinner.setVisibility(View.GONE);
        }

        baalTeshuvaSpinner.setVisibility(View.GONE);
        amIBaalTeshuvaTextView.setVisibility(View.GONE);
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.e(null, new GsonBuilder().setPrettyPrinting().create().toJson(mDiff));
    }

    @Override
    public Class<? extends Annotation> getExlude() {
        return EditProfile.class;
    }


    private static final int EDIT_PHOTOS = 12312;
    @OnClick(R.id.editButton)
    public void editPhotos(Button b){
        Intent intent = new Intent(this, EditPhotosActivity.class);
        startActivityForResult(intent, EDIT_PHOTOS);
    }

    @Override
    public void onActivityResult(int request, int result, final Intent data) {
        super.onActivityResult(request, result, data);
        String url = data.getStringExtra("image");
        mDiff.profileImages = ((AppUser)UserService.getsUser(this)).profileImages;
        Picasso
                .with(this)
                .load(url)
                .into(profileImage);
    }

    @Override
    public void onBackPressed(){
        AppUser u = (AppUser) UserService.getsUser(this);
        mDiff.gender = u.gender;
        mDiff.type = UserType.APP_USER;

        String json = new GsonBuilder()
                .registerTypeAdapter(User.class,new UserDeserializer())
                .create()
                .toJson(mDiff);



        Intent intent = new Intent();
        intent.putExtra(Utils.JSON,json);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void initHowManySpinner(AppUser empty) {
        String[] vals = getResources().getStringArray(R.array.numOfChildren);

        HowManySpinnerAdapter adapter = new HowManySpinnerAdapter(vals, empty);
        howManySpinner.setAdapter(adapter);
        howManySpinner.setOnItemSelectedListener(adapter);

        if (appUser.numberOfChildren != 0) {
            String numOfChildren = Integer.toString(appUser.numberOfChildren);
            for (int i = 0, n = vals.length; i < n; i++) {
                if (numOfChildren.equals(vals[i])) {
                    howManySpinner.setSelection(i);
                    break;
                }
            }
        }
    }

    private void initHeightSpinner(AppUser empty) {
        String[] heights = getResources().getStringArray(R.array.heights);

        HeightSpinnerAdapter adapter = new HeightSpinnerAdapter(heights, empty);
        heightSpinner.setAdapter(adapter);
        heightSpinner.setOnItemSelectedListener(adapter);

        if (appUser.heightInCms != 0) {
            String heightInInches = Utils.cmsToInches(appUser.heightInCms);
            for (int i = 0, n = heights.length; i < n; i++) {
                if (heightInInches.equals(heights[i])) {
                    heightSpinner.setSelection(i);
                    break;
                }
            }
        }
    }

    private void initReligiositySpinner(AppUser empty) {
        Religiousity[] religiousities = Religiousity.values();

        ReligiositySpinnerAdapter adapter = new ReligiositySpinnerAdapter(religiousities, empty);
        religionSpinner.setAdapter(adapter);
        religionSpinner.setOnItemSelectedListener(adapter);

        if (appUser.religiousity != null) {
            for (int i = 0, n = religiousities.length; i < n; i++) {
                if (appUser.religiousity == religiousities[i]) {
                    religionSpinner.setSelection(i);
                    break;
                }
            }
        }
    }

    private class ReligiositySpinnerAdapter extends BaseEditApapter implements AdapterView.OnItemSelectedListener{

        Religiousity[] religiousities;
        AppUser empty;

        ReligiositySpinnerAdapter(Religiousity[] religiousities, AppUser empty) {
            this.religiousities = religiousities;
            this.empty = empty;
        }

        @Override
        public int getCount() {
            return religiousities.length;
        }

        @Override
        public Religiousity getItem(int position) {
            return religiousities[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView label;
            if(convertView == null){
                convertView = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.spinner_item,parent,false);
                label = (TextView)convertView.findViewById(R.id.label);
                convertView.setTag(new Holder(label));
            }else{
                Holder h = (Holder)convertView.getTag();
                label = h.label;
            }
            label.setText(Religiousity.MAPPER.get(religiousities[position]));

            return convertView;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            boolean shouldNotify = appUser.religiousity == null;

            empty.religiousity = religiousities[position];
            if (empty.religiousity == Religiousity.WILLING_TO_CONVERT) {
                showWillingToConvertDefaults();
            } else if (EditPerfectMatchActivity.isReallyReligious(empty)) {
                showReallyReligiousDefaults();
            } else {
                showJustReligiousDefaults();
            }

            if(shouldNotify)
                notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }


        private class Holder{
            final TextView label;
            public Holder(TextView l){
                label = l;
            }
        }
    }

    private class HeightSpinnerAdapter extends BaseEditApapter implements AdapterView.OnItemSelectedListener{

        String[] heights;
        AppUser empty;

        HeightSpinnerAdapter(String[] heights, AppUser empty) {
            this.heights = heights;
            this.empty = empty;
        }

        @Override
        public int getCount() {
            return heights.length;
        }

        @Override
        public String getItem(int position) {
            return heights[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView label;
            if(convertView == null){
                convertView = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.spinner_item,parent,false);
                label = (TextView)convertView.findViewById(R.id.label);
                convertView.setTag(new Holder(label));
            }else{
                Holder h = (Holder)convertView.getTag();
                label = h.label;
            }
            label.setText(heights[position]);

            return convertView;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            boolean shouldNotify = appUser.heightInCms == 0;

            String[] feetAndInches = ((String)heights[position]).split("\'|\"");
            int feet = Integer.parseInt(feetAndInches[0]);
            int inches = Integer.parseInt(feetAndInches[1]);
            inches += feet * 12;
            empty.heightInCms = Utils.inchesToCms(inches);

            if(shouldNotify)
                notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }


        private class Holder{
            final TextView label;
            public Holder(TextView l){
                label = l;
            }
        }
    }

    private class HowManySpinnerAdapter extends BaseEditApapter implements AdapterView.OnItemSelectedListener{

        String[] vals;
        AppUser empty;

        HowManySpinnerAdapter(String[] vals, AppUser empty) {
            this.vals = vals;
            this.empty = empty;
        }

        @Override
        public int getCount() {
            return vals.length;
        }

        @Override
        public String getItem(int position) {
            return vals[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView label;
            if(convertView == null){
                convertView = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.spinner_item,parent,false);
                label = (TextView)convertView.findViewById(R.id.label);
                convertView.setTag(new Holder(label));
            }else{
                Holder h = (Holder)convertView.getTag();
                label = h.label;
            }
            label.setText(vals[position]);

            return convertView;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            boolean shouldNotify = appUser.numberOfChildren == 0;

            empty.numberOfChildren = Integer.parseInt(vals[position]);

            if(shouldNotify)
                notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }


        private class Holder{
            final TextView label;
            public Holder(TextView l){
                label = l;
            }
        }
    }
}
