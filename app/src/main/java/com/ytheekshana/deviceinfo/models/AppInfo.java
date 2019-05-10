package com.ytheekshana.deviceinfo.models;

import android.graphics.drawable.Drawable;

public class AppInfo {

    private String appName;
    private String versionName;
    private String packageName;
    private Drawable appIcon;

    public AppInfo(String appName, String packageName, String versionName, Drawable appIcon) {
        this.appName = appName;
        this.packageName = packageName;
        this.versionName = versionName;
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }
}