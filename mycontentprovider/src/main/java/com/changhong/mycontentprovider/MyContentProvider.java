package com.changhong.mycontentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyContentProvider extends ContentProvider {
    private static final String TAG = "qll_provider";

    public static final int RESOURCE_DIR = 0;
    public static final int RESOURCE_ITEM = 1;
    public static final int CONFIG_DIR = 2;
    public static final int CONFIG_ITEM = 3;
    public static final String AUTHORITY = "com.changhong.mycontentprovider.provider";
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "resource", RESOURCE_DIR);
        uriMatcher.addURI(AUTHORITY, "resource/*", RESOURCE_ITEM);
        uriMatcher.addURI(AUTHORITY, "config", CONFIG_DIR);
        uriMatcher.addURI(AUTHORITY, "config/*", CONFIG_ITEM);

    }
    @Override
    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper(getContext(), "media.db", null, 2);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case RESOURCE_DIR:
                cursor = db.query("resource", projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case RESOURCE_ITEM:
                String imgId = uri.getPathSegments().get(1);
                cursor = db.query("resource", projection, "id = ?",
                        new String[] {imgId}, null, null, sortOrder);
                break;
            case CONFIG_DIR:
                cursor = db.query("config", projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case CONFIG_ITEM:
                String configId = uri.getPathSegments().get(1);
                cursor = db.query("config", projection, "name = ?",
                        new String[] {configId}, null, null, sortOrder);
                break;
            default:
                break;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Uri uriReturn = null;

        switch (uriMatcher.match(uri)) {
            case RESOURCE_DIR:
            case RESOURCE_ITEM:
                long newImageId = db.insert("resource", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/resource/" + newImageId);
                break;
            case CONFIG_DIR:
            case CONFIG_ITEM:
                Log.i(TAG, "insert: 企图插入数据");
                long newVideoId = db.insert("config", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/config/" + newVideoId);
                break;
            default:
                break;
        }

        return uriReturn;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri)) {
            case CONFIG_DIR:
                updateRows = db.update("config", values, selection, selectionArgs);
                break;
            case CONFIG_ITEM:
                String configName = uri.getPathSegments().get(1);
                updateRows = db.update("config", values, "name = ?",
                        new String[]{configName});
                break;
            case RESOURCE_DIR:
                updateRows = db.update("config", values, selection, selectionArgs);
                break;
            case RESOURCE_ITEM:
                String resourcePath = uri.getPathSegments().get(1);
                updateRows = db.update("config", values, "name = ?",
                        new String[]{resourcePath});
                break;

        }
        return updateRows;
    }
}
