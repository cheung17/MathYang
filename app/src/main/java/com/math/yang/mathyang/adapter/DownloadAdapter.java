package com.math.yang.mathyang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.math.yang.mathyang.R;
import com.math.yang.mathyang.download.DownloadHelper;
import com.math.yang.mathyang.download.DownloadService;
import com.math.yang.mathyang.model.DownloadBean;
import com.math.yang.mathyang.model.UnitVideo;
import com.math.yang.mathyang.orm.DownloadDbUtil;
import com.math.yang.mathyang.view.CircleProgress;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ztx
 */
public class DownloadAdapter extends BaseAdapter {
    public List<UnitVideo> mUnitList = new ArrayList<>();
    private int downloadRes[] = {
            R.drawable.ic_zhizhen,//未下载
            R.drawable.ic_downloading,//下载中
            R.drawable.ic_zhizhen,//已添加,等待中
            R.drawable.ic_gou,//已完成
    };
    /**
     * 上下文
     */
    private Context mContext;

    public DownloadAdapter(List<UnitVideo> unitList, Context context) {
        this.mUnitList = unitList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mUnitList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUnitList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @SuppressLint("WrongViewCast")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder tempHolder = null;
        UnitVideo unitVideo = mUnitList.get(position);
        DownloadBean downloadBean = DownloadDbUtil.getDownloadDataById(unitVideo.getId());
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_download, null);
            tempHolder = new ViewHolder();
            tempHolder.tvDownloadSize = convertView.findViewById(R.id.tv_size);
            tempHolder.tvTitle = convertView.findViewById(R.id.tv_title);
            tempHolder.rlUnitVideo = convertView.findViewById(R.id.rl_select_toot);
            tempHolder.circleProgress = convertView.findViewById(R.id.circleprogress);
            tempHolder.ivDownloadStatus = convertView.findViewById(R.id.iv_download_status);
            convertView.setTag(tempHolder);

        } else {
            tempHolder = (ViewHolder) convertView.getTag();
        }
        tempHolder.tvTitle.setText(mUnitList.get(position).getTitle());
        float totalSize = (float) downloadBean.getTotalBytes() / 1024 / 1024;
        float sofarSize = (float) downloadBean.getSofarBytes() / 1024 / 1024;
        String sofarDownloaded = String.format("%.2f", sofarSize) + "M/";
        tempHolder.tvDownloadSize.setText(downloadBean.getStatus() == DownloadService.DOWNLAOD_FINISHED ? "" : sofarDownloaded
                + String.format("%.2f", totalSize) + "M");
        tempHolder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.download_black));
        tempHolder.circleProgress.setImageResource(downloadRes[downloadBean.getStatus()]);
        tempHolder.circleProgress.setProgress(downloadBean.getProgress());
        tempHolder.circleProgress.setVisibility(downloadBean.getStatus() == DownloadService.DOWNLAOD_FINISHED ? View.GONE : View.VISIBLE);
        tempHolder.ivDownloadStatus.setVisibility(downloadBean.getStatus() != DownloadService.DOWNLAOD_FINISHED ? View.GONE : View.VISIBLE);
        tempHolder.rlUnitVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRootClickListener != null) {
                    onRootClickListener.onUnitRootCLick(position);
                }
            }
        });
        return convertView;
    }

    OnRootClickListener onRootClickListener;

    public void setOnRootClickListener(OnRootClickListener onRootClickListener) {
        this.onRootClickListener = onRootClickListener;
    }

    public interface OnRootClickListener {
        void onUnitRootCLick(int position);
    }

    public interface OnSendCommentClickListener {
        void onSendClick(String commentText);
    }

    /***
     * 静态类
     */
    class ViewHolder {
        /**
         * 显示文字
         */
        private TextView tvTitle, tvDownloadSize;
        private CircleProgress circleProgress;
        private RelativeLayout rlUnitVideo;
        private ImageView ivDownloadStatus;

    }

}
