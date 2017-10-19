package com.rothen.blefinder.BLEService;

/**
 * Created by lecros0 on 19/10/2017.
 */

public interface IBLEService {
    void startScan(final long scanPeriod);
    void stopScan();
    boolean isScanning();

}




