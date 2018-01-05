package com.math.yang.mathyang.course;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;


import com.math.yang.mathyang.R;
import com.math.yang.mathyang.model.BookTerm;
import com.math.yang.mathyang.model.OrgBean;
import com.math.yang.mathyang.util.ViewScrollUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SampleListFragment extends ScrollTabHolderFragment implements CourseDetailActivity.LoadIndexListener {
    private static final String ARG_POSITION = "position";
    private ListView mListView;
    private ArrayList<String> mListItems;
    private int mPosition;
    private List<OrgBean> mIndexList = new ArrayList<>();
    private boolean isBookBought = false;
    private Context mContext;

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
        View placeHolderView = inflater.inflate(R.layout.view_header_placeholder, mListView, false);
        placeHolderView.setBackgroundColor(0xFFFFFFFF);
        mListView.addHeaderView(placeHolderView);
        initData();
        loadIndex();
        return v;
    }



    private void setTreeView() {

    }

    private void initData() {


    }


    private BuyClickCallBack onBuyClickListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CourseDetailActivity) {
            this.onBuyClickListener = (BuyClickCallBack) activity;
        }
    }




    private void buyBook() {
        if (onBuyClickListener != null) {
            onBuyClickListener.onBuyClick();
        }
    }

    private void showToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    private void loadIndex() {

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
    public void onLoadIndexSuccess(String courseId, BookTerm course) {

    }


    public class OnScroll implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (mScrollTabHolder != null)
                mScrollTabHolder.onScroll(ViewScrollUtil.getListViewScrollDistance(view), mPosition, mListView.getFirstVisiblePosition());
        }

    }

    @Override
    public void onScroll(int distance,
                         int pagePosition, int firstVisiblePosition) {
    }

}