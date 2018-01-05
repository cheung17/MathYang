package com.math.yang.mathyang.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;

/**
 * @author ztx
 */
public class ViewScrollUtil {
    /**
     * 获得Recycleview的滑动距离
     *
     * @param layoutManager 必须是LinearLayoutManager
     * @param recyclerView  计算的recycleview
     * @return 返回滑动距离
     */
    public static int getRecycleViewScorllDistance(LinearLayoutManager layoutManager, RecyclerView recyclerView) {

        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    /**
     * 获得Listview的滑动距离
     *
     * @param listView
     * @return 返回滑动的距离
     */
    public static int getListViewScrollDistance(AbsListView listView) {
        View c = listView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }
}
