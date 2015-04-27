package com.syas.jbolt.model.user;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.syas.jbolt.BaseActivity;
import com.syas.jbolt.R;

public class VerificationActivity extends BaseActivity {

    private VerificationFragment verificationFragment;
    private LinearLayout menuButtonsContainer;

    @Override
    protected void onCreate(Bundle save) {
        super.onCreate(save);
        setContentView(R.layout.activity_verification);

        setActionBarTitle(R.string.friend_verification);

        verificationFragment = new VerificationFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.verify_fragment_container, verificationFragment)
                .commit();

        menuButtonsContainer = (LinearLayout) getActionBar()
                .getCustomView()
                .findViewById(R.id.buttonsContainer);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    /*
    *
    *           Search Functionality
    *
     */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.verify_activity, menu);
        ImageView menuRefresh = (ImageView) menuButtonsContainer
                .findViewById(R.id.menuRefresh);
        menuRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificationFragment.updateVerificationPage();
            }
        });
        if (verificationFragment.needsRefresh) {
            menuRefresh.setVisibility(View.VISIBLE);
        } else {
            menuRefresh.setVisibility(View.GONE);
        }

        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setOnQueryTextListener(verificationFragment);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}