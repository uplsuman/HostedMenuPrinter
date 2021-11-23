package app.hmprinter.com.Adapters

import android.bluetooth.BluetoothDevice
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.hmprinter.com.Models.BluetoothDevicesModel
import app.hmprinter.com.R

class CustomDeviceAdapter(val deviceList: ArrayList<BluetoothDevicesModel>) : RecyclerView.Adapter<CustomDeviceAdapter.ViewHolder>(), View.OnClickListener {

    private var bondedDevices: java.util.ArrayList<BluetoothDevice>? = null
    private var selectedDevicePos = -1

//    fun CustomDeviceAdapter(
//        bondedDevices: java.util.ArrayList<BluetoothDevice>,
//    ) {
//        this.bondedDevices = bondedDevices
//        for (i in bondedDevices.indices) {
//            if (bondedDevices[i].name.equals(mPrinterName, ignoreCase = true)) {
//                selectedDevicePos = i
//            }
//        }
//    }

        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.bluetooth_device, parent, false)
            return ViewHolder(v)
        }

        //this method is binding the data on the list
//        override fun onBindViewHolder(holder: CustomDeviceAdapter.ViewHolder, position: Int) {
//            holder.bindItems(deviceList[position])
//        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindItems(deviceList[position])
        holder.itemView.setOnClickListener(View.OnClickListener { v: View? ->
            Log.d(TAG, "\nDevice position: $position,$selectedDevicePos")
            selectDevice(
                holder,
                position
            )
        })
        if (position == selectedDevicePos) {
            holder.iv_select_indicator.setImageResource(R.drawable.ic_check_circle)
        } else {
            holder.iv_select_indicator.setImageResource(R.drawable.ic_circle)
        }
    }

    private fun selectDevice(holder: ViewHolder, position: Int) {
        selectedDevicePos = position
        holder.iv_select_indicator.setImageResource(R.drawable.ic_check_circle)
    }

        //this method is giving the size of the list
        override fun getItemCount(): Int {
            return deviceList.size
        }

        //the class is hodling the list view
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val iv_select_indicator  = itemView.findViewById(R.id.iv_select_indicator) as ImageView
            val textViewName = itemView.findViewById(R.id.tv_device_name) as TextView

            fun bindItems(device: BluetoothDevicesModel) {
                textViewName.text = device.name
//                val textViewAddress  = itemView.findViewById(R.id.textViewAddress) as TextView
//                textViewAddress.text = device.address
            }
        }

    override fun onClick(V: View?) {
        TODO("Not yet implemented")
    }

}