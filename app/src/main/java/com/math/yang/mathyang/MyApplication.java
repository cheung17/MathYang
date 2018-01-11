package com.math.yang.mathyang;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

import com.litesuits.orm.db.DataBaseConfig;
import com.liulishuo.filedownloader.FileDownloader;
import com.math.yang.mathyang.download.DownloadService;
import com.math.yang.mathyang.orm.MathOrm;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by zhangtx on 2018/1/2.
 */

public class MyApplication extends Application {
    /**
     * 数据库名称
     */
    public final static String BOOK_DB = "math_db";

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
        FileDownloader.init(getApplicationContext());
        DataBaseConfig dBaseConfig = new DataBaseConfig(getApplicationContext(), BOOK_DB);
        MathOrm.init(dBaseConfig);
        Intent serviceIntent = new Intent(getApplicationContext(), DownloadService.class);
        startService(serviceIntent);

    }

    private void initImageLoader() {
        // 初始化图片加载
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                // 自定义缓存路径
                .defaultDisplayImageOptions(defaultOptions).threadPoolSize(4)
                .imageDownloader(
                        new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // 超时时间
                .build();// 开始构建
        // 全局初始化此配置
        ImageLoader.getInstance().init(config);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
