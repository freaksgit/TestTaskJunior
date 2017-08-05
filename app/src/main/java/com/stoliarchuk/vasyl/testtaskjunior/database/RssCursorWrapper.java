package com.stoliarchuk.vasyl.testtaskjunior.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.stoliarchuk.vasyl.testtaskjunior.RssItem;

/**
 * Created by freak on 01.08.2017.
 */

public class RssCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public RssCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public RssItem getRssItem(){
        String title = getString(getColumnIndex(RssContract.COLUMN_TITLE));
        String description = getString(getColumnIndex(RssContract.COLUMN_DESCRIPTION));
        String category = getString(getColumnIndex(RssContract.COLUMN_CATEGORY));
        String link = getString(getColumnIndex(RssContract.COLUMN_LINK));
        String imageLink = getString(getColumnIndex(RssContract.COLUMN_IMAGE_LINK));

        return new RssItem(title, category, description, link, imageLink);
    }
}
