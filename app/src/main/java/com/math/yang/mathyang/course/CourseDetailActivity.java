package com.math.yang.mathyang.course;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.commonsdk.http.util.OkHttpUtil;
import com.commonsdk.network.NetUtils;
import com.commonsdk.string.StringUtils;
import com.math.yang.mathyang.R;
import com.math.yang.mathyang.fragment.BookCommentFragment;
import com.math.yang.mathyang.fragment.BookIntroFragment;
import com.math.yang.mathyang.model.BookTerm;
import com.math.yang.mathyang.model.UserData;
import com.math.yang.mathyang.util.Constant;
import com.math.yang.mathyang.util.FileUtil;
import com.math.yang.mathyang.util.JsonParseUtil;
import com.math.yang.mathyang.util.LL;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressLint("NewApi")
public class CourseDetailActivity extends FragmentActivity implements ScrollTabHolder, ViewPager.OnPageChangeListener, View.OnClickListener, BuyClickCallBack {
    public static final boolean NEEDS_PROXY = Integer.valueOf(Build.VERSION.SDK_INT).intValue() < 11;
    private View mHeader;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private RelativeLayout rlTitle;
    private int mMinHeaderHeight;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;
    private int mLastY;
    private TextView tvBuyNow, tvStudy, tvShare, tvNewPrice, tvOldPrice, tvSchool, tvTeacher, tvTeacher1, tvLevel;
    private TextView tvTitle, tvRefresh;
    private ImageView ivBack;
    private TextView tvBoughtCount, tvStudyCount;
    private ImageView ivSpecial, ivCanQuit;
    private String mCourseId;
    private String mClasscode;
    private BookTerm mBookTerm;
    private ImageView ivHeadTeacher, ivBookCover, ivOffline;
    private TextView tvCourseDetail;
    private LinearLayout llFreeService, llPayService, llPrice;
    private UserData mUser;
    private TextView tvDownloadVideo;

    @Override
    public void onBuyClick() {
    }


    public interface LoadIndexListener {
        void onLoadIndexSuccess(String courseId, BookTerm course);

    }

    public interface OnBuyBookSuccessListener {
        void onBookBuySuccess();

    }

    public interface LoadCourseListener {
        void loadCourseOk(BookTerm course);
    }

    private LoadIndexListener loadIndexListener;
    private LoadCourseListener loadCourseListener;
    private OnBuyBookSuccessListener onBuyBookListener;

    public void setOnBuyBookListener(OnBuyBookSuccessListener onBuyBookListener) {
        this.onBuyBookListener = onBuyBookListener;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof LoadIndexListener) {
            this.loadIndexListener = (LoadIndexListener) fragment;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkIndex(false);
                }
            }, 1000);
        }
        if (fragment instanceof LoadCourseListener) {
            LL.ztx("onAttach简介");
            this.loadCourseListener = (LoadCourseListener) fragment;
            if (mBookTerm != null) {
                loadCoureseOk();
            }
        }
        if (fragment instanceof OnBuyBookSuccessListener) {
            this.onBuyBookListener = (OnBuyBookSuccessListener) fragment;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.course_banner_height);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.course_banner_height);
        mMinHeaderTranslation = -mMinHeaderHeight;
        setContentView(R.layout.activity_main_v);
        mHeader = findViewById(R.id.header);
        rlTitle = (RelativeLayout) findViewById(R.id.rl_title);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        initFragment();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(3);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.setTabHolderScrollingContent(this);
        mViewPager.setAdapter(mPagerAdapter);
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(this);
        initViews();
        initData();
        loadData();
        mLastY = 0;
    }

    private void loadData() {

    }


    /**
     * 课程
     */
    private Handler courseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case OkHttpUtil.Success:
                    LL.ztx("课程" + msg.obj.toString());
                    onColumnLoadSuucess(msg.obj.toString());
                    break;
                case OkHttpUtil.Faile:
                    showToast("加载失败，请检查网络后重试");
                    break;
            }
        }
    };

    private void showToast(String s) {
        Toast.makeText(CourseDetailActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    private void onColumnLoadSuucess(String s) {

    }

    private void loadCoureseOk() {
        if (loadCourseListener != null) {
            loadCourseListener.loadCourseOk(mBookTerm);
        }
    }

    private void onIndexLoadOk(String index, boolean isLocal) {
        //目录加载成功
        tvDownloadVideo.setVisibility(mUser.isManager() ? View.VISIBLE : View.GONE);
        tvDownloadVideo.setVisibility(View.GONE);
        if (loadIndexListener != null) {
            loadIndexListener.onLoadIndexSuccess(mBookTerm.getId(), mBookTerm);
        }
    }
    /**
     * 加载目录
     */
    private void loadIndex() {
        LL.ztx("下载目录");
        String url = "";
        if (mBookTerm.getIndexUrl().startsWith("http")) {
            url = mBookTerm.getIndexUrl();
        } else {
            url = Constant.FTPIP + mBookTerm.getIndexUrl();
        }
        OkHttpUtil.downloadFile(this, indexHandler, url, FileUtil.LOCAL_INDEX_PATH, mBookTerm.getId() + FileUtil.INDEX_PATTERN + ".temp", false);
    }

    private Handler indexHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case OkHttpUtil.Success:
                    File file = new File(String.valueOf(msg.obj));
                    File indexFile = new File(FileUtil.getCourseIndexFile(mBookTerm.getId()));
                    try {
                        if (!indexFile.exists()) {
                            indexFile.createNewFile();
                        }
                        if (!FileUtil.compareFileContentSame(String.valueOf(msg.obj), FileUtil.getCourseIndexFile(mBookTerm.getId()))) {
                            file.renameTo(indexFile);
                            checkIndex(true);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case OkHttpUtil.Faile:
                    break;
            }
        }
    };
    private float money = 0;

    private void setData() {
        showViews(true);

    }

    /**
     * @param newDownload 刚刚下载成功
     */
    private void checkIndex(boolean newDownload) {

    }



    private void initData() {


    }

    /**
     * view初始化
     */
    private void initViews() {

    }

    private void showViews(boolean isShow) {

    }

    private List<ScrollTabHolderFragment> mFragmentList = new ArrayList<>();

    private void initFragment() {
        ScrollTabHolderFragment fragment = (ScrollTabHolderFragment) SampleListFragment.newInstance(0);
        ScrollTabHolderFragment fragment1 = BookIntroFragment.newInstance(1);
        ScrollTabHolderFragment fragment2 =  BookCommentFragment.newInstance(2);
        mFragmentList.add(fragment);
        mFragmentList.add(fragment1);
        mFragmentList.add(fragment2);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // nothing
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffsetPixels > 0) {
            int currentItem = mViewPager.getCurrentItem();
            SparseArrayCompat<ScrollTabHolder> scrollTabHolders = mPagerAdapter.getScrollTabHolders();
            ScrollTabHolder currentHolder;
            if (position < currentItem) {
                currentHolder = scrollTabHolders.valueAt(position);
            } else {
                currentHolder = scrollTabHolders.valueAt(position + 1);
            }
            if (NEEDS_PROXY) {
                // TODO is not good
                currentHolder.adjustScroll(mHeader.getHeight() - mLastY);
                mHeader.postInvalidate();
            } else {
                if (currentHolder != null) {
                    currentHolder.adjustScroll((int) (mHeader.getHeight() + mHeader.getTranslationY()));
                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        SparseArrayCompat<ScrollTabHolder> scrollTabHolders = mPagerAdapter.getScrollTabHolders();
        ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);
        if (NEEDS_PROXY) {
            //TODO is not good
            if (currentHolder != null) {
                currentHolder.adjustScroll(mHeader.getHeight() - mLastY);
                mHeader.postInvalidate();
            }
        } else {
            if (currentHolder != null) {
                currentHolder.adjustScroll((int) (mHeader.getHeight() + mHeader.getTranslationY()));
            }
        }
    }

    @Override
    public void onScroll(int distance, int pagePosition, int firstVisiblePosition) {
        if (mViewPager.getCurrentItem() == pagePosition) {
            TextView tv;
            if (firstVisiblePosition > 0) {
                distance += mHeaderHeight;
            }
            int scrollY = distance;
            int translationY = Math.max(-scrollY, mMinHeaderTranslation);
            int titleAndTripHeight = (int) (getResources().getDimension(R.dimen.course_tab_height)) + (int) getResources().getDimension(R.dimen.title_height);
            if (NEEDS_PROXY) {
                //TODO is not good
                mLastY = -Math.max(-scrollY, mMinHeaderTranslation);
                mHeader.scrollTo(0, mLastY);
                mHeader.postInvalidate();
            } else {
                if (-translationY + titleAndTripHeight >= mHeaderHeight) {
                    mHeader.setTranslationY(titleAndTripHeight - mHeaderHeight);
                } else {
                    mHeader.setTranslationY(translationY);
                }
            }
            int bannerHeight = (int) getResources().getDimension(R.dimen.banner_height);
            if (distance == 0) {
                rlTitle.getBackground().setAlpha(0);
            } else if (distance + titleAndTripHeight <= bannerHeight) {
                rlTitle.getBackground().setAlpha((int) (((float) distance / bannerHeight) * 255));
            } else {
                rlTitle.getBackground().setAlpha(255);
            }
        }
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        // nothing
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private int clickCount = 0;



    int articleCount = 0;




    /**
     * 显示加载框
     *
     * @param s
     */
    private void showLoadingDialog(String s) {
        mDialog = new MaterialDialog.Builder(this)
                .title(s)
                .progress(true, 0)
                .show();
    }





    MaterialDialog mDialog;

    private void addCourse() {
        mDialog = new MaterialDialog.Builder(this)
                .title("添加课程")
                .progress(true, 0)
                .show();
    }


    public class PagerAdapter extends FragmentPagerAdapter {
        private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders;
        private final String[] TITLES = {"目录", "简介", "评价"};
        private ScrollTabHolder mListener;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            mScrollTabHolders = new SparseArrayCompat<ScrollTabHolder>();
        }

        public void setTabHolderScrollingContent(ScrollTabHolder listener) {
            mListener = listener;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            ScrollTabHolderFragment fragment = mFragmentList.get(position);
            mScrollTabHolders.put(position, fragment);
            if (mListener != null) {
                fragment.setScrollTabHolder(mListener);
            }
            return fragment;
        }

        public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
            return mScrollTabHolders;
        }

    }
}
