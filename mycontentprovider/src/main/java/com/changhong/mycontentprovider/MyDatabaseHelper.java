package com.changhong.mycontentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    /**
     * path: 资源路径
     * type: 0 为图片，1为视频
     * size: 文件大小
     * */
    public static final String CREATE_RESOURCE = "create table resource ("
            + "path text primary key, "
            + "type integer, "
            + "size biginteger)";

    public static final String CREATE_CONFIG = "create table config ("
            + "name text primary key, "
            + "value text)";


    private Context mContext;

    public MyDatabaseHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_RESOURCE);
        db.execSQL(CREATE_CONFIG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
