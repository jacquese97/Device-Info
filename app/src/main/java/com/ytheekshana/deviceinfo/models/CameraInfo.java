package com.ytheekshana.deviceinfo.models;


public class CameraInfo {

    private String featureName;
    private String featureValue;

    public CameraInfo(String featureName, String featureValue) {
        this.featureName = featureName;
        this.featureValue = featureValue;
    }

    public String getFeatureName() {
        return featureName;
    }

    public String getFeatureValue() {
        return featureValue;
    }
}