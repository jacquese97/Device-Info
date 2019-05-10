package com.ytheekshana.deviceinfo.models;

public class SensorInfo {

    private String sensorName;
    private String vendorName;
    private String sensorType;
    private String wakeUpType;
    private String sensorPower;

    public SensorInfo(String sensorName, String vendorName, String sensorType, String wakeUpType, String sensorPower) {
        this.sensorName = sensorName;
        this.vendorName = vendorName;
        this.sensorType = sensorType;
        this.wakeUpType = wakeUpType;
        this.sensorPower = sensorPower;
    }

    public String getSensorName() {
        return sensorName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getSensorType() {
        return sensorType;
    }

    public String getWakeUpType() {
        return wakeUpType;
    }

    public String getSensorPower() {
        return sensorPower;
    }

}