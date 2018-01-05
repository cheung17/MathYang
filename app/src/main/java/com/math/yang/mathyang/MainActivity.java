package com.math.yang.mathyang;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.commonsdk.application.BaseActivity;
import com.math.yang.mathyang.fragment.DownloadFragment;
import com.math.yang.mathyang.fragment.HomeFragment;
import com.math.yang.mathyang.fragment.MyCourseFragment;

public class MainActivity extends BaseActivity {

    protected static final String TAG = "MainActivity";
    private Button[] mTabs;
    private Fragment mDownloadFragment;
    private Fragment mHomeFragment;
    private Fragment mMineFragment;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMineFragment = new MyCourseFragment();
        mDownloadFragment = new DownloadFragment();
        mHomeFragment = new HomeFragment();
        fragments = new Fragment[]{mHomeFragment, mMineFragment, mDownloadFragment};
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mHomeFragment)
                .add(R.id.fragment_container, mMineFragment).hide(mDownloadFragment).hide(mMineFragment).show(mHomeFragment)
                .commit();
        initView();
    }

    /**
     * init views
     */
    private void initView() {
        mTabs = new Button[3];
        mTabs[0] = (Button) findViewById(R.id.btn_conversation);
        mTabs[1] = (Button) findViewById(R.id.btn_address_list);
        mTabs[2] = (Button) findViewById(R.id.btn_setting);
        mTabs[0].setSelected(true);
    }

    /**
     * on tab clicked
     *
     * @param view
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_conversation:
                index = 0;
                break;
            case R.id.btn_address_list:
                index = 1;
                break;
            case R.id.btn_setting:
                index = 2;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // set current tab selected
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStop() {

        super.onStop();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
