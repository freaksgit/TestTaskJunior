package com.stoliarchuk.vasyl.testtaskjunior.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by freak on 01.08.2017.
 */

public class RssContract implements BaseColumns{
    public static final String CONTENT_AUTHORITY = "com.stoliarchuk.vasyl.testtaskjunior";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY;

    public static final String TABLE_NAME = "rss";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_IMAGE_LINK = "image_link";

    public static Uri buildRssUri(long id) {
        return ContentUris.withAppendedId(BASE_CONTENT_URI, id);
    }
}
