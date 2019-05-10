package com.ytheekshana.deviceinfo.models;


public class ThermalInfo {

    private String thermalName;
    private String thermalValue;

    public ThermalInfo(String thermalName, String thermalValue) {
        this.thermalName = thermalName;
        this.thermalValue = thermalValue;
    }

    public String getThermalName() {
        return thermalName;
    }

    public String getThermalValue() {
        return thermalValue;
    }
}