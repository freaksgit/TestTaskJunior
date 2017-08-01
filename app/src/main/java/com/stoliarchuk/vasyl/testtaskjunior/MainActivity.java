package com.stoliarchuk.vasyl.testtaskjunior;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private ArrayList<RssItem> rssItems = new ArrayList<>();
    private TextView tv, linkTv;
    private BroadcastReceiver receiver = new Receiver();
    private IntentFilter intentFilter = new IntentFilter(Receiver.INTENT_FILTER);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        linkTv = (TextView) findViewById(R.id.link_tv);
        startRssDownloaderService();
    }

    private void startRssDownloaderService() {
        URL url = null;
        try {
            url = new URL(linkTv.getText().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (null != url) {
            RssDownloaderService.startDownloadRss(this, url);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(receiver);
    }

    class Receiver extends BroadcastReceiver{
        public static final String INTENT_FILTER = "com.stoliarchuk.vasyl.testtaskjunior.INTENT_FILTER";
        public static final String EXTRA_ITEMS = "com.stoliarchuk.vasyl.testtaskjunior.ACTION_ITEMS";
        public void onReceive(Context context, Intent intent) {
            rssItems = intent.getParcelableArrayListExtra(EXTRA_ITEMS);
            Log.v("TAG", Arrays.toString(rssItems.toArray()));

        }
    }

}
