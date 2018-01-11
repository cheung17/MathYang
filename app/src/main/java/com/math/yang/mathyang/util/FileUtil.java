package com.math.yang.mathyang.util;

import android.os.Environment;

import com.commonsdk.file.FileIOandOperation;

import java.io.File;

/**
 * Created by zhangtx on 2018/1/4.
 */

public class FileUtil {
    public static final String GEN_PATH = Environment.getExternalStorageDirectory() + "/mathyang/";
    public static final String LOCAL_INDEX_PATH = GEN_PATH + "index/";
    public static final String VIDEO_DOWNLOAD_PATH = GEN_PATH + "video/";
    public static final String INDEX_PATTERN = ".index";

    public static String getCourseIndexFile(String id) {
        return LOCAL_INDEX_PATH + id + INDEX_PATTERN;
    }

    public static boolean compareFileContentSame(String file1, String file2) {
        String s1 = String.valueOf(FileIOandOperation.readFile(new File(file1), FileIOandOperation.UTF_8));
        String s2 = String.valueOf(FileIOandOperation.readFile(new File(file2), FileIOandOperation.UTF_8));
        if (s1 == null || null == s2) {
            return false;
        }
        return s1.equals(s2);
    }

    public static String getCourseIndex(String id) {
        File file = new File(LOCAL_INDEX_PATH + id + INDEX_PATTERN);
        if (file.exists()) {
            return String.valueOf(FileIOandOperation.readFile(file, "UTF-8"));
        } else {
            return "";
        }
    }

    public static String getDownloadPathById(String downloadId) {
        File dirFile = new File(VIDEO_DOWNLOAD_PATH);
        if (dirFile.exists()) {
            dirFile.mkdirs();
        }
        return VIDEO_DOWNLOAD_PATH + downloadId + ".mp4";
    }
}
