package com.stoliarchuk.vasyl.testtaskjunior.database;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

import com.stoliarchuk.vasyl.testtaskjunior.RssItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by freak on 01.08.2017.
 */

public class RssItemsLab {

    public static List<RssItem> getRssItems(Context context) {
        List<RssItem> items = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(RssContract.BASE_CONTENT_URI, null, null, null, null);
        RssCursorWrapper cursorWrapper = new RssCursorWrapper(cursor);
        if (null != cursor) {
            try {
                cursorWrapper.moveToFirst();
                while (!cursorWrapper.isAfterLast()) {
                    items.add(cursorWrapper.getRssItem());
                    cursorWrapper.moveToNext();
                }
            } finally {
                cursorWrapper.close();
            }
        }
        return items;
    }
}
