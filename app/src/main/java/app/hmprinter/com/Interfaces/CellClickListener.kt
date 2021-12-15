package app.hmprinter.com.Interfaces

import android.bluetooth.BluetoothDevice

interface CellClickListener {
    fun onCellClickListener(data: BluetoothDevice);
}