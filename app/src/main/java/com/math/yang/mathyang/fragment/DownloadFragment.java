package com.math.yang.mathyang.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.commonsdk.application.BaseFragment;
import com.math.yang.mathyang.R;
import com.math.yang.mathyang.adapter.DownloadAdapter;
import com.math.yang.mathyang.download.DownloadService;
import com.math.yang.mathyang.model.DownloadBean;
import com.math.yang.mathyang.model.UnitVideo;
import com.math.yang.mathyang.orm.DownloadDbUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtx on 2018/1/2.
 */

public class DownloadFragment extends BaseFragment {
    private ListView listView;
    private List<UnitVideo> mUnitList = new ArrayList<>();
    private DownloadAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        initViews(view);
        initData();
        registBroadcast();

        return view;
    }

    private void registBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_DOWNLOAD);
        getContext().registerReceiver(downloadReciver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(downloadReciver);
    }

    private void initData() {
        mUnitList = DownloadDbUtil.getDownloadingUnitList();
        //   mUnitList = UnitDbUtil.getDownloadingList();
        mAdapter = new DownloadAdapter(mUnitList, getContext());
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        mUnitList = DownloadDbUtil.getDownloadingUnitList();
        mAdapter = new DownloadAdapter(mUnitList, getContext());
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 下载状态接收广播
     */
    public BroadcastReceiver downloadReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                switch (intent.getAction()) {
                    case DownloadService.ACTION_DOWNLOAD: //暂停
                        DownloadBean data = (DownloadBean) intent.getSerializableExtra(DownloadService.EXTRA_DOWNLOAD_OBJECT);
                        for (UnitVideo unitVideo : mUnitList) {
                            if (data.getDownloadId().equals(unitVideo.getId())) {
                                unitVideo.setProgress(data.getProgress());
                                unitVideo.setDownloadStatus(data.getStatus());
                                break;
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        break;

                }
            }
        }
    };

    private void initViews(View view) {
        listView = view.findViewById(R.id.listView);
    }
}
