package com.kelan.mvvmsmile.net;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import com.kelan.mvvmsmile.bus.messenger.Messenger;
import com.kelan.mvvmsmile.net.cache.ACache;
import retrofit2.HttpException;


/**
 * Created by wanghua on 2017/4/18.
 * Description：
 */

public abstract class DefaultObserver<T extends BasicResponse> implements Observer<T> {
    private String cacheKey;
    private ACache mCache;
    private Context context;
    private boolean ifExtraneousDialog;//是否是外部定义的对话框

    public DefaultObserver(Context context, String cacheKey) {
        this(context, false, cacheKey);
    }


    public DefaultObserver(Context context, boolean ifShowingDialog, String cacheKey) {
        //this(context, ifShowingDialog, null, cacheKey);
        this.ifExtraneousDialog = ifShowingDialog;
        this.context = context;
        this.cacheKey = cacheKey;
        mCache = ACache.get(context);
    }

    /*private DefaultObserver(Context context, boolean ifShowingDialog, Dialog dialog, String cacheKey) {
        mCache = ACache.get(context);
        this.cacheKey = cacheKey;
        this.context = context;
        if (dialog == null && ifShowingDialog) {
            loading =new MaterialDialog.Builder(context)
                    .title("请稍后...")
                    //.content("Please Wait...")
                    .cancelable(false)
                    .widgetColor(Color.parseColor("#6da457"))
                    .progress(true, 0)
                    .progressIndeterminateStyle(true)
                    .show();
            ifExtraneousDialog = false;
        } else if (dialog != null) {
            loading = dialog;
            loading.show();
            ifExtraneousDialog = true;
        }


    }*/

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T response) {
        //if (!ifExtraneousDialog) dismissProgress();
        Messenger.getDefault().send(true, "dimissLoad");
        if (!response.isError()) cacheManage(true, response);
        else try {
            onFail(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(Throwable e) {
        Messenger.getDefault().send(true, "dimissLoad");
        onException(e);
        cacheManage(false, null);
    }


    @Override
    public void onComplete() {
    }

    /**
     * 缓存策略
     */
    private void cacheManage(boolean access, T response) {
        /*try {
            Integer cacheLong;
            cacheLong = Integer.parseInt(response.getCacheLong() + "");
            if (cacheLong != 0) mCache.put(cacheKey, response, cacheLong * 60);
            onSuccess(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        if (access) {
            Integer cacheLong;
            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                cacheLong = jsonObject.getInt("cacheLong");
                if (cacheLong != 0) mCache.put(cacheKey, response, cacheLong * 60);
                onSuccess(new JSONObject(response.toString()));
            } catch (Exception e) {
                e.printStackTrace();
                onException(e);
            }

        } else {
            try {
                JSONObject jsonObject = mCache.getAsJSONObject(cacheKey);
                if (jsonObject != null) onSuccess(new JSONObject(jsonObject.toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int SERVICE_UNAVAILABLE = 503;

    protected void onException(Throwable e) {
        String message;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    message = "操作未授权";
                    break;
                case FORBIDDEN:
                    message = "请求被拒绝";
                    break;
                case NOT_FOUND:
                    message = "资源不存在";
                    break;
                case REQUEST_TIMEOUT:
                    message = "服务器执行超时";
                    break;
                case INTERNAL_SERVER_ERROR:
                    message = "服务器内部错误";
                    break;
                case SERVICE_UNAVAILABLE:
                    message = "服务器不可用";
                    break;
                default:
                    message = "网络错误";
                    break;
            }
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof android.net.ParseException || e instanceof MalformedJsonException) {
            message = "解析错误";
        } else if (e instanceof ConnectException) {
            message = "连接失败";
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            message = "证书验证失败";
        } else if (e instanceof ConnectTimeoutException) {
            message = "连接超时";
        } else if (e instanceof java.net.SocketTimeoutException) {
            message = "连接超时";
        } else if (e instanceof java.net.UnknownHostException) {
            message = "主机地址未知";
        } else {
            message = "未知错误";
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", message);
            onFail(jsonObject);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        e.printStackTrace();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


 /*   private void dismissProgress() {
        if (loading != null && loading.isShowing())
            loading.dismiss();
    }*/


    abstract protected void onSuccess(JSONObject response) throws JSONException;

    abstract protected void onFail(JSONObject response) throws JSONException;

}
