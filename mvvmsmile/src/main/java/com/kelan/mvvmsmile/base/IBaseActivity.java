package com.kelan.mvvmsmile.base;

import android.os.Bundle;

/**
 * Created by wanghua on 2018/9/17.
 * Description：
 */

public interface IBaseActivity {
    /**
     * 初始化界面传递参数
     */
    void initParam(Bundle bundle);

    /**
     * 初始化界面观察者的监听
     */
    void initViewObservable();

    /**
     * 设置是否需要状态栏文字黑色字体
     */

    boolean ifStatusBarLightMode();


}
