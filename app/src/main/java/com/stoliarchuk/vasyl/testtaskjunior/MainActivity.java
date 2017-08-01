package com.stoliarchuk.vasyl.testtaskjunior;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView tv, linkTv;
    private BroadcastReceiver receiver = new Receiver();
    private IntentFilter intentFilter = new IntentFilter(Receiver.INTENT_FILTER);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        linkTv = (TextView) findViewById(R.id.link_tv);
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

        public void onReceive(Context context, Intent intent) {
            String rss = intent.getStringExtra(Intent.EXTRA_TEXT);
            tv.setText(rss);
        }
    }

}
