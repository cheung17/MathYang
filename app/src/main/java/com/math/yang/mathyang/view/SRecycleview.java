package com.math.yang.mathyang.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author ztx
 */
public class SRecycleview extends RefreshRecyclerView {
    private LinearLayoutManager layoutManager;

    public SRecycleview(Context context) {
        super(context);
        init();
    }

    public SRecycleview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public SRecycleview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();

    }



    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);

    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
    }

    private void init() {
        // this.setOnScrollListener(onScrollListener);
    }


    public int getScollYDistance(LinearLayoutManager layoutManager) {
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }
}
