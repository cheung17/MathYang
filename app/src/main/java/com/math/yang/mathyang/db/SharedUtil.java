package com.math.yang.mathyang.db;

import android.content.Context;

import com.math.yang.mathyang.course.SampleListFragment;

/**
 * Created by zhangtx on 2018/1/9.
 */

public class SharedUtil {
    public static final String EXTRA_BOOK_VIDEO_POSITION = "EXTRA_BOOK_VIDEO_HISTORY";

    /**
     * 保存播放位置，以便下次播放时接着上次的位置继续播放.
     *
     * @param context
     * @param bookid  书的id
     */
    public static void putBookPlayPosition(Context context, String bookid, long position) {
        context.getSharedPreferences(EXTRA_BOOK_VIDEO_POSITION,
                Context.MODE_PRIVATE)
                .edit()
                .putLong(bookid, position)
                .apply();
    }

    public static long getBookPlayPosition(Context context, String bookid) {
        return context.getSharedPreferences(EXTRA_BOOK_VIDEO_POSITION,
                Context.MODE_PRIVATE).getLong(bookid, 0);

    }
}
