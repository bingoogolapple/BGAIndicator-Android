package cn.bingoogolapple.bgaindicator.library;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bingoogolapple on 14/10/22.
 */
public class BGAScrollIndicator extends HorizontalScrollView implements View.OnClickListener, View.OnFocusChangeListener, ViewPager.OnPageChangeListener {
    private static final String TAG = BGAScrollIndicator.class.getSimpleName();
    private LinearLayout mWrapper;
    private final int BSSEEID = 0xffff00;
    private ColorStateList mTextColor;
    private int mTextSizeNormal = 12;
    private int mTextSizeSelected = 15;

    private int mTriangleColor = android.R.color.white;
    private int mTriangleHeight = 5;
    private int mTriangleMarginBottom = 2;

    private boolean mHasDivider = true;
    private int mDividerColor = android.R.color.black;
    private Drawable mDividerDrawable;
    private int mDividerWidth = 3;
    private int mDividerVerticalMargin = 10;
    private int mTabPadding = 20;

    private Paint mPaintFooterTriangle;
    private LayoutInflater mInflater;

    private ViewPager mViewPager;

    private List<TabInfo> mTabInfos;
    private int mTabCount = 0;
    private int mCurrentTabIndex = 0;

    private int mPagerScrollX = 0;

    private Path mPath = new Path();

    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    public BGAScrollIndicator(Context context) {
        this(context, null);
    }

    public BGAScrollIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BGAScrollIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);
        initDraw(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BGAIndicator);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.BGAIndicator_triangleColor) {
                mTriangleColor = typedArray.getColor(attr, mTriangleColor);
            } else if (attr == R.styleable.BGAIndicator_triangleMarginBottom) {
                /**
                 * getDimension和getDimensionPixelOffset的功能差不多,都是获取某个dimen的值,如果是dp或sp的单位,将其乘以density,如果是px,则不乘;两个函数的区别是一个返回float,一个返回int. getDimensionPixelSize则不管写的是dp还是sp还是px,都会乘以denstiy.
                 */
                mTriangleMarginBottom = typedArray.getDimensionPixelSize(attr, mTriangleMarginBottom);
            } else if (attr == R.styleable.BGAIndicator_triangleHeight) {
                mTriangleHeight = typedArray.getDimensionPixelSize(attr, mTriangleHeight);
            } else if (attr == R.styleable.BGAIndicator_textColor) {
                mTextColor = typedArray.getColorStateList(attr);
            } else if (attr == R.styleable.BGAIndicator_textSizeNormal) {
                mTextSizeNormal = typedArray.getDimensionPixelSize(attr, mTextSizeNormal);
            } else if (attr == R.styleable.BGAIndicator_textSizeSelected) {
                mTextSizeSelected = typedArray.getDimensionPixelSize(attr, mTextSizeSelected);
            } else if (attr == R.styleable.BGAIndicator_hasDivider) {
                mHasDivider = typedArray.getBoolean(attr, mHasDivider);
            } else if (attr == R.styleable.BGAIndicator_dividerColor) {
                mDividerDrawable = typedArray.getDrawable(attr);
            } else if (attr == R.styleable.BGAIndicator_dividerWidth) {
                mDividerWidth = typedArray.getDimensionPixelSize(attr, mDividerWidth);
            } else if (attr == R.styleable.BGAIndicator_dividerVerticalMargin) {
                mDividerVerticalMargin = typedArray.getDimensionPixelSize(attr, mDividerVerticalMargin);
            }
        }
        typedArray.recycle();
    }

    private void initDraw(Context context) {
        mPaintFooterTriangle = new Paint();
        mPaintFooterTriangle.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintFooterTriangle.setColor(mTriangleColor);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // 初始化选项卡
    public void initData(int currentTab, List<TabInfo> tabInfos, ViewPager viewPager) {
        this.removeAllViews();
        mViewPager = viewPager;
        mTabInfos = tabInfos;
        mTabCount = tabInfos.size();
        mViewPager.setOnPageChangeListener(this);

        initWrapper();
        initTab(currentTab);
        postInvalidate();
    }

    private void initWrapper() {
        mWrapper = new LinearLayout(getContext());
        mWrapper.setOrientation(LinearLayout.HORIZONTAL);
        mWrapper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        this.addView(mWrapper);
    }


    @SuppressWarnings("deprecation")
    private void initTab(int currentTab) {
        // 避免for循环中重复创建
        LinearLayout.LayoutParams dividerLp = null;
        if(mHasDivider) {
            dividerLp = new LinearLayout.LayoutParams(mDividerWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            dividerLp.setMargins(0, mDividerVerticalMargin, 0, mDividerVerticalMargin);
        }
        LinearLayout.LayoutParams autoMatchLp = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);;


        int position = 0;
        for (int index = 0; index < mTabCount; index++) {
            View tabIndicator = mInflater.inflate(R.layout.view_indicator, this, false);
            tabIndicator.setId(BSSEEID + index);
            tabIndicator.setOnClickListener(this);
            tabIndicator.setPadding(mTabPadding,0,mTabPadding,0);

            TextView titleTv = (TextView) tabIndicator.findViewById(R.id.tv_indicator_title);
            if (mTextColor != null) {
                titleTv.setTextColor(mTextColor);
            }
            titleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeNormal);
            titleTv.setText(mTabInfos.get(index).title);

            // 防止currentTab为0时，第一个tab文字颜色没变化
            if (index == 0) {
                resetTab(tabIndicator, true);
            }
            mWrapper.addView(tabIndicator,position,autoMatchLp);
            position++;

            if (index != mTabCount - 1 && mHasDivider) {

                View vLine = new View(getContext());
                if (mDividerDrawable != null) {
                    vLine.setBackgroundDrawable(mDividerDrawable);
                } else {
                    vLine.setBackgroundResource(mDividerColor);
                }
                mWrapper.addView(vLine,position,dividerLp);
                position++;
            }
        }
        setCurrentTab(currentTab);
    }

    private void setCurrentTab(int index) {
        if (mCurrentTabIndex != index && index > -1 && index < mTabCount) {
            View oldTab = mWrapper.findViewById(BSSEEID + mCurrentTabIndex);
            resetTab(oldTab, false);

            mCurrentTabIndex = index;
            View newTab = mWrapper.findViewById(BSSEEID + mCurrentTabIndex);
            resetTab(newTab, true);

            if (mViewPager.getCurrentItem() != mCurrentTabIndex) {
                mViewPager.setCurrentItem(mCurrentTabIndex);
            }
            postInvalidate();
        }
    }

    private void resetTab(View tab, boolean b) {
        TextView tv = (TextView) tab.findViewById(R.id.tv_indicator_title);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, b ? mTextSizeSelected : mTextSizeNormal);
        tab.setSelected(b);
        tab.setPressed(b);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    @Override
    public void onClick(View v) {
        int currentTabIndex = v.getId() - BSSEEID;
        setCurrentTab(currentTabIndex);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == this && hasFocus) {
            findViewById(BSSEEID + mCurrentTabIndex).requestFocus();
            return;
        } else if (hasFocus) {
            for (int i = 0; i < mTabCount; i++) {
                if (getChildAt(i) == v) {
                    setCurrentTab(i);
                }
            }
        }
    }

    // 注意：必须要设置背景后，该方法才会被调用
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int itemWidth = getWidth();
        float indicatorScrollX = mPagerScrollX;

        if (mTabCount != 0) {
            itemWidth = getWidth() / mTabCount;
            indicatorScrollX = itemWidth * mPagerScrollX / getPagerRealWidth();
        }

        mPath.rewind();
        float offset = 20;
        float left_x = offset + indicatorScrollX;
        float right_x = itemWidth - offset + indicatorScrollX;
        float top_y = getHeight() - mTriangleMarginBottom - mTriangleHeight;
        float bottom_y = getHeight() - mTriangleMarginBottom;

        mPath.moveTo(left_x, top_y + 1f);
        mPath.lineTo(right_x, top_y + 1f);
        mPath.lineTo(right_x, bottom_y + 1f);
        mPath.lineTo(left_x, bottom_y + 1f);
        mPath.close();

        canvas.drawPath(mPath, mPaintFooterTriangle);
    }

    private int getPagerRealWidth() {
        return mViewPager.getWidth() + mViewPager.getPageMargin();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mPagerScrollX = getPagerRealWidth() * position + positionOffsetPixels;
        postInvalidate();

        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentTab(position);

        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }

}