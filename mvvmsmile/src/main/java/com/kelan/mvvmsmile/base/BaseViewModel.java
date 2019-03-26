package com.kelan.mvvmsmile.base;

import android.app.Application;
import android.os.Bundle;

import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Created by wanghua on 2018/9/17.
 * Description：viewModel基类
 */

public class BaseViewModel extends AndroidViewModel implements IBaseViewModel {

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }


    UIChangeLiveData uc = new UIChangeLiveData();

    private LifecycleProvider lifecycle;

    public void injectLifecycleProvider(LifecycleProvider lifecycle) {
        this.lifecycle = lifecycle;
    }

    public LifecycleProvider getLifecycleProvider() {
        return lifecycle;
    }

    public class UIChangeLiveData extends LiveData {
        public MutableLiveData<Map<String, Object>> startActivityLiveData = new MutableLiveData<>();
        public MutableLiveData<Boolean> finishLiveData = new MutableLiveData<>();
        public MutableLiveData<Boolean> refreshEnable = new MutableLiveData<>();
        public MutableLiveData<Boolean> loadEnable = new MutableLiveData<>();
    }



    public void closeRefresh() {
        uc.refreshEnable.postValue(uc.refreshEnable.getValue() != null && !uc.refreshEnable.getValue());
    }

    public boolean showLoading() {
        uc.loadEnable.setValue(uc.loadEnable.getValue() != null && !uc.loadEnable.getValue());
        return true;
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.CLASS, clz);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        uc.startActivityLiveData.postValue(params);
    }

    /**
     * 关闭界面
     */
    public void finish() {
        uc.finishLiveData.postValue(uc.finishLiveData.getValue() != null && !uc.finishLiveData.getValue());
    }


    @Override
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void registerRxBus() {

    }

    @Override
    public void removeRxBus() {

    }
    public static class ParameterField {
        public static String CLASS = "CLASS";
        public static String BUNDLE = "BUNDLE";
    }


}
