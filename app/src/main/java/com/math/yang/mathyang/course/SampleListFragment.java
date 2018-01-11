package com.math.yang.mathyang.course;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.math.yang.mathyang.R;
import com.math.yang.mathyang.activity.SelectDownlaodActivity;
import com.math.yang.mathyang.adapter.UnitVideodapter;
import com.math.yang.mathyang.db.SharedUtil;
import com.math.yang.mathyang.model.BookTerm;
import com.math.yang.mathyang.model.OrgBean;
import com.math.yang.mathyang.model.UnitVideo;
import com.math.yang.mathyang.orm.UnitDbUtil;
import com.math.yang.mathyang.util.FileUtil;
import com.math.yang.mathyang.util.JsonParseUtil;
import com.math.yang.mathyang.util.ViewScrollUtil;
import com.math.yang.mathyang.view.DownloadDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SampleListFragment extends ScrollTabHolderFragment implements CourseDetailActivity.LoadIndexListener, AdapterView.OnItemClickListener {
    private static final String ARG_POSITION = "position";
    private ListView mListView;
    private ArrayList<String> mListItems;
    private int mPosition;
    private List<OrgBean> mIndexList = new ArrayList<>();
    private boolean isBookBought = false;
    private Context mContext;
    private List<UnitVideo> mUnitList;
    private UnitVideodapter mAdapter;
    private FloatingActionButton floatDownload;

    public Context getContext() {
        return mContext;
    }


    public static Fragment newInstance(int position) {
        SampleListFragment f = new SampleListFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = super.getContext();
        View v = inflater.inflate(R.layout.fragment_tree_list, null);
        mListView = (ListView) v.findViewById(R.id.listView);
        floatDownload = v.findViewById(R.id.float_download);
        floatDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDownload();
            }
        });
        View placeHolderView = inflater.inflate(R.layout.view_header_placeholder, mListView, false);
        placeHolderView.setBackgroundColor(0xFFFFFFFF);
        mListView.addHeaderView(placeHolderView);
        mListView.setOnItemClickListener(this);
        initData();
        return v;
    }

    private void showDownload() {
       /* DownloadDialog dialog = new DownloadDialog(getActivity());
        dialog.show();*/
        Intent intent = new Intent(getActivity(), SelectDownlaodActivity.class);
        intent.putExtra(SelectDownlaodActivity.EXTRA_DOWNLOAD_LIST, (Serializable) mUnitList);
        startActivity(intent);
    }




    private void initData() {

    }


    private UnitVideoEvent unitVideoEvent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CourseDetailActivity) {
            this.unitVideoEvent = (UnitVideoEvent) activity;
        }
    }


    private void showToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView.setOnScrollListener(new OnScroll());
        if (CourseDetailActivity.NEEDS_PROXY) {//in my moto phone(android 2.1),setOnScrollListener do not work well
            mListView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mScrollTabHolder != null)
                        mScrollTabHolder.onScroll(ViewScrollUtil.getListViewScrollDistance(mListView), mPosition, mListView.getFirstVisiblePosition());
                    return false;
                }
            });
        }
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
            return;
        }

        mListView.setSelectionFromTop(1, scrollHeight);

    }

    @Override
    public void onLoadIndexSuccess(String courseId, BookTerm course, boolean isLocal) {
        String index = FileUtil.getCourseIndex(courseId);
        //;
        List<UnitVideo> list = JsonParseUtil.parseUnitList(index);
        mUnitList = list;
        UnitDbUtil.saveList(list);
        // JsonParseUtil.updateUnitList(list, mUnitList);
        mAdapter = new UnitVideodapter(mUnitList, getContext());
        mAdapter.notifyDataSetChanged();
        mListView.setAdapter(mAdapter);
        if (isLocal) {
            playPosition((int) SharedUtil.getBookPlayPosition(getContext(), mUnitList.get(0).getBookid()), false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        playPosition(position - 1, true);
    }

    /**
     * @param position 播放位置
     * @param playNow  是立马播放吗
     */
    private void playPosition(int position, boolean playNow) {
        if (mUnitList.get(position).getIspunit() == 1 || mUnitList.get(position).isPlaying()) {
            return;
        }
        for (UnitVideo unitVideo : mUnitList) {
            unitVideo.setPlaying(false);
        }
        mUnitList.get(position).setPlaying(true);
        mAdapter.notifyDataSetChanged();
        if (unitVideoEvent != null) {
            unitVideoEvent.onVideoClickListener(mUnitList.get(position), position, playNow);
        }
    }


    public class OnScroll implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if(true){
                return;
            }
            if (mScrollTabHolder != null)
                mScrollTabHolder.onScroll(ViewScrollUtil.getListViewScrollDistance(view), mPosition, mListView.getFirstVisiblePosition());
        }

    }

    @Override
    public void onScroll(int distance,
                         int pagePosition, int firstVisiblePosition) {
    }

}