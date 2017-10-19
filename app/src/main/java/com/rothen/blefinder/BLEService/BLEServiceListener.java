package com.rothen.blefinder.BLEService;

public interface BLEServiceListener
{
    void bluetoothNotActivated();

    void scanningResult(String deviceName, String deviceAddress, int signalStrength, int txPower);

    void errorScanAlreadyStarted();

    void errorApplicationRegistrationFailed();

    void errorFeatureUnsupported();

    void errorInternal();

    void deviceLost(String deviceName, String deviceAddress, int signalStrength, int txPower);
}
