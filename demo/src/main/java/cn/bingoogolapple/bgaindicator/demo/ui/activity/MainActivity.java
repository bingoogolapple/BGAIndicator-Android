package cn.bingoogolapple.bgaindicator.demo.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;
import cn.bingoogolapple.bgaannotation.BGAAView;
import cn.bingoogolapple.bgaindicator.BGAFixedIndicator;
import cn.bingoogolapple.bgaindicator.demo.R;
import cn.bingoogolapple.bgaindicator.demo.ui.fragment.ContentFragment;

@BGAALayout(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BGAAView(R.id.indicator1)
    private BGAFixedIndicator mIndicator1;
    @BGAAView(R.id.pager1)
    private ViewPager mPager1;

    @BGAAView(R.id.indicator2)
    private BGAFixedIndicator mIndicator2;
    @BGAAView(R.id.pager2)
    private ViewPager mPager2;

    @BGAAView(R.id.indicator3)
    private BGAFixedIndicator mIndicator3;
    @BGAAView(R.id.pager3)
    private ViewPager mPager3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);
        String[] titles1 = new String[3];
        for (int i = 0; i < titles1.length; i++) {
            titles1[i] = "标签" + i;
        }
        mPager1.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), titles1));
        mIndicator1.initData(0, mPager1);

        String[] titles2 = new String[5];
        for (int i = 0; i < titles2.length; i++) {
            titles2[i] = "标签" + i;
        }
        mPager2.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), titles2));
        mIndicator2.initData(1, mPager2);

        String[] titles3 = new String[5];
        for (int i = 0; i < titles3.length; i++) {
            titles3[i] = "标签" + i;
        }
        mPager3.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), titles2));
        mIndicator3.initData(1, mPager3);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private String[] mTitles;

        public MyPagerAdapter(FragmentManager fm, String[] titles) {
            super(fm);
            mTitles = titles;
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            return ContentFragment.newInstance(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }

}