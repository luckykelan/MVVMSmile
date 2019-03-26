package com.kelan.mvvmsmile.binding.command;

/**
 * Created by wanghua on 18-1-9.
 * 一个参数的命令
 */
public interface BindingConsumer<T> {
    void call(T t);
}
