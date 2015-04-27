package com.syas.jbolt.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.syas.jbolt.R;
import com.syas.jbolt.model.Match;
import com.syas.jbolt.model.user.AppUser;
import com.syas.jbolt.model.user.MatchMaker;
import com.syas.jbolt.server.MatchmakerMatchResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MatchMakerViewPagerAdapter extends FragmentPagerAdapter {

    private static final String[] TABS = {"All", "Members", "Matches"};
    private static final MatchMakerChatMenuListViewFragment[] FRAGMENTS = new MatchMakerChatMenuListViewFragment[TABS.length];

    private MatchmakerMatchResponse response, responseCopy;

    public MatchMakerViewPagerAdapter(FragmentManager fm, MatchmakerMatchResponse response, MatchMaker matchMaker) {
        super(fm);
        this.response = response;
        copyResponse();
        sortAndPrepare();

        for (int i = 0; i < FRAGMENTS.length; i++) {
            MatchMakerChatMenuListViewFragment chatMenuFragment = new MatchMakerChatMenuListViewFragment();
            chatMenuFragment.adapter = new MatchMakerListViewAdapter(responseCopy, i, matchMaker);
            FRAGMENTS[i] = chatMenuFragment;
        }
    }

    private void copyResponse() {
        responseCopy = new MatchmakerMatchResponse();
        responseCopy.matches = new ArrayList<>(response.matches);
        responseCopy.clients = new ArrayList<>(response.clients);
    }

    private void sortAndPrepare(){
        Collections.sort(responseCopy.matches, new MatchOrderer());
        Collections.sort(responseCopy.clients, new ClientOrderer());
    }

    public void setResponse(MatchmakerMatchResponse response) {
        this.response = response;
    }

    public void filter(CharSequence seq){
        copyResponse();
        if(seq.length() > 0) {
            Iterator<Match> matchItr = responseCopy.matches.iterator();
            while (matchItr.hasNext()) {
                Match m = matchItr.next();
                AppUser female = m.female;
                AppUser male = m.male;
                if (female == null || male == null) {
                    matchItr.remove();
                    continue;
                }

                String femaleName = (female.getFirstname()+" "+female.getLastname()).toLowerCase();
                String maleName = (male.getFirstname()+" "+male.getLastname()).toLowerCase();
                String input = seq.toString().toLowerCase();
                if (!femaleName.contains(input) && !maleName.contains(input)) {
                    matchItr.remove();
                }
            }

            Iterator<? extends AppUser> clientItr = responseCopy.clients.iterator();
            while (clientItr.hasNext()) {
                AppUser u = clientItr.next();
                if (u == null) {
                    clientItr.remove();
                    continue;
                }

                String name = (u.getFirstname()+" "+u.getLastname()).toLowerCase();
                if (!name.contains(seq.toString().toLowerCase())) {
                    clientItr.remove();
                }
            }
        }

        sortAndPrepare();
        for (MatchMakerChatMenuListViewFragment fragment : FRAGMENTS) {
            fragment.adapter.changeResponse(responseCopy);
        }
    }

    public void notifyAdapters() {
        for (MatchMakerChatMenuListViewFragment fragment : FRAGMENTS) {
            fragment.adapter.notifyDataSetChanged();
        }
    }

    @Override
    public Fragment getItem(int position) {
        return FRAGMENTS[position];
    }

    @Override
    public int getCount() {
        return TABS.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TABS[position];
    }

    public static class MatchMakerChatMenuListViewFragment extends Fragment {

        @InjectView(R.id.matchmakerListView)
        ListView matchmakerListView;

        MatchMakerListViewAdapter adapter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.fragment_matchmaker_chatmenu_listview, container, false);
            ButterKnife.inject(this, layout);
            matchmakerListView.setAdapter(adapter);

            return layout;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.reset(this);
        }
    }


    private static class MatchOrderer implements Comparator<Match> {
        @Override
        public int compare(Match lhs, Match rhs) {

            if(lhs.timeStamp == rhs.timeStamp)
                return 0;

            return lhs.timeStamp > rhs.timeStamp ? -1 : 1;
        }
    }

    private static class ClientOrderer implements Comparator<AppUser> {
        @Override
        public int compare(AppUser lhs, AppUser rhs) {
           return 0;
           // return lhs.getFirstname().compareTo(rhs.getFirstname());
        }
    }

    public Match getMatch(long matchId) {
        for (Match match : response.matches) {
            if (match.getId() == matchId)
                return match;
        }
        return null;
    }
}