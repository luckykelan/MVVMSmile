package com.kelan.mvvmsmile.base;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kelan.mvvmsmile.R;
import com.kelan.mvvmsmile.binding.command.BindingConsumer;
import com.kelan.mvvmsmile.bus.messenger.Messenger;
import com.kelan.mvvmsmile.utils.SmileStatusBarHelper;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends RxAppCompatActivity implements IBaseActivity {
    protected V binding;
    protected VM viewModel;

    protected FrameLayout multipleStatusView;
    private ViewGroup mContentView;
    private Toolbar toolbar;
    protected ImageView rightImg;
    protected Dialog loading;

    protected static final RelativeLayout.LayoutParams DEFAULT_LAYOUT_PARAMS =
            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContentView = findViewById(android.R.id.content);
        multipleStatusView = findViewById(R.id.multiplestatusview);

        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //沉浸式状态栏
        SmileStatusBarHelper.translucent(this);

        //设置状态栏字体颜色
        if (ifStatusBarLightMode())
            SmileStatusBarHelper.setStatusBarLightMode(this);
        else
            SmileStatusBarHelper.setStatusBarDarkMode(this);



        initViewDataBinding(savedInstanceState);

        initParam(getIntent().getExtras());

        registorUIChangeLiveDataCallBack();

        initViewObservable();

        initTitle();

        viewModel.registerRxBus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Messenger.getDefault().unregister(viewModel);
        //multipleStatusView.setRefreshing(false);
        getLifecycle().removeObserver(viewModel);
        viewModel.removeRxBus();
        viewModel = null;
        binding.unbind();
        dimissLoading();
    }


    private void initTitle() {
        if (initTitleText() == null) {
            toolbar.setVisibility(View.GONE);
            return;
        } else toolbar.setVisibility(View.VISIBLE);

        TextView textView = findViewById(R.id.mToolBarTitleLabel);
        toolbar.setTitle("");
        ImageView imageView = findViewById(R.id.back_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rightImg = findViewById(R.id.right_icon);
        if (initRightIcon() != 0) {
            rightImg.setImageResource(initRightIcon());
            rightImg.setVisibility(View.VISIBLE);
            rightImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initRightOnClickListener(v);
                }
            });
        } else rightImg.setVisibility(View.GONE);


        textView.setText(initTitleText());
    }


    /**
     * 注入绑定
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
        viewModel = initViewModel();
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            viewModel = (VM) ViewModelProviders.of(this).get(modelClass);
        }
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState));

        /*binding = DataBindingUtil.inflate(getLayoutInflater(), initContentView(savedInstanceState), mContentView, true);
        setContentView(initContentView(savedInstanceState));*/
        binding.setVariable(initVariableId(), viewModel);
        //让ViewModel拥有View的生命周期感应
        binding.setLifecycleOwner(this);
        getLifecycle().addObserver(viewModel);
        viewModel.injectLifecycleProvider(this);
    }

    //注册ViewModel与View的契约UI回调事件
    private void registorUIChangeLiveDataCallBack() {

        viewModel.uc.startActivityLiveData.observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
                Class<?> clz = (Class<?>) params.get(BaseViewModel.ParameterField.CLASS);
                Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                startActivity(clz, bundle);
            }
        });

        viewModel.uc.finishLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                finish();
            }
        });
        //关闭loading对话框
        viewModel.uc.refreshEnable.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                // multipleStatusView.setRefreshing(false);
            }
        });
        viewModel.uc.loadEnable.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                showLoading();
            }
        });

        Messenger.getDefault().register(this, "dimissLoad", Boolean.class, new BindingConsumer<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                dimissLoading();
            }
        });


    }

    public void showLoading() {
        loading = new MaterialDialog.Builder(this)
                .title("请稍后...")
                //.content("Please Wait...")
                .cancelable(false)
                .widgetColor(Color.parseColor("#3F51B5"))
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();
    }

    public void dimissLoading() {
        if (loading != null && loading.isShowing())
            loading.dismiss();
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

   /* //刷新布局
    public void refreshLayout() {
        if (viewModel != null) {
            binding.setVariable(initVariableId(), viewModel);
        }
    }*/

    @Override
    public void initParam(Bundle bundle) {
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView(Bundle savedInstanceState);

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public abstract int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public VM initViewModel() {
        return null;
    }

    /**
     * 初始化标题栏
     *
     * @return 标题
     */
    public abstract String initTitleText();

    public int initRightIcon() {
        return 0;
    }

    public void initRightOnClickListener(View view) {
    }


    @Override
    public void initViewObservable() {
    }

    @Override
    public boolean ifStatusBarLightMode() {
        return false;
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, multipleStatusView, false);
        multipleStatusView.setId(android.R.id.content);
        mContentView.setId(View.NO_ID);
        multipleStatusView.removeAllViews();
        multipleStatusView.addView(view);
    }

}
