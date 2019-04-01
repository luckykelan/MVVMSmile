package com.kelan.mvvmsmile.widget.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.kelan.mvvmsmile.widget.SmileObservableScrollView;

/**
 * height is wrapContent but limited by maxHeight
 * <p>
 */

class SmileWrapContentScrollView extends SmileObservableScrollView {
    private int mMaxHeight = Integer.MAX_VALUE >> 2;

    public SmileWrapContentScrollView(Context context) {
        super(context);
    }

    public SmileWrapContentScrollView(Context context, int maxHeight) {
        super(context);
        mMaxHeight = maxHeight;
    }

    public SmileWrapContentScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmileWrapContentScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMaxHeight(int maxHeight) {
        if (mMaxHeight != maxHeight) {
            mMaxHeight = maxHeight;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        int expandSpec;
        if (lp.height > 0 && lp.height <= mMaxHeight) {
            expandSpec = View.MeasureSpec.makeMeasureSpec(lp.height, View.MeasureSpec.EXACTLY);
        } else {
            expandSpec = View.MeasureSpec.makeMeasureSpec(mMaxHeight, View.MeasureSpec.AT_MOST);
        }

        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
