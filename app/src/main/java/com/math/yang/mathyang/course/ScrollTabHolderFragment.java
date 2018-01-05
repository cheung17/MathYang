package com.math.yang.mathyang.course;

import com.commonsdk.application.BaseFragment;

public abstract class ScrollTabHolderFragment extends BaseFragment implements ScrollTabHolder {

	protected ScrollTabHolder mScrollTabHolder;

	public void setScrollTabHolder(ScrollTabHolder scrollTabHolder) {
		mScrollTabHolder = scrollTabHolder;
	}

}