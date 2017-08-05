package com.stoliarchuk.vasyl.testtaskjunior;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.stoliarchuk.vasyl.testtaskjunior.database.RssContract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class RssDownloaderService extends IntentService {
    private final static String TAG = RssDownloaderService.class.getSimpleName();
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_DOWNLOAD_RSS = "com.stoliarchuk.vasyl.testtaskjunior.action.RSS";

    private static final String EXTRA_LINK = "com.stoliarchuk.vasyl.testtaskjunior.extra.LINK";

    private Context mContext;

    public RssDownloaderService() {
        super("RssDownloaderService");
    }

    /**
     * Starts this service to perform action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startDownloadRss(Context context, URL link) {
        Intent intent = new Intent(context, RssDownloaderService.class);
        intent.setAction(ACTION_DOWNLOAD_RSS);
        intent.putExtra(EXTRA_LINK, link);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD_RSS.equals(action)) {
                final URL link = (URL) intent.getSerializableExtra(EXTRA_LINK);
                downloadRss(link);
            }
        }
    }

    /**
     * Handle action in the provided background thread with the provided
     * parameters.
     */
    private void downloadRss(URL url) {
        ArrayList<RssItem> items = null;
        if (url == null) {
            return;
        } else {
            items = RssItem.getRssItems(url);
            if (null != items && items.size() > 0) {
                pushDataIntoDb(items);
            }
        }
        //sendBroadcast(new Intent(MainActivity.Receiver.INTENT_FILTER).putParcelableArrayListExtra(MainActivity.Receiver.EXTRA_ITEMS, items));
    }

    private void pushDataIntoDb(ArrayList<RssItem> items) {
        ContentValues values = new ContentValues();
        ContentResolver contentResolver = getContentResolver();
        for (RssItem item : items) {
            values.clear();
            values.put(RssContract.COLUMN_TITLE, item.getTitle());
            values.put(RssContract.COLUMN_CATEGORY, item.getCategory());
            values.put(RssContract.COLUMN_DESCRIPTION, item.getDescription());
            values.put(RssContract.COLUMN_LINK, item.getLink());
            values.put(RssContract.COLUMN_IMAGE_LINK, item.getImageLink());
            contentResolver.insert(RssContract.BASE_CONTENT_URI, values);

            Calendar calendar = Calendar.getInstance();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            int currentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
            prefs.edit().putInt("day", currentDayOfYear).commit();

            stopSelf();
        }
    }
}
