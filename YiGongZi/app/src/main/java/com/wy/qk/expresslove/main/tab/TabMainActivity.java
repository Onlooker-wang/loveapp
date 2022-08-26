package com.wy.qk.expresslove.main.tab;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;

import com.wy.designtablayout.TabLayoutBuilder;
import com.wy.qk.expresslove.R;
import com.wy.qk.expresslove.base.CommonAudioActivity;

import java.util.ArrayList;
import java.util.List;

public class TabMainActivity extends CommonAudioActivity {
    private static final String TAG = "TabMainActivity";

    //Tab count
    private int mTabCount;
    private TabLayoutBuilder mTabLayoutBuilder;
    private TabFragmentAdapter mTabFragmentAdapter;
    private FragmentManager mFragmentManager;
    private ViewPager mViewPager;
    private Resources mResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_main);
        initView();
    }

    private void initView() {
        mResources = getResources();
        mFragmentManager = getSupportFragmentManager();
        mTabFragmentAdapter = new TabFragmentAdapter(mFragmentManager);
        //init viewpager
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mTabFragmentAdapter);
        //inflate TabLayoutBuilder
        mTabLayoutBuilder = (TabLayoutBuilder) findViewById(R.id.tab_layout);
        //Tab count
        mTabCount = 3;
        //The textColor
        final int[] textColor = {0xff333333, 0xffff8800};
        //The Image res id
        int[] resId = {
                R.drawable.heart_selector,
                R.drawable.ufo_selector,
                //R.drawable.diamond_selector,
                R.drawable.mine_selector
        };
        //The title
        String[] title = {
                mResources.getString(R.string.tab_heart),
                mResources.getString(R.string.tab_photo),
                //mResources.getString(R.string.tab_tool),
                mResources.getString(R.string.tab_setting),
        };


        //init TabLayout
        mTabLayoutBuilder.setupWithViewPager(mViewPager);//setting up this TabLayout with ViewPager
        mTabLayoutBuilder.setBottomMargin(2);//set the bottomMargin --unit:dp
        mTabLayoutBuilder.setTextSize(12);//set title size --unit:sp
        //add tab to TabLayout
        for (int i = 0; i < mTabCount; i++) {
            mTabLayoutBuilder.addTab(new TabLayoutBuilder.ItemStatus(title[i], resId[i], textColor[0], textColor[1]));
        }
        //show tabView to your screen
        mTabLayoutBuilder.build();
    }


    private static class TabFragmentAdapter extends FragmentPagerAdapter {
        private List<PagerFragment> mFragments = new ArrayList<>();

        public TabFragmentAdapter(FragmentManager fm) {
            super(fm);
            mFragments.add(new HeartFragment());
            mFragments.add(new PhotoFragment());
            //mFragments.add(new ToolFragment());
            mFragments.add(new SettingFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    //1.触摸事件接口
    public interface MyOnTouchListener {
        boolean onTouch(MotionEvent ev);
    }

    //2. 保存MyOnTouchListener接口的列表
    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>();

    //3.分发触摸事件给所有注册了MyOnTouchListener的接口
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    //4.提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    //5.提供给Fragment通过getActivity()方法来注销自己的触摸事件的方法
    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }
}