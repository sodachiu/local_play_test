package com.changhong.mycontentprovider;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    private static final String TAG = "qll_my_service";

    public static final String MOUNT_PATH = "/mnt/sda/sda1";
    public static final String IMAGE_DIR = "/background";
    public static final int FIND_DEVICE = 0;
    private String[] paths;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FIND_DEVICE:
                    Log.i(TAG, "handleMessage: 打算开始读取文件");
                    readImage();
                    break;

            }
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand: 新的服务已经启动");

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 把U盘的所有路径下的图片读出来,或者是只读background文件夹下的
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Log.i(TAG, "run: 计时器启动");
                        paths = FileUtil.getExternalPaths(getApplicationContext());
                        for (String path : paths) {

                            if (path.equals(MOUNT_PATH)) {
                                Log.i(TAG, "run: 拿到了心意的路径");
                                myHandler.sendEmptyMessage(FIND_DEVICE);
                                cancel();
                                break;
                            }
                        }
                    }
                }, 0, 2000);

            }
        }).start();

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    void readImage() {
        Log.i(TAG, "readImage: ");
        String path = MOUNT_PATH + IMAGE_DIR;
        File file = new File(path);

        if (!file.exists()) {
            Log.i(TAG, "readImage: 文件夹不存在");
            return;
        }

        File[] pictures = file.listFiles();
        Log.i(TAG, "readImage: 文件数量----" + pictures.length);

        for (File item : pictures) {
            if (item.isFile()) {
                String fileType = FileUtil.getFileType(item.getPath());
                if (fileType == null) {
                    Log.i(TAG, "readImage: 文件类型不符合");
                    continue;
                }

                Log.i(TAG, "readImage: get图片路径");

                addData(item);

            }
        }

    }

    void addData(File file) {

        File imageDir = getExternalFilesDir("image");
        if (file != null) {
            Log.i(TAG, "onCreate: file_path----" + imageDir.toString());

            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                String fileName = file.getName();
                String outPath = imageDir.getPath() + "/" + fileName;
                File outFile = new File(outPath);
                FileOutputStream fileOutputStream = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int byteRead;
                while (-1 != (byteRead = fileInputStream.read(buffer))) {
                    fileOutputStream.write(buffer, 0, byteRead);
                }
                fileInputStream.close();
                fileOutputStream.flush();
                fileOutputStream.close();

            } catch (Exception e) {
                Log.e(TAG, "addData: 写入图片出错----" + e.toString() );
            }
            // 加入到自己的file中
        }

        Uri uri = Uri.parse("content://com.changhong.mycontentprovider.provider/image");
        ContentValues values = new ContentValues();
        values.put("image_path", file.getAbsolutePath());
        values.put("number", 1008);
        Uri valuesUri = getContentResolver().insert(uri, values);

        if (valuesUri != null) {
            Log.i(TAG, "addData: valuesUri----" + valuesUri.toString());
        }
    }

}
