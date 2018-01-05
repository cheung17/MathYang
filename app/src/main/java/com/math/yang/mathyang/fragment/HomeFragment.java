package com.math.yang.mathyang.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsdk.application.BaseFragment;
import com.commonsdk.custome.recycleview.QuickRecycleAdapter;
import com.commonsdk.custome.recycleview.RecyclerHolder;
import com.commonsdk.custome.recycleview.ViewHolder;
import com.commonsdk.dateandtime.DateAndTimeUtil;
import com.commonsdk.dateandtime.RelativeDateTime;
import com.commonsdk.http.util.OkHttpUtil;
import com.commonsdk.network.NetUtils;
import com.commonsdk.string.StringUtils;
import com.math.yang.mathyang.R;
import com.math.yang.mathyang.activity.BookDetailActivity;
import com.math.yang.mathyang.course.CourseDetailActivity;
import com.math.yang.mathyang.model.BookTerm;
import com.math.yang.mathyang.util.Constant;
import com.math.yang.mathyang.util.JsonParseUtil;
import com.math.yang.mathyang.util.LL;
import com.math.yang.mathyang.view.RefreshRecyclerView;
import com.math.yang.mathyang.view.RoundImageView;
import com.math.yang.mathyang.view.SRecycleview;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ztx
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private SRecycleview mRecycleview;
    private QuickRecycleAdapter<BookTerm> mAdapter;
    private List<BookTerm> mBookList = new ArrayList<>();
    private RelativeLayout titleRl;
    private ImageView ivBack;
    private TextView tvTitle, tvRefresh, ivWrite;
    private SwipeRefreshLayout mSwipeRefresh;
    private int mOffset = 1;
    private LinearLayoutManager mLayoutmanager;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = super.getContext();
        initView(rootView);
        initRecycleView(rootView);
        initRefresh();
        initData();
        return rootView;
    }

    private void showNoNet() {
        showToast("请检查网络");
        mSwipeRefresh.setRefreshing(false);
        mRecycleview.loadMoreComplete();
    }

    private void initRefresh() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getContext() == null) {
                    return;
                }
                if (getContext() == null || !NetUtils.isConnected(getContext())) {
                    showNoNet();
                    return;
                }
                mOffset = 1;
                mRecycleview.setLoadMoreEnable(false);
                loadData(1);
            }
        });
        mRecycleview.setLoadMoreEnable(true);
        mRecycleview.setOnLoadMoreListener(new RefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMoreListener() {
                if (getContext() == null || !NetUtils.isConnected(getContext())) {
                    showNoNet();
                    return;
                }
                mOffset = mOffset + 1;
                mSwipeRefresh.setEnabled(false);
                loadData(mOffset);
            }
        });
        mRecycleview.setFooterResource(R.layout.listview_footer);
    }

    /**
     * 加载数据
     */
    private void initData() {
        tvTitle.setText("首页");
        titleRl.getBackground().setAlpha(255);//toolbar透明度初始化为0
        //暂时只有语文有数据
        mSwipeRefresh.setRefreshing(true);
        mRecycleview.setLoadMoreEnable(false);
        mAdapter.list = mBookList;
        mRecycleview.notifyData();
        if (getContext() == null || !NetUtils.isConnected(getContext())) {
            showNoNet();
            return;
        }
        loadData(1);
    }


    /**
     * 加载数据
     *
     * @param offset 索引
     */
    private void loadData(int offset) {
        Map<String, String> map = new HashMap<>();

        if (getActivity() == null) {
            return;
        }
        OkHttpUtil.getParam(getActivity(), columnHandler, map, Constant.URL_GET_ALL_BOOKS, false);
    }

    /**
     * 加载列表Handler
     */
    private Handler columnHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loadFinished();
            switch (msg.what) {
                case OkHttpUtil.Success:
                    LL.ztx("Success " + msg.obj.toString());
                    onColumnLoadSuucess(msg.obj.toString());
                    break;
                case OkHttpUtil.Faile:
                    if (mOffset > 1) {
                        mOffset--;
                    }
                    break;
            }
        }
    };

    private void loadFinished() {
        mSwipeRefresh.setRefreshing(false);
        mRecycleview.loadMoreComplete();
        mRecycleview.setLoadMoreEnable(true);
        mSwipeRefresh.setEnabled(true);
    }

    private void onColumnLoadSuucess(String s) {
        mBookList = JsonParseUtil.parseBookList(s);
        //mBookList.addAll(mBookList);
        //mBookList.addAll(mBookList);
        //mBookList.addAll(mBookList);
        mAdapter.list = mBookList;
        mRecycleview.notifyData();
    }

    private void showToast(String str) {
        if (getContext() != null) {
            Toast.makeText(getContext(), str + "", Toast.LENGTH_SHORT).show();
        }
    }

    //   private LinearLayoutManager staggeredGridLayoutManager;

    /**
     * recycleview初始化
     */
    private void initRecycleView(View rootView) {
        //  LinearLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,
        //     StaggeredGridLayoutManager.VERTICAL);
        mRecycleview = (SRecycleview) rootView.findViewById(R.id.recycleview);
        mLayoutmanager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecycleview.setLayoutManager(mLayoutmanager);
        mAdapter = new QuickRecycleAdapter<BookTerm>(mContext, R.layout.item_book_term, R.layout.item_banner, R.layout.include_title) {
            @Override
            public int getItemViewType(int position) {
                return mAdapter.list.get(position).getShowtype();
            }

            @Override
            public void onViewAttachedToWindow(RecyclerHolder holder) {
                super.onViewAttachedToWindow(holder);
                //设置全屏
                holder.itemView.getLayoutParams().width = -1;
            }

            @Override
            public void onBindViewHolder(final ViewHolder holder, final int position) {
                BookTerm bookTerm = mAdapter.list.get(position);
                switch (bookTerm.getShowtype()) {
                    case BookTerm.TYPE_BOOK:
                        holder.text(R.id.tv_book_name).setText(bookTerm.getName());
                        //  holder.text(R.id.tv_class_hours).setText(bookTerm.getClasshour());
                        holder.text(R.id.tv_learn_people).setText(bookTerm.getBoughtcount() + "人正在学习");
                        holder.text(R.id.tv_price).setText("￥" + bookTerm.getMoney() + "");
                        ImageLoader.getInstance().displayImage(bookTerm.getCoverurl(), holder.image(R.id.iv_avatar));
                        RoundImageView iv = (RoundImageView) holder.image(R.id.iv_avatar);
                        iv.setRadius(10);
                        break;
                    case BookTerm.TYPE_BANNER:
                        ImageLoader.getInstance().displayImage(bookTerm.getBannercoverurl(), holder.image(R.id.banner_iv));
                        holder.text(R.id.banner_title).setText(bookTerm.getName());
                        break;
                }
            }
        };
        mRecycleview.setOnScrollCallBack(new RefreshRecyclerView.onScrollCallBack() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int distance = mRecycleview.
                        getScollYDistance(mLayoutmanager);
                int bannerHeight = (int) mContext.getResources().getDimension(R.dimen.banner_height);
                if (distance == 0) {
                    titleRl.getBackground().setAlpha(0);
                } else if (distance <= bannerHeight) {
                    titleRl.getBackground().setAlpha((int) (((float) distance / bannerHeight) * 255));
                } else {
                    titleRl.getBackground().setAlpha(255);
                }
            }
        });
        mAdapter.setOnItemClickListener(new QuickRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mBookList.get(position).getShowtype() == BookTerm.TYPE_BOOK) {
                    Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
                    intent.putExtra(BookDetailActivity.EXTRA_BOOK, mBookList.get(position));
                    startActivity(intent);
                } else if (mBookList.get(position).getShowtype() == BookTerm.TYPE_BANNER) {

                }
            }
        });
        mRecycleview.setAdapter(mAdapter);
    }


    /**
     * 初始化View
     *
     * @param rootView 根view
     */
    private void initView(View rootView) {
        titleRl = (RelativeLayout) rootView.findViewById(R.id.rl_title);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        ivBack = (ImageView) rootView.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        tvRefresh = (TextView) rootView.findViewById(R.id.tv_right);
        tvRefresh.setVisibility(View.GONE);
        mSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        titleRl.getBackground().setAlpha(0);//toolbar透明度初始化为0
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
        }
    }
}
