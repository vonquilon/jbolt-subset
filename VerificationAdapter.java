package com.syas.jbolt.model.user;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.syas.jbolt.R;
import com.syas.jbolt.adapters.VerifyReligiousLevelAdapter;
import com.syas.jbolt.listeners.IsJewishClickListener;
import com.syas.jbolt.listeners.IsSingleClickListener;
import com.syas.jbolt.listeners.ReligiousLevelClickListener;
import com.syas.jbolt.model.enums.Optional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class VerificationAdapter extends BaseAdapter implements Comparator<FacebookFriend> {

    private List<FacebookFriend> friendList;
    private List<FacebookFriend> friendListCopy;
    private User u;

    public VerificationAdapter(List<FacebookFriend> friendList, User u) {
        this.friendList = friendList;
        this.friendListCopy = new ArrayList<>(friendList);
        this.u = u;
        sortFriendlist();
    }

    public void sortFriendlist() {
        Collections.sort(friendListCopy, this);
    }

    @Override
    public int getCount() {
        return friendListCopy.size();
    }

    @Override
    public Object getItem(int position) {
        return friendListCopy.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VerifyViewHolder holder;
        Context context = parent.getContext();

        if (convertView == null) {
            convertView = LayoutInflater
                    .from(context)
                    .inflate(R.layout.verification_row_layout, parent, false);
            holder = new VerifyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (VerifyViewHolder) convertView.getTag();
        }

        FacebookFriend friend = friendListCopy.get(position);
        friend.picture = "https://graph.facebook.com/v1.0/" + friend.facebookId + "/picture?access_token=" + u.getAccessToken();
        Picasso.with(context)
                .load(friend.picture)
                .into(holder.profile);

        holder.name.setText(friend.firstName + "\n" + friend.lastName);

        initImageView(holder.isJewish, friend.isJewish);
        holder.isJewish.setOnClickListener(new IsJewishClickListener(friend));

        initImageView(holder.isSingle, friend.isSingle);
        holder.isSingle.setOnClickListener(new IsSingleClickListener(friend));

        holder.observanceSpinner.setAdapter(new VerifyReligiousLevelAdapter(friend));
        holder.observanceSpinner.setOnItemSelectedListener(new ReligiousLevelClickListener(friend));
        initSpinnerView(holder.observanceSpinner,friend.religiousityLevel);

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        convertView.getLayoutParams().height = size.x / 5;
        return convertView;
    }

    private void initImageView(ImageView imageView, Optional attr) {
        switch (attr) {
            case YES:
                imageView.setImageResource(R.drawable.yes);
                break;
            case NO:
                imageView.setImageResource(R.drawable.no);
                break;
            case MAYBE:
                imageView.setImageResource(R.drawable.unknown);
                break;
        }
    }

    private void initSpinnerView(Spinner spinner,String religiousity){
        String[] VALUES = {null, "Orthodox", "Yeshivish", "Traditional",
                "Conservative", "Reform", "Just Jewish", "Not Jewish"};

        int position = 0;
        for(int i = 1; i < VALUES.length; i++){
            if(VALUES[i].equals(religiousity)){
                position = i;
                break;
            }
        }
        spinner.setSelection(position);
    }




    public void filter(CharSequence seq){
        friendListCopy = new ArrayList<>(friendList);
        if(seq.length() > 0) {
            Iterator<FacebookFriend> itr = friendListCopy.iterator();
            while (itr.hasNext()) {
                FacebookFriend f = itr.next();
                String name = (f.firstName+" "+f.lastName).toLowerCase();
                if (!name.contains(seq.toString().toLowerCase())) {
                    itr.remove();
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int compare(FacebookFriend lhs, FacebookFriend rhs) {
        return lhs.getPosition() - rhs.getPosition();
    }

    static class VerifyViewHolder {
        @InjectView(R.id.verificationProfileImage)
        ImageView profile;

        @InjectView(R.id.verificationName)
        TextView name;

        @InjectView(R.id.verificationJewish)
        ImageView isJewish;

        @InjectView(R.id.verificationSingle)
        ImageView isSingle;

        @InjectView(R.id.verificationObservance)
        Spinner observanceSpinner;

        @InjectView(R.id.verificationRowContainer)
        LinearLayout container;

        public VerifyViewHolder(View v) {
            ButterKnife.inject(this, v);
            profile.setScaleType(ImageView.ScaleType.FIT_XY);
            isJewish.setScaleType(ImageView.ScaleType.FIT_XY);
            isSingle.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }
}
