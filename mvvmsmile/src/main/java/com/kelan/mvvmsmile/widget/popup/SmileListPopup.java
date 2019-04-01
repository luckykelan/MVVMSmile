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

package com.kelan.mvvmsmile.widget.popup;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;


/**
 * 继承自 {@link SmilePopup}，在 {@link SmilePopup} 的基础上，支持显示一个列表。
 *
 * @author cginechen
 * @date 2016-11-16
 */

public class SmileListPopup extends SmilePopup {
    private BaseAdapter mAdapter;

    /**
     * 构造方法。
     *
     * @param context   传入一个 Context。
     * @param direction Popup 的方向，为 {@link SmilePopup#DIRECTION_NONE}, {@link SmilePopup#DIRECTION_TOP} 和 {@link SmilePopup#DIRECTION_BOTTOM} 中的其中一个值。
     * @param adapter   列表的 Adapter
     */
    public SmileListPopup(Context context, @Direction int direction, BaseAdapter adapter) {
        super(context, direction);
        mAdapter = adapter;
    }

    public void create(int width, int maxHeight, AdapterView.OnItemClickListener onItemClickListener) {
        ListView listView = new SmileWrapContentListView(mContext, maxHeight);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, maxHeight);
        listView.setLayoutParams(lp);
        listView.setAdapter(mAdapter);
        listView.setVerticalScrollBarEnabled(false);
        listView.setOnItemClickListener(onItemClickListener);
        listView.setDivider(null);
        setContentView(listView);
    }
}
