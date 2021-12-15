package app.hmprinter.com.Models;


import android.bluetooth.BluetoothDevice;

import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;

public class PrinterConfig {
    private static int noOfCopies = 1;
    private static String deviceName = "";
    private static BluetoothDevice deviceAddress = null;
    private static PrinterConfig printerConfig = null;
    private static Boolean isPaired = true;
    private static BluetoothConnection selectedDevice = null;

    public static PrinterConfig getInstance() {
        if (printerConfig == null) {

            try {
                printerConfig = new PrinterConfig();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return printerConfig;
    }

    public BluetoothDevice getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(BluetoothDevice deviceAddress) {
        PrinterConfig.deviceAddress = deviceAddress;
    }

    public int getNoOfCopies() {
        return noOfCopies;
    }

    public void setNoOfCopies(int noOfCopies) {
        PrinterConfig.noOfCopies = noOfCopies;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        PrinterConfig.deviceName = deviceName;
    }

    public Boolean getIsPaired() {
        return isPaired;
    }

    public void setIsPaired(Boolean isPaired) {
        PrinterConfig.isPaired = isPaired;
    }

    public BluetoothConnection getSelectedDevice() {
        return selectedDevice;
    }

    public void setSelectedDevice(BluetoothConnection selectedDevice) {
        PrinterConfig.selectedDevice = selectedDevice;
    }
}
