package com.math.yang.mathyang.download;

import android.content.Context;
import android.content.Intent;

import com.math.yang.mathyang.activity.SelectDownlaodActivity;
import com.math.yang.mathyang.model.DownloadBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangtx on 2018/1/11.
 */

public class DownloadHelper {
    public static void addDownload(Context context, DownloadBean data) {
        Intent intent = new Intent();
        intent.setAction(DownloadService.INTENT_DOWNLOAD_RESTART_START);
        intent.putExtra(DownloadService.EXTRA_DOWNLOAD_OBJECT, data);
        context.sendBroadcast(intent);
    }

    public static void addDownloadList(Context context, List<DownloadBean> dataList) {
        for (DownloadBean downloadBean : dataList) {
            addDownload(context, downloadBean);
        }
    }
}
