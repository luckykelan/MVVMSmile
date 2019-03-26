package com.kelan.mvvmsmile.binding.viewadapter.spinner;

/**
 * Created by wanghua on 18-1-9.
 * 下拉Spinner控件的键值对, 实现该接口,返回key,value值, 在xml绑定List<IKeyAndValue>
 */
public interface IKeyAndValue {
    String getKey();

    String getValue();
}
