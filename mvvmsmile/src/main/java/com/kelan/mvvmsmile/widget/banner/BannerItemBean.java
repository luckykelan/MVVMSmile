package com.kelan.mvvmsmile.widget.banner;

/**
 * Created by wanghua on 2018/11/21.
 * Description：
 */

public class BannerItemBean {
    Object img_path;
    String title;
    int id;
    int type;

    public BannerItemBean(Object img_path, String title, int id, int type) {
        this.img_path = img_path;
        this.title = title;
        this.id = id;
        this.type = type;
    }

    public BannerItemBean(Object img_path) {
        this.img_path = img_path;
    }

    public BannerItemBean() {
    }

    public Object getImg_path() {
        return img_path;
    }

    public void setImg_path(Object img_path) {
        this.img_path = img_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"img_path\":")
                .append(img_path);
        sb.append(",\"title\":\"")
                .append(title).append('\"');
        sb.append(",\"id\":")
                .append(id);
        sb.append(",\"type\":")
                .append(type);
        sb.append('}');
        return sb.toString();
    }
}
