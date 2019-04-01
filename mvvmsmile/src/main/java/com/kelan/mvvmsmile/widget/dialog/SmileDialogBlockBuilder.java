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

import android.content.Context;
import android.content.res.TypedArray;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kelan.mvvmsmile.R;
import com.kelan.mvvmsmile.utils.SmileResHelper;
import com.kelan.mvvmsmile.widget.textview.SmileSpanTouchFixTextView;


/**
 * @author cginechen
 * @date 2015-12-12
 */
public class SmileDialogBlockBuilder extends SmileDialogBuilder<SmileDialogBlockBuilder> {
    private CharSequence mContent;


    public SmileDialogBlockBuilder(Context context) {
        super(context);
        setActionDivider(1, R.color.smile_config_color_separator, 0, 0);
    }


    public SmileDialogBlockBuilder setContent(CharSequence content) {
        mContent = content;
        return this;
    }

    public SmileDialogBlockBuilder setContent(int contentRes) {
        mContent = getBaseContext().getResources().getString(contentRes);
        return this;
    }

    @Override
    protected void onConfigTitleView(TextView titleView) {
        super.onConfigTitleView(titleView);
        if(mContent == null || mContent.length() == 0){
            TypedArray a = titleView.getContext().obtainStyledAttributes(null,
                    R.styleable.SmileDialogTitleTvCustomDef, R.attr.smile_dialog_title_style, 0);
            int count = a.getIndexCount();
            for (int i = 0; i < count; i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.SmileDialogTitleTvCustomDef_smile_paddingBottomWhenNotContent) {
                    titleView.setPadding(
                            titleView.getPaddingLeft(),
                            titleView.getPaddingTop(),
                            titleView.getPaddingRight(),
                            a.getDimensionPixelSize(attr, titleView.getPaddingBottom())
                    );
                }
            }
            a.recycle();
        }
    }

    @Override
    protected void onCreateContent(SmileDialog dialog, ViewGroup parent, Context context) {
        if(mContent != null && mContent.length() > 0){
            TextView contentTv = new SmileSpanTouchFixTextView(context);
            SmileResHelper.assignTextViewWithAttr(contentTv, R.attr.smile_dialog_message_content_style);

            if (!hasTitle()) {
                TypedArray a = context.obtainStyledAttributes(null,
                        R.styleable.SmileDialogMessageTvCustomDef,
                        R.attr.smile_dialog_message_content_style, 0);
                int count = a.getIndexCount();
                for (int i = 0; i < count; i++) {
                    int attr = a.getIndex(i);
                    if (attr == R.styleable.SmileDialogMessageTvCustomDef_smile_paddingTopWhenNotTitle) {
                        contentTv.setPadding(
                                contentTv.getPaddingLeft(),
                                a.getDimensionPixelSize(attr, contentTv.getPaddingTop()),
                                contentTv.getPaddingRight(),
                                contentTv.getPaddingBottom()
                        );
                    }
                }
                a.recycle();
            }
            contentTv.setText(mContent);


            SmileWrapContentScrollView scrollView = new SmileWrapContentScrollView(context);
            scrollView.setMaxHeight(getContentAreaMaxHeight());
            scrollView.addView(contentTv);
            parent.addView(scrollView);
        }
    }

    @Override
    public SmileDialog create(int style) {
        setActionContainerOrientation(VERTICAL);
        return super.create(style);
    }
}
