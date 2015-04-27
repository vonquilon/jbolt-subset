package com.syas.jbolt.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.syas.jbolt.R;
import com.syas.jbolt.chat.ChatActivity;
import com.syas.jbolt.model.Match;
import com.syas.jbolt.model.enums.MatchStatus;
import com.syas.jbolt.model.enums.UserType;
import com.syas.jbolt.model.user.AppUser;
import com.syas.jbolt.model.user.FemaleAppUser;
import com.syas.jbolt.model.user.MaleAppUser;
import com.syas.jbolt.model.user.MatchMaker;
import com.syas.jbolt.model.user.ProfilePageActivity;
import com.syas.jbolt.model.user.Utils;
import com.syas.jbolt.server.MatchmakerMatchResponse;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MatchMakerListViewAdapter extends BaseAdapter {

    private MatchmakerMatchResponse response;
    private final MemberAdapter memberAdapter;
    private final MatchesAdapter matchesAdapter;
    private final MatchMaker matchMaker;

    private final int currTabPos;

    public MatchMakerListViewAdapter(MatchmakerMatchResponse response, int currTabPos, MatchMaker matchMaker) {
        this.response = response;
        this.currTabPos = currTabPos;
        this.matchMaker = matchMaker;

        memberAdapter = new MemberAdapter();
        matchesAdapter = new MatchesAdapter();
    }

    public void changeResponse(MatchmakerMatchResponse newResponse) {
        response = newResponse;
        notifyDataSetChanged();
    }

    private String getProfileUrl(AppUser u){
        if(u.profileImages != null && u.profileImages.size() > 0){
            return u.profileImages.get(0);
        }

        return "";
    }

    @Override
    public int getCount() {
        switch (currTabPos) {
            case 0:
                return response.clients.size() + response.matches.size();
            case 1:
                return memberAdapter.getCount();
            case 2:
                return matchesAdapter.getCount();
            default:
                return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        switch (currTabPos) {
            case 0: {
                if (position >= response.matches.size()) {
                    return response
                            .clients
                            .get(position - response.matches.size());
                } else {
                    return response
                            .matches
                            .get(position);
                }
            }
            case 1: {
                return memberAdapter.getItem(position);
            }
            case 2: {
                return matchesAdapter.getItem(position);
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (currTabPos) {
            case 0: {
                if (position >= response.matches.size()) {
                    return memberAdapter.getView(position - response.matches.size(), convertView, parent);
                } else {
                    return matchesAdapter.getView(position, convertView, parent);
                }
            }
            case 1: {
                return memberAdapter.getView(position, convertView, parent);
            }
            case 2: {
                return matchesAdapter.getView(position, convertView, parent);
            }
            default: {
                return null;
            }
        }
    }

    private class MemberAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return response.clients.size();
        }

        @Override
        public Object getItem(int position) {
            return response.clients.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyMatchesAdapter.MatchViewHolder holder;
            Context context = parent.getContext();

            if (convertView == null || convertView.getTag() instanceof MatchMakerMatchViewHolder) {
                convertView = LayoutInflater
                        .from(context)
                        .inflate(R.layout.chat_menu_match_item, parent, false);
                holder = new MyMatchesAdapter.MatchViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (MyMatchesAdapter.MatchViewHolder) convertView.getTag();
            }

            AppUser user = response.clients.get(position);
            loadImage(context, user, holder.profileImage);
            holder.name.setText(user.getFirstname() + " " + user.getLastname());
            holder.matchStatus.setVisibility(View.GONE);
            convertView.setOnClickListener(new OnClickChatListener(
                    user, null, context
            ));

            return convertView;
        }
    }

    private void loadImage(Context context, AppUser user, ImageView imageView) {
        String url;
        if (!(url = getProfileUrl(user)).isEmpty()) {
            Picasso.with(context)
                    .load(url)
                    .into(imageView);
        }
    }

    private class MatchesAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return response.matches.size();
        }

        @Override
        public Object getItem(int position) {
            return response.matches.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MatchMakerMatchViewHolder holder;
            Context context = parent.getContext();

            if (convertView == null || convertView.getTag() instanceof MyMatchesAdapter.MatchViewHolder) {
                convertView = LayoutInflater
                        .from(context)
                        .inflate(R.layout.chat_menu_matches_item, parent, false);
                holder = new MatchMakerMatchViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (MatchMakerMatchViewHolder) convertView.getTag();
            }

            Match match = response.matches.get(position);

            AppUser u1 = match.female;
            loadImage(context, u1, holder.matchesProfileImage1);
            holder.matchesName1.setText(u1.getFirstname()+" "+u1.getLastname());
            holder.matchStatus1.setText(MatchStatus.MAPPER.get(match.matchStatus));

            AppUser u2 = match.male;
            loadImage(context, u2, holder.matchesprofileImage2);
            holder.matchesName2.setText(u2.getFirstname()+" "+u2.getLastname());
            holder.matchStatus2.setText(MatchStatus.MAPPER.get(match.matchStatus));

            if (match.matchStatus == MatchStatus.INITIAL) {
                holder.matches_user1.setOnClickListener(new OnClickProfileListener(
                        u1, match, context
                ));
                holder.matches_user2.setOnClickListener(new OnClickProfileListener(
                        u2, match, context
                ));
            } else {
                holder.matches_user1.setOnClickListener(new OnClickChatListener(
                        u1, match, context
                ));
                holder.matches_user2.setOnClickListener(new OnClickChatListener(
                        u2, match, context
                ));
            }

            if (match.femaleHasMessage) {
                holder.notification1.setImageResource(R.drawable.notification);
            } else {
                holder.notification1.setImageResource(R.drawable.notification_idle);
            }

            if (match.maleHasMessage) {
                holder.notification2.setImageResource(R.drawable.notification);
            } else {
                holder.notification2.setImageResource(R.drawable.notification_idle);
            }

            return convertView;
        }
    }

    private class OnClickChatListener implements View.OnClickListener {

        AppUser appUser;
        final Match match;
        final Context context;

        OnClickChatListener(AppUser appUser, Match match, Context context) {
            this.appUser = appUser;
            this.match = match;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Log.e(null, new GsonBuilder().setPrettyPrinting().create().toJson(appUser));
            Intent goToChat = new Intent(context, ChatActivity.class);
            goToChat.putExtra(ChatActivity.APP_USER_ID_KEY,
                    new Gson().toJson(appUser, "male".equals(appUser.getGender()) ? MaleAppUser.class : FemaleAppUser.class));
            goToChat.putExtra(ChatActivity.MATCH_MAKER_ID_KEY,
                    new Gson().toJson(matchMaker, MatchMaker.class));
            if (match != null) {
                goToChat.putExtra(ChatActivity.MATCH_OBJECT_ID_KEY,
                        new Gson().toJson(match, Match.class));
                goToChat.putExtra(ChatActivity.MATCH_ID_KEY, match.getId());
            }
            goToChat.putExtra(ChatActivity.MY_ID_KEY, matchMaker.getId());
            context.startActivity(goToChat);
        }
    }

    private class OnClickProfileListener extends OnClickChatListener{

        OnClickProfileListener(AppUser appUser, Match match, Context context) {
            super(appUser, match, context);
        }

        @Override
        public void onClick(View v) {
            Intent goToProfile = new Intent(context, ProfilePageActivity.class);
            goToProfile.putExtra(Utils.JSON,
                    new Gson().toJson(appUser, "male".equals(appUser.getGender()) ? MaleAppUser.class : FemaleAppUser.class));
            goToProfile.putExtra("matchStatus",
                    MatchStatus.MAPPER.get(match.matchStatus));
            goToProfile.putExtra("matchId", match.getId());
            goToProfile.putExtra(ProfilePageActivity.VIEWER_STATUS,
                    ProfilePageActivity.MATCHER);
            context.startActivity(goToProfile);
        }
    }

    static class MatchMakerMatchViewHolder{
        @InjectView(R.id.matches_user1)
        RelativeLayout matches_user1;

        @InjectView(R.id.matchesProfileImage1)
        ImageView matchesProfileImage1;

        @InjectView(R.id.matches_has_mesg1)
        ImageView notification1;

        @InjectView(R.id.matchesName1)
        TextView matchesName1;

        @InjectView(R.id.matches_match_status1)
        TextView matchStatus1;

        @InjectView(R.id.matches_user2)
        RelativeLayout matches_user2;

        @InjectView(R.id.matchesprofileImage2)
        ImageView matchesprofileImage2;

        @InjectView(R.id.matches_has_mesg2)
        ImageView notification2;

        @InjectView(R.id.matchesName2)
        TextView matchesName2;

        @InjectView(R.id.matches_match_status2)
        TextView matchStatus2;

        public MatchMakerMatchViewHolder(View v){
            ButterKnife.inject(this, v);
        }
    }
}

