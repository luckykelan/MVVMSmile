/*
 * Tencent is pleased to support the open source community by making Smile_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kelan.mvvmsmile.widget.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kelan.mvvmsmile.R;
import com.kelan.mvvmsmile.utils.SmileResHelper;
import com.kelan.mvvmsmile.utils.SmileViewHelper;


/**
 * 菜单类型的对话框的item
 *
 * @author chantchen
 * @date 2016-1-20
 */

public class SmileDialogMenuItemView extends RelativeLayout {
    private int index = -1;
    private MenuItemViewListener mListener;
    private boolean mIsChecked = false;

    public SmileDialogMenuItemView(Context context) {
        super(context, null, R.attr.smile_dialog_menu_item_style);
    }

    public static TextView createItemTextView(Context context) {
        TextView tv = new TextView(context);
        TypedArray a = context.obtainStyledAttributes(null, R.styleable.SmileDialogMenuTextStyleDef, R.attr.smile_dialog_menu_item_style, 0);
        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SmileDialogMenuTextStyleDef_android_gravity) {
                tv.setGravity(a.getInt(attr, -1));
            } else if (attr == R.styleable.SmileDialogMenuTextStyleDef_android_textColor) {
                tv.setTextColor(a.getColorStateList(attr));
            } else if (attr == R.styleable.SmileDialogMenuTextStyleDef_android_textSize) {
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimensionPixelSize(attr, 0));
            }
        }
        a.recycle();

        tv.setSingleLine(true);
        tv.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        tv.setDuplicateParentStateEnabled(false);
        return tv;
    }

    public int getMenuIndex() {
        return this.index;
    }

    public void setMenuIndex(int index) {
        this.index = index;
    }

    protected void notifyCheckChange(boolean isChecked) {

    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean checked) {
        mIsChecked = checked;
        notifyCheckChange(mIsChecked);
    }

    public void setListener(MenuItemViewListener listener) {
        if (!isClickable()) {
            setClickable(true);
        }
        mListener = listener;
    }

    @Override
    public boolean performClick() {
        if (mListener != null) {
            mListener.onClick(index);
        }
        return super.performClick();
    }

    public interface MenuItemViewListener {
        void onClick(int index);
    }

    public static class TextItemView extends SmileDialogMenuItemView {
        protected TextView mTextView;

        public TextItemView(Context context) {
            super(context);
            init();
        }

        public TextItemView(Context context, CharSequence text) {
            super(context);
            init();
            setText(text);
        }

        private void init() {
            mTextView = createItemTextView(getContext());
            addView(mTextView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        }

        public void setText(CharSequence text) {
            mTextView.setText(text);
        }

        public void setTextColor(int color) {
            mTextView.setTextColor(color);
        }
    }

    public static class MarkItemView extends SmileDialogMenuItemView {
        private Context mContext;
        private TextView mTextView;
        private ImageView mCheckedView;

        public MarkItemView(Context context) {
            super(context);
            mContext = context;
            mCheckedView = new ImageView(mContext);
            mCheckedView.setId(SmileViewHelper.generateViewId());

            TypedArray a = context.obtainStyledAttributes(null, R.styleable.SmileDialogMenuMarkDef,
                    R.attr.smile_dialog_menu_item_style, 0);
            int markMarginHor = 0;
            int count = a.getIndexCount();
            for (int i = 0; i < count; i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.SmileDialogMenuMarkDef_smile_dialog_menu_item_check_mark_margin_hor) {
                    markMarginHor = a.getDimensionPixelSize(attr, 0);
                } else if (attr == R.styleable.SmileDialogMenuMarkDef_smile_dialog_menu_item_mark_drawable) {
                    mCheckedView.setImageDrawable(SmileResHelper.getAttrDrawable(context, a, attr));
                }
            }
            a.recycle();

            RelativeLayout.LayoutParams checkLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            checkLp.addRule(CENTER_VERTICAL, TRUE);
            checkLp.addRule(ALIGN_PARENT_RIGHT, TRUE);
            checkLp.leftMargin = markMarginHor;
            addView(mCheckedView, checkLp);

            mTextView = createItemTextView(mContext);
            RelativeLayout.LayoutParams tvLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            tvLp.addRule(ALIGN_PARENT_LEFT, TRUE);
            tvLp.addRule(LEFT_OF, mCheckedView.getId());
            addView(mTextView, tvLp);
        }

        public MarkItemView(Context context, CharSequence text) {
            this(context);
            setText(text);
        }

        public void setText(CharSequence text) {
            mTextView.setText(text);
        }

        @Override
        protected void notifyCheckChange(boolean isChecked) {
            SmileViewHelper.safeSetImageViewSelected(mCheckedView, isChecked);
        }
    }

    @SuppressLint("ViewConstructor")
    public static class CheckItemView extends SmileDialogMenuItemView {
        private Context mContext;
        private TextView mTextView;
        private ImageView mCheckedView;

        public CheckItemView(Context context, boolean right) {
            super(context);
            mContext = context;
            mCheckedView = new ImageView(mContext);
            mCheckedView.setId(SmileViewHelper.generateViewId());

            TypedArray a = context.obtainStyledAttributes(null, R.styleable.SmileDialogMenuCheckDef,
                    R.attr.smile_dialog_menu_item_style, 0);
            int markMarginHor = 0;
            int count = a.getIndexCount();
            for (int i = 0; i < count; i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.SmileDialogMenuCheckDef_smile_dialog_menu_item_check_mark_margin_hor) {
                    markMarginHor = a.getDimensionPixelSize(attr, 0);
                } else if (attr == R.styleable.SmileDialogMenuCheckDef_smile_dialog_menu_item_check_drawable) {
                    mCheckedView.setImageDrawable(SmileResHelper.getAttrDrawable(context, a, attr));
                }
            }
            a.recycle();

            RelativeLayout.LayoutParams checkLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            checkLp.addRule(CENTER_VERTICAL, TRUE);
            if (right) {
                checkLp.addRule(ALIGN_PARENT_RIGHT, TRUE);
                checkLp.leftMargin = markMarginHor;
            } else {
                checkLp.addRule(ALIGN_PARENT_LEFT, TRUE);
                checkLp.rightMargin = markMarginHor;
            }

            addView(mCheckedView, checkLp);

            mTextView = createItemTextView(mContext);
            RelativeLayout.LayoutParams tvLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            if (right) {
                tvLp.addRule(LEFT_OF, mCheckedView.getId());
            } else {
                tvLp.addRule(RIGHT_OF, mCheckedView.getId());
            }

            addView(mTextView, tvLp);
        }

        public CheckItemView(Context context, boolean right, CharSequence text) {
            this(context, right);
            setText(text);
        }

        public void setText(CharSequence text) {
            mTextView.setText(text);
        }

        public CharSequence getText() {
            return mTextView.getText();
        }

        @Override
        protected void notifyCheckChange(boolean isChecked) {
            SmileViewHelper.safeSetImageViewSelected(mCheckedView, isChecked);
        }
    }
}