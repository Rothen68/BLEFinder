package com.rothen.blefinder.BLEService;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

/**
 * Service for using Bluetooth LE client
 * Created by lecros0 on 19/10/2017.
 */

public class BLEService implements IBLEService {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            String deviceName = result.getDevice().getName();
            String deviceAddress = result.getDevice().getAddress();
            int signalStrength = result.getRssi();
            int txPower = result.getTxPower();

            switch (callbackType) {
                case ScanSettings.CALLBACK_TYPE_ALL_MATCHES:
                case ScanSettings.CALLBACK_TYPE_FIRST_MATCH:
                    listener.scanningResult(deviceName, deviceAddress, signalStrength, txPower);
                    break;
                case ScanSettings.CALLBACK_TYPE_MATCH_LOST:
                    listener.deviceLost(deviceName, deviceAddress, signalStrength, txPower);
                    break;
            }

            listener.scanningResult(deviceName, deviceAddress, signalStrength, txPower);

            super.onScanResult(callbackType, result);
        }

        @Override
        public void onScanFailed(int errorCode) {
            switch (errorCode) {
                case SCAN_FAILED_ALREADY_STARTED:
                    listener.errorScanAlreadyStarted();
                    break;
                case SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
                    listener.errorApplicationRegistrationFailed();
                    break;
                case SCAN_FAILED_FEATURE_UNSUPPORTED:
                    listener.errorFeatureUnsupported();
                    break;
                case SCAN_FAILED_INTERNAL_ERROR:
                    listener.errorInternal();
                    break;
            }
            super.onScanFailed(errorCode);
        }
    };

    private BLEServiceListener listener;
    private boolean isScanning;


    public BLEService(Context context, BLEServiceListener listener) {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        this.listener = listener;
        isScanning = false;

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            listener.bluetoothNotActivated();
        }
    }

    public void startScan(final long scanPeriod) {

        if (bluetoothAdapter != null && isScanning == false) {
            bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

            if (scanPeriod > 0) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isScanning = false;
                        bluetoothLeScanner.stopScan(scanCallback);
                    }
                }, scanPeriod);
            }
            isScanning = true;
            List<ScanFilter> list = new LinkedList<>();
            ScanSettings settings = new ScanSettings.Builder()
                    .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                    .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
            bluetoothLeScanner.startScan(null, settings, scanCallback);
        }
    }

    public void stopScan() {
        if (isScanning) {
            bluetoothLeScanner.stopScan(scanCallback);
        }
    }

    public boolean isScanning()
    {
        return isScanning;
    }
}
