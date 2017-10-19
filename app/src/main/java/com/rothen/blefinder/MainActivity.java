package com.rothen.blefinder;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.rothen.blefinder.BLEService.BLEService;
import com.rothen.blefinder.BLEService.BLEServiceListener;
import com.rothen.blefinder.BLEService.IBLEService;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private IBLEService bleService;

    private ListView lstVDevices;
    private ToggleButton tgbtnScan;

    private DeviceStrengthCustomAdapter deviceStrengthCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                // Android M Permission check 
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This app needs location access");
            builder.setMessage("Please grant location access so this app can detect beacons.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }
            });
            builder.show();

        }




        lstVDevices = (ListView) findViewById(R.id.lstDevices);

        deviceStrengthCustomAdapter = new DeviceStrengthCustomAdapter(this);
        lstVDevices.setAdapter(deviceStrengthCustomAdapter);

        tgbtnScan = (ToggleButton) findViewById(R.id.tgButtonScan);
        tgbtnScan.setTextOff("Scan");
        tgbtnScan.setTextOn("Scanning");
        tgbtnScan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    bleService = new BLEService(getApplicationContext(), new BLEServiceListener() {
                        @Override
                        public void bluetoothNotActivated() {
                            displayMessage("Bluetooth not activated");
                        }

                        @Override
                        public void scanningResult(String deviceName, String deviceAddress, int signalStrength, int txPower) {
                            deviceStrengthCustomAdapter.newData(deviceName,deviceAddress,signalStrength);
                            displayMessage(deviceName +" " + deviceAddress + "/" + signalStrength + " " + txPower);
                        }

                        @Override
                        public void errorScanAlreadyStarted() {
                            displayMessage("Scan already started");
                        }

                        @Override
                        public void errorApplicationRegistrationFailed() {
                            displayMessage("Application registration failed");
                        }

                        @Override
                        public void errorFeatureUnsupported() {
                            displayMessage("Bluetooth LE Feature unsopported");
                        }

                        @Override
                        public void errorInternal() {
                            displayMessage("Bluetooth LE Internal error");
                        }

                        @Override
                        public void deviceLost(String deviceName, String deviceAddress, int signalStrength, int txPower) {
                            deviceStrengthCustomAdapter.deviceLost(deviceName,deviceAddress);
                        }
                    });
                    bleService.startScan(0);
                }
                else
                {
                    bleService.stopScan();
                }
            }
        });


    }

    @Override
    protected void onStop() {
        if(bleService.isScanning())
        {
            bleService.stopScan();

        }
        super.onStop();
    }

    private void displayMessage(String text)
    {
        //Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
        Log.d("BLEFinder", text);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("BLEFinder", "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
            }
        }
    }
}
