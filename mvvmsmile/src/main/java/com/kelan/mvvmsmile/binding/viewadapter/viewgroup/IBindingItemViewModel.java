package com.kelan.mvvmsmile.binding.viewadapter.viewgroup;

import androidx.databinding.ViewDataBinding;

/**
 * Created by wanghua on 18-1-9.
 */


public interface IBindingItemViewModel<V extends ViewDataBinding> {
    void injecDataBinding(V binding);
}
