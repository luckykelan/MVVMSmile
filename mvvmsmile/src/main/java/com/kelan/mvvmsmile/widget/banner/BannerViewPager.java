package com.kelan.mvvmsmile.widget.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelan.mvvmsmile.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by wanghua on 2018/11/20.
 * Description：
 */

public class BannerViewPager extends FrameLayout {

    private static final int MSG_WHAT = 0;
    private int delayMillis;
    private ViewPager mViewPager;
    private ViewPager.PageTransformer mPageTransformer;
    private BannerAdapter mBannerAdapter;
    private int mItemCount;
    private LinearLayout mIndicators;
    private TextView mTitle;
    private boolean mAutoPlay;
    private OnBannerItemClickListener mOnBannerClickListener;
    private BaseIndicator mIndicatorView;
    private ImageLoaderInterface mImageLoader;
    private boolean mHaveTitle;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mAutoPlay) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                this.sendEmptyMessageDelayed(MSG_WHAT, delayMillis);
            }
        }
    };

    public BannerViewPager(@NonNull Context context) {
        this(context, null);
    }

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHaveTitle = true;
        mAutoPlay = true;
        mItemCount = 1;
        delayMillis = 5000;
        initView();
        initListener();
        mHandler.sendEmptyMessageDelayed(MSG_WHAT, delayMillis);
    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setTitleSlogan(position);
                for (int i = 0; i < mIndicators.getChildCount(); i++) {
                    if (i == position % mItemCount) {
                        ((BaseIndicator) mIndicators.getChildAt(i)).setState(true);
                    } else {
                        ((BaseIndicator) mIndicators.getChildAt(i)).setState(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initView() {
        View.inflate(getContext(), R.layout.layout_bannerviewpager, this);
        mViewPager = ((ViewPager) findViewById(R.id.viewPager));
        mIndicators = ((LinearLayout) findViewById(R.id.bannerIndicators));
        mTitle = ((TextView) findViewById(R.id.bannerTitle));
    }

    public BannerViewPager setData(List<BannerItemBean> data, ImageLoaderInterface imageLoader) {
        mImageLoader = imageLoader;
        mBannerAdapter = new BannerAdapter(this);
        mBannerAdapter.setData(data);
        mItemCount = data.size();
        mViewPager.setAdapter(mBannerAdapter);
        mViewPager.setCurrentItem(mItemCount * 1000);
        setIndicators(data.size());
        setTitleSlogan(0);
        return this;
    }

    private void setTitleSlogan(int i) {
        if (mHaveTitle) {
            if (mTitle.getVisibility() == GONE) {
                mTitle.setVisibility(VISIBLE);
            }
            String s = mBannerAdapter.getData().get(i % mItemCount).getTitle();
            mTitle.setText(s);
        } else if (mTitle.getVisibility() == VISIBLE) {
            mTitle.setVisibility(GONE);
        }
    }

    private void setIndicators(int dataCount) {
        mIndicators.removeAllViews();
        for (int i = 0; i < dataCount; i++) {
            if (mIndicatorView == null) {
                Indicator indicator = new Indicator(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                        (getRatioDimension(getContext(), 20, false),
                                getRatioDimension(getContext(), 20, false));
                layoutParams.setMargins(getRatioDimension(getContext(), 10, true), 0,
                        getRatioDimension(getContext(), 10, true), 0);
                indicator.setLayoutParams(layoutParams);
                mIndicators.addView(indicator);
            } else {
                BaseIndicator baseIndicator = mIndicatorView;
                ViewParent vp = baseIndicator.getParent();
                if (vp != null) {
                    ViewGroup parent = (ViewGroup) vp;
                    parent.removeView(baseIndicator);
                }
                mIndicators.addView(baseIndicator);
            }
        }
        ((BaseIndicator) mIndicators.getChildAt(0)).setState(true);
    }

    public void displayImg(Context context, ImageView imageView, Object s) {
        mImageLoader.displayImage(context, s, imageView);
    }

    public interface OnBannerItemClickListener {
        void OnClickLister(View view, BannerItemBean currentItem);
    }

    protected void OnBannerItemClick(View view) {
        if (mOnBannerClickListener != null) {
            /*mOnBannerClickListener.OnClickLister(view, mViewPager.getCurrentItem() % mItemCount);*/
            mOnBannerClickListener.OnClickLister(view,  mBannerAdapter.getData().get(mViewPager.getCurrentItem() % mItemCount));
        }
    }

    public static int getRatioDimension(Context context, int value, boolean isWidth) {
        DisplayMetrics displayMetrics = context.getApplicationContext().getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        float STANDARD_WIDTH = 1080F;
        float STANDARD_HEIGHT = 1920F;
        if (isWidth) {
            return (int) (value / STANDARD_WIDTH * widthPixels);
        } else {
            return (int) (value / STANDARD_HEIGHT * heightPixels);
        }
    }

    public boolean isAutoPlay() {
        return mAutoPlay;
    }

    public BannerViewPager setAutoPlay(boolean autoPlay) {
        mAutoPlay = autoPlay;
        return this;
    }

    public BannerViewPager setOnBannerItemClickListener(OnBannerItemClickListener onBannerClickListener) {
        mOnBannerClickListener = onBannerClickListener;
        return this;
    }

    protected BaseIndicator getIndicatorView() {
        return mIndicatorView;
    }

    private BannerViewPager setIndicatorView(BaseIndicator indicatorView) {
        mIndicatorView = indicatorView;
        Log.e("ooo", "" + mItemCount);
        mViewPager.setCurrentItem(mItemCount * 1000);
        setIndicators(mItemCount);
        setTitleSlogan(0);
        return this;
    }

    public BannerViewPager setPageTransformer(ViewPager.PageTransformer pageTransformer) {
        mPageTransformer = pageTransformer;
        mViewPager.setPageTransformer(true, mPageTransformer);
        return this;
    }

    public BannerViewPager setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
        return this;
    }

    public boolean isHaveTitle() {
        return mHaveTitle;
    }

    public BannerViewPager setHaveTitle(boolean haveTitle) {
        mHaveTitle = haveTitle;
        setTitleSlogan(mViewPager.getCurrentItem());
        return this;
    }
     class BannerAdapter extends PagerAdapter {

        private BannerViewPager bannerViewPager;
        private int mItemCount = 1;
        private List<BannerItemBean> mData;
        private ImageView.ScaleType mScaleType;

        public List<BannerItemBean> getData() {
            return mData;
        }

        public void setData(List<BannerItemBean> data) {
            mData = data;
            if (mData != null && mData.size() != 0) {
                mItemCount = mData.size();
            }
        }

        private ImageView.ScaleType getScaleType() {
            return mScaleType == null ? ImageView.ScaleType.CENTER_CROP : mScaleType;
        }

        public void setScaleType(ImageView.ScaleType scaleType) {
            mScaleType = scaleType;
        }

        public BannerAdapter(BannerViewPager bannerViewPager) {
            this.bannerViewPager = bannerViewPager;
        }

        @Override
        public int getCount() {
            return mData == null ? 0 : Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(((ImageView) object));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(getScaleType());
            bannerViewPager.displayImg(container.getContext(), imageView, mData.get(position % mItemCount).getImg_path());
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    bannerViewPager.OnBannerItemClick(view);
                }
            });
            container.addView(imageView);

            return imageView;
        }

    }



    abstract class BaseIndicator extends View {

        public BaseIndicator(Context context) {
            super(context);
        }

        public BaseIndicator(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public BaseIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public abstract void setState(boolean b);
    }


    class Indicator extends BaseIndicator {
        private Paint mPaint;

        public Indicator(Context context) {
            super(context);
            init();
        }

        private void init() {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            setState(false);
        }

        @Override
        public void setState(boolean b) {
            if (b){
                mPaint.setColor(0xffffffff);
            }else {
                mPaint.setColor(0x88ffffff);
            }
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int measuredHeight = getMeasuredHeight();
            int measuredWidth = getMeasuredWidth();

            Log.e("heigth",""+measuredHeight);
            Log.e("width",""+measuredWidth);
            canvas.translate(measuredWidth * 0.5F, measuredHeight * 0.5F);
            canvas.drawCircle(0F, 0F, measuredWidth * 0.5F, mPaint);
        }
    }

}