package com.ytheekshana.deviceinfo;


class CPUCoreInfo {

    private String coreName;
    private String coreValue;

    CPUCoreInfo(String coreName, String coreValue) {
        this.coreName = coreName;
        this.coreValue = coreValue;
    }

    String getCoreName() {
        return coreName;
    }

    String getCoreValue() {
        return coreValue;
    }
}