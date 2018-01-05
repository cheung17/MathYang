package com.math.yang.mathyang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.commonsdk.application.BaseFragment;
import com.math.yang.mathyang.R;

/**
 * Created by zhangtx on 2018/1/2.
 */

public class DownloadFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);

        return view;
    }
}
