package com.kelan.mvvmsmile.binding.viewadapter.twinklingRefreshLayout;



import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import androidx.databinding.BindingAdapter;
import com.kelan.mvvmsmile.binding.command.BindingCommand;


/**
 * Created by wanghua on 2018/6/16.
 * TwinklingRefreshLayout列表刷新的绑定适配器
 */
public class ViewAdapter {

    @BindingAdapter(value = {"onRefreshTwinklingCommand", "onLoadMoreTwinklingCommand"}, requireAll = false)
    public static void onRefreshAndLoadMoreCommand(TwinklingRefreshLayout layout, final BindingCommand onRefreshTwinklingCommand, final BindingCommand onLoadMoreTwinklingCommand) {
        layout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                if (onRefreshTwinklingCommand != null) {
                    onRefreshTwinklingCommand.execute();
                }
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if (onLoadMoreTwinklingCommand != null) {
                    onLoadMoreTwinklingCommand.execute();
                }
            }
        });
    }
}
