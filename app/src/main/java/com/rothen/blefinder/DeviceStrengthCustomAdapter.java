package com.rothen.blefinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rothen.blefinder.model.DeviceCellViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lecros0 on 19/10/2017.
 */

public class DeviceStrengthCustomAdapter extends BaseAdapter {

    private List<DeviceCellViewModel> lstDevices;
    private Context context;


    public DeviceStrengthCustomAdapter(Context c) {
        lstDevices = new ArrayList<>();
        context = c;
    }

    public void newData(String name, String macAddress, int signalStrength)
    {
        boolean found = false;
        for (DeviceCellViewModel device: lstDevices             ) {
            if(device.getDeviceMacAddress().equals(macAddress))
            {
                found=true;
                device.setSignalStrength(signalStrength);
                break;
            }
        }
        if(!found)
        {
            lstDevices.add(new DeviceCellViewModel(name,macAddress,signalStrength));
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lstDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return lstDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null)
        {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            view = vi.inflate(R.layout.device_cell_layout, null);
        }

        TextView txtName = view.findViewById(R.id.txtDeviceName);
        TextView txtMacAddress = view.findViewById(R.id.txtMacAddress);
        ProgressBar progress = view.findViewById(R.id.progressStrength);

        DeviceCellViewModel device = lstDevices.get(i);


        txtName.setText(device.getDeviceName());
        txtMacAddress.setText(device.getDeviceMacAddress());
        progress.setMax(device.getMaxSignalStrength());
        progress.setMin(device.getMinSignalStrength());
        progress.setProgress(device.getSignalStrength());

        return view;
    }

    public void deviceLost(String deviceName, String deviceAddress) {
        boolean found = false;
        for (DeviceCellViewModel device: lstDevices             ) {
            if(device.getDeviceMacAddress().equals(deviceAddress))
            {
                found=true;
                lstDevices.remove(device);
                break;
            }
        }

        notifyDataSetChanged();
    }
}
