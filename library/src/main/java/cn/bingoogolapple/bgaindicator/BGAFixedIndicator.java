package cn.bingoogolapple.bgaindicator;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BGAFixedIndicator extends LinearLayout implements View.OnClickListener, View.OnFocusChangeListener, OnPageChangeListener {
    private final int BSSEEID = 0xffff00;
    private ColorStateList mTextColor;
    private int mTextSizeNormal = 12;
    private int mTextSizeSelected = 15;

    private int mTriangleColor = android.R.color.white;
    private int mTriangleHeight = 5;
    private int mTriangleHorizontalMargin = 20;

    private boolean mHasDivider = true;
    private int mDividerColor = android.R.color.black;
    private int mDividerWidth = 3;
    private int mDividerVerticalMargin = 10;

    private Paint mPaintFooterTriangle;
    private LayoutInflater mInflater;

    private ViewPager mViewPager;

    private int mTabCount = 0;
    private int mCurrentTabIndex = 0;

    private int mTriangleLeftX = 0;

    private Path mPath = new Path();
    private int mItemWidth;

    private OnPageChangeListener mOnPageChangeListener;

    public BGAFixedIndicator(Context context) {
        this(context, null);
    }

    public BGAFixedIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initDraw(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BGAIndicator);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    public void initAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.BGAIndicator_indicator_triangleColor) {
            mTriangleColor = typedArray.getColor(attr, mTriangleColor);
        } else if (attr == R.styleable.BGAIndicator_indicator_triangleHorizontalMargin) {
            /**
             * getDimension和getDimensionPixelOffset的功能差不多,都是获取某个dimen的值,如果是dp或sp的单位,将其乘以density,如果是px,则不乘;两个函数的区别是一个返回float,一个返回int. getDimensionPixelSize则不管写的是dp还是sp还是px,都会乘以denstiy.
             */
            mTriangleHorizontalMargin = typedArray.getDimensionPixelSize(attr, mTriangleHorizontalMargin);
        } else if (attr == R.styleable.BGAIndicator_indicator_triangleHeight) {
            mTriangleHeight = typedArray.getDimensionPixelSize(attr, mTriangleHeight);
        } else if (attr == R.styleable.BGAIndicator_indicator_textColor) {
            mTextColor = typedArray.getColorStateList(attr);
        } else if (attr == R.styleable.BGAIndicator_indicator_textSizeNormal) {
            mTextSizeNormal = typedArray.getDimensionPixelSize(attr, mTextSizeNormal);
        } else if (attr == R.styleable.BGAIndicator_indicator_textSizeSelected) {
            mTextSizeSelected = typedArray.getDimensionPixelSize(attr, mTextSizeSelected);
        } else if (attr == R.styleable.BGAIndicator_indicator_hasDivider) {
            mHasDivider = typedArray.getBoolean(attr, mHasDivider);
        } else if (attr == R.styleable.BGAIndicator_indicator_dividerColor) {
            mDividerColor = typedArray.getColor(attr, mDividerColor);
        } else if (attr == R.styleable.BGAIndicator_indicator_dividerWidth) {
            mDividerWidth = typedArray.getDimensionPixelSize(attr, mDividerWidth);
        } else if (attr == R.styleable.BGAIndicator_indicator_dividerVerticalMargin) {
            mDividerVerticalMargin = typedArray.getDimensionPixelSize(attr, mDividerVerticalMargin);
        }
    }

    private void initDraw(Context context) {
        mPaintFooterTriangle = new Paint();
        mPaintFooterTriangle.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintFooterTriangle.setColor(mTriangleColor);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // 初始化选项卡
    public void initData(int currentTab, ViewPager viewPager) {
        this.removeAllViews();
        mViewPager = viewPager;
        mTabCount = mViewPager.getAdapter().getCount();

        mViewPager.setOnPageChangeListener(this);

        initTab(currentTab);
        postInvalidate();
    }

    private void initTab(int currentTab) {
        for (int index = 0; index < mTabCount; index++) {
            View tabIndicator = mInflater.inflate(R.layout.view_indicator, this, false);
            tabIndicator.setId(BSSEEID + index);
            tabIndicator.setOnClickListener(this);

            TextView titleTv = (TextView) tabIndicator.findViewById(R.id.tv_indicator_title);
            if (mTextColor != null) {
                titleTv.setTextColor(mTextColor);
            }
            titleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeNormal);
            titleTv.setText(mViewPager.getAdapter().getPageTitle(index));

            LayoutParams tabLp = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
            tabLp.gravity = Gravity.CENTER;
            tabIndicator.setLayoutParams(tabLp);
            // 防止currentTab为0时，第一个tab文字颜色没变化
            if (index == 0) {
                resetTab(tabIndicator, true);
            }
            this.addView(tabIndicator);

            if (index != mTabCount - 1 && mHasDivider) {
                LayoutParams dividerLp = new LayoutParams(mDividerWidth, LayoutParams.MATCH_PARENT);
                dividerLp.setMargins(0, mDividerVerticalMargin, 0, mDividerVerticalMargin);
                View vLine = new View(getContext());
                vLine.setBackgroundColor(mDividerColor);
                vLine.setLayoutParams(dividerLp);
                this.addView(vLine);
            }
        }
        setCurrentTab(currentTab);
    }

    private void setCurrentTab(int index) {
        if (mCurrentTabIndex != index && index > -1 && index < mTabCount) {
            View oldTab = findViewById(BSSEEID + mCurrentTabIndex);
            resetTab(oldTab, false);

            mCurrentTabIndex = index;
            View newTab = findViewById(BSSEEID + mCurrentTabIndex);
            resetTab(newTab, true);

            if (mViewPager.getCurrentItem() != mCurrentTabIndex) {
                mViewPager.setCurrentItem(mCurrentTabIndex);
            }
            postInvalidate();
        }
    }

    private void resetTab(View tab, boolean isSelected) {
        TextView tv = (TextView) tab.findViewById(R.id.tv_indicator_title);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, isSelected ? mTextSizeSelected : mTextSizeNormal);
        tab.setSelected(isSelected);
        tab.setPressed(isSelected);
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
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
                    return;
                }
            }
        }
    }

    // 注意：必须要设置背景后，该方法才会被调用
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.rewind();
        float left_x = mTriangleHorizontalMargin + mTriangleLeftX;
        float right_x = mTriangleLeftX + mItemWidth - mTriangleHorizontalMargin;
        float top_y = getHeight() - mTriangleHeight;
        float bottom_y = getHeight();

        mPath.moveTo(left_x, top_y);
        mPath.lineTo(right_x, top_y);
        mPath.lineTo(right_x, bottom_y);
        mPath.lineTo(left_x, bottom_y);
        mPath.close();

        canvas.drawPath(mPath, mPaintFooterTriangle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mItemWidth = getWidth();
        if (mTabCount != 0) {
            mItemWidth = getWidth() / mTabCount;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mTriangleLeftX = (int) (mItemWidth * (position + positionOffset));

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