package com.math.yang.mathyang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsdk.dateandtime.DateAndTimeUtil;
import com.commonsdk.dateandtime.RelativeDateTime;
import com.commonsdk.network.NetUtils;
import com.math.yang.mathyang.R;
import com.math.yang.mathyang.download.DownloadService;
import com.math.yang.mathyang.model.UnitVideo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ztx
 */
public class SelectDownloadAdapter extends BaseAdapter {
    public List<UnitVideo> mUnitList = new ArrayList<>();
    private int downloadRes[] = {R.drawable.ic_wating, R.drawable.ic_wating, R.drawable.ic_wating, R.drawable.ic_gou, R.drawable.ic_gou};
    /**
     * 上下文
     */
    private Context mContext;

    public SelectDownloadAdapter(List<UnitVideo> unitList, Context context) {
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_select_downlad, null);
            tempHolder = new ViewHolder();
            tempHolder.ivDownloadStatus = convertView.findViewById(R.id.iv_download_status);
            tempHolder.tvSize = convertView.findViewById(R.id.tv_size);
            tempHolder.tvTitle = convertView.findViewById(R.id.tv_title);
            tempHolder.checkBox = convertView.findViewById(R.id.check_download);
            tempHolder.rlUnitVideo = convertView.findViewById(R.id.rl_select_toot);
            convertView.setTag(tempHolder);
        } else {
            tempHolder = (ViewHolder) convertView.getTag();
        }
        tempHolder.tvTitle.setText(mUnitList.get(position).getTitle());
        float size = (float) unitVideo.getFilesize() / 1024;
        tempHolder.tvSize.setText(String.format("%.2f", size) + "M");
        if (unitVideo.getIspunit() == 1) {
            //是标题
            tempHolder.tvSize.setVisibility(View.GONE);
            tempHolder.checkBox.setVisibility(View.GONE);
            tempHolder.ivDownloadStatus.setVisibility(View.GONE);
            //tempHolder.tvStatus.setVisibility(View.GONE);
            tempHolder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.download_unit_title));
            tempHolder.checkBox.setChecked(false);

        } else {
            tempHolder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.download_black));
            tempHolder.tvSize.setVisibility(unitVideo.getDownloadStatus() == DownloadService.DOWNLOAD_NOT_YET ? View.VISIBLE : View.GONE);
            tempHolder.checkBox.setVisibility(unitVideo.getDownloadStatus() == DownloadService.DOWNLOAD_NOT_YET ? View.VISIBLE : View.GONE);
            tempHolder.ivDownloadStatus.setVisibility(unitVideo.getDownloadStatus() != DownloadService.DOWNLOAD_NOT_YET ? View.VISIBLE : View.GONE);
            // tempHolder.tvStatus.setVisibility(View.GONE);
            tempHolder.checkBox.setChecked(unitVideo.isChecked());
            final ViewHolder finalTempHolder = tempHolder;
            tempHolder.rlUnitVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRootClickListener != null) {
                        finalTempHolder.checkBox.setChecked(!finalTempHolder.checkBox.isChecked());
                        onRootClickListener.onUnitRootCLick(position);
                    }
                }
            });
        }

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
        private TextView tvTitle, tvSize;
        private ImageView ivDownloadStatus;
        private CheckBox checkBox;
        private RelativeLayout rlUnitVideo;

    }

}
