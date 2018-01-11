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
import com.math.yang.mathyang.model.UnitVideo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ztx
 */
public class UnitVideodapter extends BaseAdapter {
    public List<UnitVideo> mUnitList = new ArrayList<>();
    /**
     * 上下文
     */
    private Context mContext;

    public UnitVideodapter(List<UnitVideo> unitList, Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_video_unit, null);
            tempHolder = new ViewHolder();
            tempHolder.ivPlay = convertView.findViewById(R.id.iv_play_icon);
            tempHolder.tvCricle = convertView.findViewById(R.id.tv_circle);
            tempHolder.tvTitle = convertView.findViewById(R.id.tv_title);
            tempHolder.tvLength = convertView.findViewById(R.id.tv_time);
            tempHolder.rlUnitVideo = convertView.findViewById(R.id.rl_unit);
            convertView.setTag(tempHolder);
        } else {
            tempHolder = (ViewHolder) convertView.getTag();
        }
        tempHolder.tvTitle.setText(mUnitList.get(position).getTitle());
        tempHolder.tvLength.setText(unitVideo.getLength());
        if (unitVideo.getIspunit() == 1) {
            //是标题
            tempHolder.ivPlay.setVisibility(View.GONE);
            tempHolder.tvCricle.setVisibility(View.VISIBLE);
            tempHolder.tvTitle.setTextColor(Color.BLACK);
            tempHolder.tvLength.setVisibility(View.GONE);
        } else {
            tempHolder.tvLength.setVisibility(View.VISIBLE);
            tempHolder.tvCricle.setVisibility(View.GONE);
            tempHolder.ivPlay.setVisibility(View.VISIBLE);
            if (!unitVideo.isPlaying()) {
                tempHolder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.tv_unit_title_normal));
                tempHolder.tvLength.setTextColor(mContext.getResources().getColor(R.color.tv_unit_title_normal));
            } else {
                tempHolder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                tempHolder.tvLength.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            }
        }
        return convertView;
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
        private TextView tvCricle, tvTitle, tvLength;
        private ImageView ivPlay;
        private RelativeLayout rlUnitVideo;

    }

}
