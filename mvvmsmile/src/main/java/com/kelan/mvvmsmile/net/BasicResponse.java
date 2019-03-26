package com.kelan.mvvmsmile.net;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 *
 */
public class BasicResponse<T> extends JSONObject {
    private String version;
    private String message;
    private T results;
    private int cacheLong;
    private boolean error;
    private String requestMapping;

    public String getRequestMapping() {
        return requestMapping;
    }

    public void setRequestMapping(String requestMapping) {
        this.requestMapping = requestMapping;
    }

    public int getCacheLong() {
        return cacheLong;
    }

    public BasicResponse setCacheLong(int cacheLong) {
        this.cacheLong = cacheLong;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "\"requestMapping\":\"" + requestMapping +
                "\",\"version\":\"" + version +
                "\",\"message\":\"" + message +
                "\",\"results\":" + new Gson().toJson(results) +
                ",\"cacheLong\":\"" + cacheLong +
                "\",\"error\":\"" + error +
                "\"}";
    }
}
