package com.stoliarchuk.vasyl.testtaskjunior;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.stoliarchuk.vasyl.testtaskjunior.fragments.DetailFragment;
import com.stoliarchuk.vasyl.testtaskjunior.fragments.RssListFragment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements RssListFragment.OnRssItemClickListener{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    Toolbar mToolbar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterdetail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        startRssDownloader();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (null == fragment) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private Fragment createFragment() {
        return new RssListFragment();
    }


    public void startRssDownloader() {
        if (isNeedToGetFreshData()) {
            URL url = null;
            try {
                url = new URL("http://feeds.abcnews.com/abcnews/topstories");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (null != url) {
                RssDownloaderService.startDownloadRss(this, url);
            }
        }
    }

    public boolean isNeedToGetFreshData() {
        Calendar calendar = Calendar.getInstance();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        int currentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        Log.v(LOG_TAG, "Current day of year: " + currentDayOfYear);

        int savedDayOfYear = prefs.getInt("day", -1);
        Log.v(LOG_TAG, "Saved day of year: " + savedDayOfYear);

            if (currentDayOfYear > savedDayOfYear) {
                return true;
            } else {
                return false;
            }

    }

    @Override
    public void onItemClicked(String url) {
        if (findViewById(R.id.detail_fragment_container) == null){
            startActivity(DetailActivity.createIntent(this, url));
        }else{
            Fragment detailFragment = DetailFragment.newInstance(url);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, detailFragment)
                    .commit();
        }
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
