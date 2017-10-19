package com.rothen.blefinder.model;

/**
 * Created by lecros0 on 19/10/2017.
 */

public class DeviceCellViewModel {

    String deviceName;
    String deviceMacAddress;
    int signalStrength;
    int maxSignalStrength;
    int minSignalStrength;


    public DeviceCellViewModel() {
        maxSignalStrength=-30;
        minSignalStrength=-110;
    }

    public DeviceCellViewModel(String deviceName, String deviceMacAddress, int signalStrength) {

        this.deviceName = deviceName;
        this.deviceMacAddress = deviceMacAddress;
        setSignalStrength(signalStrength);
        maxSignalStrength=-30;
        minSignalStrength=-90;
    }

    public int getMaxSignalStrength() {
        return maxSignalStrength;
    }

    public int getMinSignalStrength() {
        return minSignalStrength;
    }

    public String getDeviceName() {
        if(deviceName == null)
        {
            return "Unknown";
        }
        else {
            return deviceName;
        }
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceMacAddress() {
        return deviceMacAddress;
    }

    public void setDeviceMacAddress(String deviceMacAddress) {
        this.deviceMacAddress = deviceMacAddress;
    }

    public int getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(int signalStrength) {
        this.signalStrength = signalStrength;

    }
}
