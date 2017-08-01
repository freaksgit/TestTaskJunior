package com.stoliarchuk.vasyl.testtaskjunior;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RssDownloaderService extends IntentService {

    private final static String TAG = RssDownloaderService.class.getSimpleName();
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_DOWNLOAD_RSS = "com.stoliarchuk.vasyl.testtaskjunior.action.RSS";

    private static final String EXTRA_LINK = "com.stoliarchuk.vasyl.testtaskjunior.extra.LINK";

    public RssDownloaderService() {
        super("RssDownloaderService");
    }

    /**
     * Starts this service to perform action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
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
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void downloadRss(URL url) {

        if (url == null) {
            return;
        }else{
            List<RssItem> items = RssItem.getRssItems(url);
            Log.v(TAG, Arrays.toString(items.toArray()));
        }
/*
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String responseJson = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                Log.e(TAG, "Empty response");
                return;
            }
            responseJson = buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
        sendBroadcast(new Intent(MainActivity.Receiver.INTENT_FILTER).putExtra(Intent.EXTRA_TEXT, responseJson));*/
    }
}
