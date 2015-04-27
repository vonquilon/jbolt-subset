package com.syas.jbolt.model.user;

import android.app.Activity;
import android.content.Context;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.common.base.Joiner;
import com.squareup.picasso.Picasso;
import com.syas.jbolt.model.enums.EnumMapper;
import com.syas.jbolt.views.DoubleSlider;
import com.syas.jbolt.views.Font;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ActivityHelper {

    static <T> void initMultiChoice(final Activity activity, List<T> init, Field value, Object empty, final T[] array, final EnumMapper<T> MAPPER, final TextView link, ImageButton button) throws IllegalAccessException {
        if(init == null){
            init = new ArrayList<T>();
        }

        value.set(empty,init);

        String vals;

        if (MAPPER != null) {
            List<String> temp = new ArrayList<>();
            for (T val : init) {
                temp.add(MAPPER.get(val));
            }
            vals = Joiner
                    .on(", ")
                    .join(temp);
        } else {
            vals = Joiner
                    .on(", ")
                    .join(init);
        }

        link.setText(vals);

        final List<T> copy = init;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiChoiceDialog
                        .show(activity, copy, array, MAPPER, link);
            }
        });
    }

    static void initEditText(Activity activity, String init, AppUser empty, Field attr, EditText editText){
        editText.setTypeface(Font.getFont(activity, Font.ROBOTO_LIGHT));
        TextWatcher watcher = new EditProfileTextListener(attr,empty);
        if(init != null)
            editText.setText(init);
        editText.addTextChangedListener(watcher);
    }

    static void initSpinner(Field field, boolean init, Object empty, Spinner spinner, LinearLayout howManyContainer){
        YesNoAdapter adapter = new YesNoAdapter(field,empty,howManyContainer);
        spinner.setAdapter(adapter);
        spinner.setSelection(init ? 0 : 1);
        if (spinner.getSelectedItemPosition() == 1 && howManyContainer != null)
            howManyContainer.setVisibility(View.INVISIBLE);
        spinner.setOnItemSelectedListener(adapter);
    }

    public static <T> void initSpinner(final Spinner spinner, T[] values, T value, EnumMapper<T> mapper, Field attr, Object empty) {
        try {
            final EditEnumAdapter<T> adapter = new EditEnumAdapter<>(values, mapper, attr, empty);
            spinner.setAdapter(adapter);
            if (value != null) {
                attr.set(empty, value);
                for (int i = 0; i < values.length; i++) {
                    if (value.equals(values[i])) {
                        spinner.setSelection(i);
                    }
                }
            } else {
                spinner.setSelection(0);
            }

            adapter.doReflection(spinner);


            spinner.postDelayed(new Runnable() {
                @Override
                public void run() {
                    spinner.setOnItemSelectedListener(adapter);
                }
            }, 100);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    static void initAgeSlider(DoubleSlider slider, final TextView link, final AppUser match) {

        slider.setPositions(match.minAge,match.maxAge);
        link.setText(match.minAge + " - " + match.maxAge);

        slider.setSlideListener(new DoubleSlider.OnSlideListener() {
            @Override
            public void onSlide(DoubleSlider doubleSlider, int leftPosition, int rightPosition) {
                link.setText(leftPosition + " - " + rightPosition);
            }

            @Override
            public void onSlideFinished(DoubleSlider doubleSlider, int leftPosition, int rightPosition) {
                match.minAge = leftPosition;
                match.maxAge = rightPosition;
            }
        });
    }

    static void initSlider(DoubleSlider slider, final TextView link, final PerfectUser match) {
        final int offset = 115;

        if(match.minHeightInCms != 0 && match.maxHeightInCms != 0)
            slider.setPositions(match.minHeightInCms - offset, match.maxHeightInCms - offset);

        slider.setSlideListener(new DoubleSlider.OnSlideListener() {
            @Override
            public void onSlide(DoubleSlider doubleSlider, int leftPosition, int rightPosition) {
                link.setText(Utils.cmsToInches(leftPosition + offset) + " - " + Utils.cmsToInches(rightPosition + offset));
            }

            @Override
            public void onSlideFinished(DoubleSlider doubleSlider, int leftPosition, int rightPosition) {
                match.minHeightInCms = leftPosition + offset;
                match.maxHeightInCms = rightPosition + offset;
            }
        });

        link.setText(Utils.cmsToInches(match.minHeightInCms) + " - " + Utils.cmsToInches(match.maxHeightInCms));
    }

    static void initSwitch(Switch switchButton, AppUser empty, String fieldName, boolean isChecked) {
        SwitchListener listener = new SwitchListener(empty, fieldName, isChecked);
        switchButton.setChecked(isChecked);
        switchButton.setOnCheckedChangeListener(listener);
    }

    public static void initProfileImage(ImageView imageView, AppUser user, Activity activity) {
        String imageUrl = user.isMale() ?
                "http://commondatastorage.googleapis.com/jbolt-photos/PlaceholderMale.png" :
                "http://commondatastorage.googleapis.com/jbolt-photos/PlaceholderFemale.png";

        if(user.profileImages != null && user.profileImages.size() > 0){
            imageUrl = user.profileImages.get(0);
        }

       initProfileImage(imageView, imageUrl, activity);
    }

    public static void initProfileImage(ImageView imageView, String imageUrl, Context context) {
        Picasso
                .with(context)
                .load(imageUrl)
                .into(imageView);
    }
}
