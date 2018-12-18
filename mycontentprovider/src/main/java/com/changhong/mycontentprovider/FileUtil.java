package com.changhong.mycontentprovider;

import android.content.Context;
import android.os.storage.StorageManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class FileUtil {

    private static final String TAG = "qll_file_util";

    public static String[] getExternalPaths(Context context) {
        String[] paths = {};
        StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);

        try {
            Class<?>[] paramTypes = {};
            Method getVolumePaths = StorageManager.class.getMethod("getVolumePaths", paramTypes);
            getVolumePaths.setAccessible(true);
            Object[] values = {};
            paths = (String[]) getVolumePaths.invoke(sm, values);


        } catch (NoSuchMethodException e) {
            Log.e(TAG, "getExternalPath: " + e.toString());
        } catch (IllegalAccessException e) {
            Log.e(TAG, "getExternalPath: " + e.toString() );
        } catch (InvocationTargetException e) {
            Log.e(TAG, "getExternalPath: " + e.toString() );
        }

        return paths;
    }

    public static String getFileType(String path) {
        HashMap<String, String> mFileTypes = new HashMap<String, String>();
        mFileTypes.put("FFD8FF", "jpg");
        mFileTypes.put("89504E47", "png");
        mFileTypes.put("47494638", "gif");
        mFileTypes.put("424D", "bmp");

        return mFileTypes.get(getFileHeader(path));

    }

    public static String getFileHeader(String filePath) {
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[3];
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return value;
    }

    /**
     * 将byte字节转换为十六进制字符串
     *
     * @param src
     * @return
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }


}
