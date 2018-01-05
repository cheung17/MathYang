package com.math.yang.mathyang.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.commonsdk.application.BaseFragment;
import com.math.yang.mathyang.R;
import com.math.yang.mathyang.course.ScrollTabHolderFragment;

/**
 * Created by zhangtx on 2018/1/4.
 */

public class BookCommentFragment extends ScrollTabHolderFragment {


    private Context mContext;
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = super.getContext();
        View v = inflater.inflate(R.layout.fragment_comment, null);
        mListView = (ListView) v.findViewById(R.id.listView);
        View placeHolderView = inflater.inflate(R.layout.view_header_placeholder, mListView, false);
        placeHolderView.setBackgroundColor(0xFFFFFFFF);
        mListView.addHeaderView(placeHolderView);
        initData();
        return v;
    }

    private void initData() {
    }

    @Override
    public void adjustScroll(int scrollHeight) {

    }

    @Override
    public void onScroll(int distance, int pagePosition, int firstVisiblePosition) {

    }

    public static BookCommentFragment newInstance(int what) {

        Bundle args = new Bundle();

        BookCommentFragment fragment = new BookCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
