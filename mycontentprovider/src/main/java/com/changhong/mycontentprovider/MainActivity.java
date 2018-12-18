package com.changhong.mycontentprovider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;

public class MainActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "qll_provider_activity";

    public static final String PROVIDER_URI = "content://com.changhong.mycontentprovider.provider";
    public static final String TABLE_CONFIG = PROVIDER_URI + "/config";
    public static final String TABLE_RESOURCE = PROVIDER_URI + "/resource";
    public static final String DEFAULT_MODE = "/default_mode";
    private String nowMode;
    private TextView tvMode, tvPrompt;
    private Button btnChangeMod, btnPreview, btnImport;


    @Override
    protected void onCreate(Bundle args) {
        super.onCreate(args);
        setContentView(R.layout.activity_main);

        // 监控另一个程序是否启动

        initView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_mod:
                changeMod();
                break;
            case R.id.btn_import_resource:
                break;
            case R.id.btn_preview:
                break;
            default:
                break;
        }
    }

    void initView() {
        Log.i(TAG, "initView: ");
        tvMode = (TextView) findViewById(R.id.tv_now_mod);
        tvPrompt = (TextView) findViewById(R.id.tv_prompt);
        btnChangeMod = (Button) findViewById(R.id.btn_change_mod);
        btnImport = (Button) findViewById(R.id.btn_import_resource);
        btnPreview = (Button) findViewById(R.id.btn_preview);

        String modeText = getString(R.string.background_mod);
        modeText = String.format(modeText, getMode());
        tvMode.setText(modeText);

        if (nowMode.equals("image")) {
            tvPrompt.setText(getString(R.string.image_prompt));
        } else if (nowMode.equals("video")) {
            tvPrompt.setText(getString(R.string.video_mod));
        }

        btnPreview.setOnClickListener(this);
        btnImport.setOnClickListener(this);
        btnChangeMod.setOnClickListener(this);

    }

    String getMode() {
        Uri uri = Uri.parse(TABLE_CONFIG + DEFAULT_MODE);
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        String value = "";
        if (cursor != null) {
            while (cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex("value"));
            }
            cursor.close();
        }


        if (value.equals("video")) {
            nowMode = "video";
            return getString(R.string.video_mod);
        } else {
            nowMode = "image";
            return getString(R.string.image_mod);
        }

    }

    void changeMod() {
        Uri uri = Uri.parse(TABLE_CONFIG);
        ContentValues values = new ContentValues();

        if (nowMode.equals("image")) {
            values.put("value", "video");

        } else if (nowMode.equals("video")) {
            values.put("value", "image");

        }

        String[] args = {"default_mode"};

        getContentResolver().update(uri, values, "name = ?", args);

        String modeText = getString(R.string.background_mod);
        modeText = String.format(modeText, getMode());
        tvMode.setText(modeText);

        if (nowMode.equals("image")) {
            tvPrompt.setText(getString(R.string.image_prompt));
        } else if (nowMode.equals("video")) {
            tvPrompt.setText(getString(R.string.video_prompt));
        }

        // 右边的素材列表也需要变化

    }
}
