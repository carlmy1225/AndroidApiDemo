1.声明权限
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />

2.打开蓝牙设备方式
    1)Intent i = new Intent(BlueToothAdapter.ACTION_REQUEST_ENABLE)
    startActivityForResult(i, 2001);
   2)接收广播
    BluetoothAdapter.ACTION_STATE_CHANGED