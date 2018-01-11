package com.math.yang.mathyang.download;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.math.yang.mathyang.model.DownloadBean;
import com.math.yang.mathyang.orm.DownloadDbUtil;
import com.math.yang.mathyang.util.FileUtil;
import com.math.yang.mathyang.util.LL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhangtx on 2018/1/10.
 */

public class DownloadService extends Service {
    public static final int DOWNLOAD_NOT_YET = 0;
    public static final int DOWNLOAD_ING = 1;
    public static final int DOWNLOAD_ADDED = 2;
    public static final int DOWNLAOD_FINISHED = 3;
    public static final String INTENT_DOWNLOAD_PAUSE = "shelf.updownloader.download.pause";
    public static final String INTENT_DOWNLOAD_RESTART_START = "shelf.updownloader.download.restart";
    public static final String ACTION_DOWNLOAD = "shelf.updownloader.download.added";
    public static final String EXTRA_DOWNLOAD_OBJECT = "EXTRA_DOWNLOAD_OBJECT";
    public static final int MAX_DOWNLOAD_COUNT = 3;
    /**
     * 正在下载ID
     */
    private List<DownloadBean> mDownloadingList = new ArrayList<>();
    /**
     * 等待下载ID
     */
    private List<DownloadBean> mWaitingList = new ArrayList<>();
    /**
     * 正在下载集合
     */
    private List<FileDownloadListener> mDownloadListenerList = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //检查历史下载，恢复下载
        resumeLocalDownload();
    }

    private void resumeLocalDownload() {
        List<DownloadBean> list = DownloadDbUtil.getDownloadingList();
        DownloadHelper.addDownloadList(getApplicationContext(), list);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerDownloadReceiver();
        LL.ztx("DownloadService启动");
        return START_STICKY;
    }

    /**
     * 注册下载广播
     */
    private void registerDownloadReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.INTENT_DOWNLOAD_PAUSE);
        filter.addAction(DownloadService.INTENT_DOWNLOAD_RESTART_START);
        registerReceiver(downloadReciver, filter);
    }

    /**
     * 通过Id查找是否在当前下载列表中
     *
     * @param dId 下载Id
     * @return 返回True||False
     */
    private FileDownloadListener getListenerByID(String dId) {
        for (int i = 0; i < mDownloadingList.size(); i++) {
            if (mDownloadingList.get(i).getDownloadId().equals(dId)) {
                return mDownloadListenerList.get(i);
            }

        }
        return null;
    }

    /**
     * 从下载列表里移除一个
     *
     * @param downloadId 下载ID
     */
    private void removeFromDwonloadListener(String downloadId) {
        for (int i = 0; i < mDownloadingList.size(); i++) {
            if (mDownloadingList.get(i).getDownloadId().equals(downloadId)) {
                mDownloadingList.remove(i);
                mDownloadListenerList.remove(i);
                break;
            }

        }
    }

    /**
     * 下载状态接收广播
     */
    public BroadcastReceiver downloadReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                switch (intent.getAction()) {
                    case INTENT_DOWNLOAD_PAUSE: //暂停
                        DownloadBean data = (DownloadBean) intent.getSerializableExtra(EXTRA_DOWNLOAD_OBJECT);

                        break;
                    case INTENT_DOWNLOAD_RESTART_START: //重新开始
                        DownloadBean data1 = (DownloadBean) intent.getSerializableExtra(EXTRA_DOWNLOAD_OBJECT);
                        data1.setStatus(DOWNLOAD_ADDED);
                        if (getListenerByID(data1.getDownloadId()) != null) {
                            removeFromDwonloadListener(data1.getDownloadId()); //从下载列表中删除，重新开始下载时 再重新创建对象添加进去
                        }
                        startDownload(data1);
                        break;
                }
            }
        }
    };

    /**
     * 开始下载
     *
     * @param data
     */
    private void startDownload(DownloadBean data) {
        if (mDownloadingList.size() >= MAX_DOWNLOAD_COUNT) {
            //已经超出同时下载限制，添加到等待列表
            mWaitingList.add(data);
            data.setStatus(DOWNLOAD_ADDED);
            DownloadDbUtil.saveDownload(data);
            return;
        }
        FileDownloadListener downloader = getDownloadListener();
        mDownloadingList.add(data);
        mDownloadListenerList.add(downloader);
        DownloadDbUtil.saveDownload(data);
        FileDownloader.getImpl()
                .create(data.getDownloadUrl())
                .setPath(FileUtil.getDownloadPathById(data.getDownloadId()))
                .setTag(data.getDownloadId())
                .setListener(downloader).start();

    }


    /**
     * 下载监听
     *
     * @return 返回监听
     */
    public FileDownloadListener getDownloadListener() {
        return new FileDownloadListener() {
            @Override
            protected void completed(BaseDownloadTask task) {
                Intent intent = new Intent();
                intent.setAction(ACTION_DOWNLOAD); //正在下载
                String downloadId = (String) task.getTag();
                DownloadBean data = DownloadDbUtil.getDownloadDataById(downloadId);
                data.setStatus(DOWNLAOD_FINISHED);
                DownloadDbUtil.saveDownload(data);
                intent.putExtra(EXTRA_DOWNLOAD_OBJECT, data);
                sendBroadcast(intent);
                removeFromDownload(downloadId);
                //http://localhost:8080/math/v1/book/loadbookunit.do
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                LL.ztx("progress: " + (int) (((float) soFarBytes / (float) totalBytes) * 100) + " " + task.getUrl());
                Intent intent = new Intent();
                intent.setAction(ACTION_DOWNLOAD); //正在下载
                String downloadId = (String) task.getTag();
                DownloadBean data = DownloadDbUtil.getDownloadDataById(downloadId);
                data.setSofarBytes(soFarBytes);
                data.setTotalBytes(totalBytes);
                data.setStatus(DOWNLOAD_ING);
                float progress = ((float) soFarBytes / (float) totalBytes) * 100;
                int a = (int) progress;
                data.setProgress(a);
                DownloadDbUtil.saveDownload(data);
                intent.putExtra(EXTRA_DOWNLOAD_OBJECT, data);
                sendBroadcast(intent);
            }

            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                LL.ztx("Connected " + task.getUrl());
            }

            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                LL.ztx("pending " + task.getUrl());

            }


            @Override
            protected void blockComplete(BaseDownloadTask task) {
                LL.ztx("blockComplete " + task.getUrl());

            }


            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                LL.ztx("paused " + task.getUrl());

            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                LL.ztx("error  " + task.getUrl() + e.getMessage() + " " + e.getLocalizedMessage());
                removeDownload2Waiting((String) task.getTag());
                //有文件下载失败 先暂停全部 再发送下载失败的广播
                //  task.pause();
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                // task.start();

                LL.ztx("warn " + task.getUrl());
            }
        };
    }

    private void removeFromDownload(String downloadId) {
        int index = findDownloadIndexById(downloadId);
        if (index != -1) {
            mDownloadListenerList.remove(index);
            mDownloadingList.remove(index);
        }
    }

    private void removeDownload2Waiting(String downloadId) {
        int index = findDownloadIndexById(downloadId);
        if (index != -1) {
            mWaitingList.add(mDownloadingList.get(index));
            mDownloadListenerList.remove(index);
            mDownloadingList.remove(index);
        }
    }

    private int findDownloadIndexById(String downloadId) {
        for (int i = 0; i < mDownloadingList.size(); i++) {
            if (downloadId.equals(mDownloadingList.get(i).getDownloadId())) {
                return i;
            }
        }
        return -1;
    }


}
