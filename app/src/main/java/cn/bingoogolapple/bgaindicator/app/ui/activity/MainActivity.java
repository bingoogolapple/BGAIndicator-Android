package cn.bingoogolapple.bgaindicator.app.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgaindicator.app.R;
import cn.bingoogolapple.bgaindicator.app.ui.fragment.ContentFragment;
import cn.bingoogolapple.bgaindicator.library.BGAFixedIndicator;
import cn.bingoogolapple.bgaindicator.library.BGAScrollIndicator;
import cn.bingoogolapple.bgaindicator.library.TabInfo;
import cn.bingoogolapple.loon.library.Loon;
import cn.bingoogolapple.loon.library.LoonLayout;
import cn.bingoogolapple.loon.library.LoonView;

@LoonLayout(id = R.layout.activity_main)
public class MainActivity extends FragmentActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @LoonView(id = R.id.indicator1)
    private BGAFixedIndicator mIndicator1;
    @LoonView(id = R.id.pager1)
    private ViewPager mPager1;

    @LoonView(id = R.id.indicator2)
    private BGAScrollIndicator mIndicator2;
    @LoonView(id = R.id.pager2)
    private ViewPager mPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Loon.injectView2Activity(this);
        List<TabInfo> tabInfos1 = new ArrayList<TabInfo>();
        for(int i = 1; i< 5;i++) {
            tabInfos1.add(new TabInfo("标签" + i));
        }
        mPager1.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),tabInfos1));
        mIndicator1.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.i(TAG,"newTab:" + position);
            }
        });
        mIndicator1.initData(0,tabInfos1,mPager1);

        List<TabInfo> tabInfos2 = new ArrayList<TabInfo>();
        for(int i = 1; i< 6;i++) {
            tabInfos2.add(new TabInfo("标签" + i));
        }
        mPager2.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),tabInfos2));
        mIndicator2.initData(1,tabInfos2,mPager2);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private List<TabInfo> mTabInfos;

        public MyPagerAdapter(FragmentManager fm,List<TabInfo> tabInfos) {
            super(fm);
            mTabInfos = tabInfos;
        }

        @Override
        public int getCount() {
            return mTabInfos.size();
        }

        @Override
        public Fragment getItem(int position) {
            return ContentFragment.newInstance(position + 1);
        }

    }

}