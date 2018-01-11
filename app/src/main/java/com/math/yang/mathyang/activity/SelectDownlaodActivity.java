package com.math.yang.mathyang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.commonsdk.application.BaseActivity;
import com.commonsdk.view.toast.Toasty;
import com.math.yang.mathyang.R;
import com.math.yang.mathyang.adapter.SelectDownloadAdapter;
import com.math.yang.mathyang.download.DownloadHelper;
import com.math.yang.mathyang.download.DownloadService;
import com.math.yang.mathyang.model.DownloadBean;
import com.math.yang.mathyang.model.UnitVideo;
import com.math.yang.mathyang.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtx on 2018/1/10.
 */

public class SelectDownlaodActivity extends BaseActivity implements View.OnClickListener {
    public static final String EXTRA_DOWNLOAD_LIST = "EXTRA_DOWNLOAD_LIST";
    private ImageView ivClose;
    private Button btnSelectAll;
    private ListView listView;
    private SelectDownloadAdapter mAdapter;
    private List<UnitVideo> mUnitList = new ArrayList<>();
    private int selectedCount = 0;
    private int canDownloadCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download);
        initViews();
        initData();
    }

    private void initData() {
        mUnitList = (List<UnitVideo>) getIntent().getSerializableExtra(EXTRA_DOWNLOAD_LIST);
        checkAvaliableDownloadSize();
        mAdapter = new SelectDownloadAdapter(mUnitList, this);
        listView.setAdapter(mAdapter);
        mAdapter.setOnRootClickListener(new SelectDownloadAdapter.OnRootClickListener() {
            @Override
            public void onUnitRootCLick(int position) {
                checkOne(position);
            }
        });

    }

    private void checkAvaliableDownloadSize() {
        for (UnitVideo unitVideo : mUnitList) {
            if (unitVideo.getDownloadStatus() == DownloadService.DOWNLOAD_NOT_YET) {
                canDownloadCount++;
            }
        }
    }

    private void checkOne(int position) {
        mUnitList.get(position).setChecked(!mUnitList.get(position).isChecked());
        selectedCount = selectedCount + (mUnitList.get(position).isChecked() ? 1 : -1);
        if (selectedCount < canDownloadCount) {
            btnSelectAll.setText("全选");
        }
    }

    /**
     * 全选或取消全选
     */
    private void selectOrDisSelectAll() {
        btnSelectAll.setText(selectedCount == canDownloadCount ? "全选" : "取消全选");
        for (UnitVideo unitVideo : mUnitList) {
            unitVideo.setChecked(selectedCount == canDownloadCount ? false : true);
        }
        selectedCount = selectedCount == canDownloadCount ? 0 : canDownloadCount;
        mAdapter.notifyDataSetChanged();
    }

    private void initViews() {
        findViewById(R.id.iv_close).setOnClickListener(this);
        btnSelectAll = findViewById(R.id.btn_select_all);
        findViewById(R.id.btn_confirm_download).setOnClickListener(this);
        findViewById(R.id.btn_select_all).setOnClickListener(this);
        listView = findViewById(R.id.listView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.btn_select_all:
                selectOrDisSelectAll();
                break;
            case R.id.btn_confirm_download:
                gotoDownload();
                break;
        }
    }

    private void gotoDownload() {
        List<DownloadBean> dataList = new ArrayList<>();
        for (UnitVideo unitVideo : mUnitList) {
            if (unitVideo.isChecked() && unitVideo.getIspunit() != 1 && unitVideo.getDownloadStatus() == DownloadService.DOWNLOAD_NOT_YET) {
                DownloadBean downloadBean = new DownloadBean();
                downloadBean.setBookid(unitVideo.getBookid());
                downloadBean.setDownloadUrl(unitVideo.getVideo_url().startsWith("http://") ? unitVideo.getVideo_url() : Constant.FTPIP + unitVideo.getVideo_url());
                downloadBean.setDownloadId(unitVideo.getId());
                dataList.add(downloadBean);
            }
        }
        DownloadHelper.addDownloadList(this, dataList);
    }


}
