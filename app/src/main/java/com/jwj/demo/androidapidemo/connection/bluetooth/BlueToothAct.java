package com.jwj.demo.androidapidemo.connection.bluetooth;

import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jwj.demo.androidapidemo.R;

import java.util.ArrayList;
import java.util.List;

public class BlueToothAct extends ListActivity implements BlueToothManager.DeviceFoundCallBack {

    BlueToothManager blueToothServer;
    List<String> address = new ArrayList();
    List<BluetoothDevice> devices = new ArrayList<>();
    ArrayAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth);
        blueToothServer = new BlueToothManager();
        mAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, address);
        setListAdapter(mAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        BluetoothDevice mDevice = devices.get(position);
    }

    @Override
    public void deviceFound(BluetoothDevice mDevice) {
        address.add(mDevice.getName());
        devices.add(mDevice);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        blueToothServer.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        blueToothServer.unregister(this);
    }
}
