package com.math.yang.mathyang.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.commonsdk.custome.recycleview.RecyclerHolder;


/**
 * @author ztx
 *         将脚布局放在最后一个条目,当加载更多的时候显示,加载完成的时候隐藏
 */
public class RefreshRecyclerView extends RecyclerView {

    private AutoLoadAdapter autoLoadAdapter;

    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public interface onScrollCallBack {
        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }

    private boolean isLoadingMore = false;//是否正在加载更多
    private OnLoadMoreListener loadMoreListener;//加载数据监听
    private boolean loadMoreEnable = true;//是否允许加载更多
    private int footerResource = -1;//脚布局
    private boolean footer_visible = false;//脚部是否可以见
    private onScrollCallBack onScrollCallBack;//滑动监听

    public void setOnScrollCallBack(RefreshRecyclerView.onScrollCallBack onScrollCallBack) {
        this.onScrollCallBack = onScrollCallBack;
    }

    private void init() {
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (onScrollCallBack != null) {
                    onScrollCallBack.onScrolled(recyclerView, dx, dy);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (getAdapter() != null && getLayoutManager() != null) {
                    int lastVisiblePosition = 0;
                    LayoutManager layoutManager = getLayoutManager();
                    if (layoutManager instanceof GridLayoutManager) {
                        lastVisiblePosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                        ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                        lastVisiblePosition = findMax(into);
                    } else {
                        lastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    }

                    int itemCount = layoutManager.getItemCount();
                    if (distanceY < 0 && itemCount > 1 && lastVisiblePosition == itemCount - 1 && !isLoadingMore && loadMoreEnable && newState == 1) {
                        Log.i("test", "加载更多");
                        //正在加载更多
                        loading();
                        if (footerResource != -1) {//有脚布局
                            //显示脚布局
                            footer_visible = true;
                            getAdapter().notifyItemChanged(itemCount - 1);
                        }
                        if (loadMoreListener != null) {
                            loadMoreListener.loadMoreListener();
                        }
                    }
                }
            }
        });
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 判断滑动方向
     */
    private float distanceY = 0;
    float startY = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float y = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                distanceY = y - startY;
                startY = y;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setAdapter(Adapter adapter) {
      /* SlideInBottomAnimationAdapter slideInBottomAnimationAdapter = new SlideInBottomAnimationAdapter(adapter);
        slideInBottomAnimationAdapter.setDuration(600);*/
        autoLoadAdapter = new AutoLoadAdapter(adapter);//添加动画
        super.setAdapter(autoLoadAdapter);
    }

    /**
     * 设置是否允许加载更多
     *
     * @param isEnable
     */
    public void setLoadMoreEnable(boolean isEnable) {
        this.loadMoreEnable = isEnable;
    }

    /**
     * 设置脚布局
     */
    public void setFooterResource(int footerResource) {
        this.footerResource = footerResource;
    }


    /**
     * 加载完成
     */
    public void loadMoreComplete() {
        this.isLoadingMore = false;
        if (footerResource != -1 && loadMoreEnable) {
            //隐藏脚布局
            footer_visible = false;

            getAdapter().notifyDataSetChanged();
        }


    }

    /**
     * 正在刷新
     */
    private void loading() {
        this.isLoadingMore = true;//设置正在刷新
    }

    /**
     * 加载更多数据回调
     *
     * @param listener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.loadMoreListener = listener;
    }

    public interface OnLoadMoreListener {
        void loadMoreListener();//上拉刷新
    }


    /**
     * 刷新数据
     */
    public void notifyData() {
        if (getAdapter() != null) {
            loadMoreComplete();
            if (footerResource != -1 && loadMoreEnable) {
                //隐藏脚布局
                footer_visible = false;
            }
            getAdapter().notifyDataSetChanged();

        }
    }

    public class AutoLoadAdapter extends RecyclerView.Adapter<RecyclerHolder> {
        private Adapter dataAdapter;//数据adapter
        private final int TYPE_FOOTER = Integer.MAX_VALUE;//底部布局

        public AutoLoadAdapter(RecyclerView.Adapter adapter) {
            this.dataAdapter = adapter;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1 && loadMoreEnable && footerResource != -1 && footer_visible) {
                return TYPE_FOOTER;
            }
            if (dataAdapter.getItemViewType(position) == TYPE_FOOTER) {
                throw new RuntimeException("adapter中itemType不能为:" + Integer.MAX_VALUE);
            }
            return dataAdapter.getItemViewType(position);
        }

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = null;
            if (viewType == TYPE_FOOTER) {//脚部
                holder = new FooterViewHolder(LayoutInflater.from(getContext()).inflate(footerResource, parent, false));
            } else {//数据
                holder = (RecyclerHolder) dataAdapter.onCreateViewHolder(parent, viewType);
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            int itemViewType = getItemViewType(position);
            if (itemViewType != TYPE_FOOTER) {
                dataAdapter.onBindViewHolder(holder, position);
            }
        }

        @Override
        public void onViewAttachedToWindow(RecyclerHolder holder) {
            super.onViewAttachedToWindow(holder);

            dataAdapter.onViewAttachedToWindow(holder);
        }


        @Override
        public int getItemCount() {
            if (dataAdapter.getItemCount() != 0) {
                int count = dataAdapter.getItemCount();
                if (loadMoreEnable && footerResource != -1 && footer_visible) {
                    count++;
                }
                return count;
            }
            return 0;
        }

        public class FooterViewHolder extends RecyclerHolder {

            public FooterViewHolder(View itemView) {
                super(itemView);
            }
        }

    }

}
