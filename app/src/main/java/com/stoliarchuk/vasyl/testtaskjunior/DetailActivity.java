package com.stoliarchuk.vasyl.testtaskjunior;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.stoliarchuk.vasyl.testtaskjunior.fragments.DetailFragment;
import com.stoliarchuk.vasyl.testtaskjunior.fragments.RssListFragment;

public class DetailActivity extends AppCompatActivity {
    private String mUrl;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUrl = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.detail_fragment_container);
        if (null == fragment) {
            fragment = DetailFragment.newInstance(mUrl);
            fm.beginTransaction()
                    .add(R.id.detail_fragment_container, fragment)
                    .commit();
        }
    }

    public static Intent createIntent(Context context, String url){
        return new Intent(context, DetailActivity.class)
                .putExtra(Intent.EXTRA_TEXT, url);
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.detail_fragment_container) == null){
            super.onBackPressed();
        }else{
            DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container);
            if (detailFragment != null){
                detailFragment.onBackPressed();
            }
        }
    }
}
