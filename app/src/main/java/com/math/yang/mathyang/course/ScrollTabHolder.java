package com.math.yang.mathyang.course;

public interface ScrollTabHolder {

	void adjustScroll(int scrollHeight);

	void onScroll(int distance, int pagePosition, int firstVisiblePosition);
}
