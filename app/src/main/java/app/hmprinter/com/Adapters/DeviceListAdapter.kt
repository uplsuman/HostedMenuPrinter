package app.hmprinter.com.Adapters
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.hmprinter.com.R
import java.util.*


internal class DeviceListAdapter(
    private val bondedDevices: ArrayList<BluetoothDevice>
) :
    RecyclerView.Adapter<DeviceListAdapter.Holder>() {
    private var selectedDevicePos = -1

    private val applicationUUID: UUID = UUID
        .fromString("00001101-0000-1000-8000-00805F9B34FB")
    var mBluetoothDevice: BluetoothDevice? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.bluetooth_device, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val device = bondedDevices[position]
        holder.tvDeviceName.text = device.name
        holder.itemView.setOnClickListener {
            selectDevice(
                holder,
                position
            )
        }
        if (position == selectedDevicePos) {
            holder.ivIndicator.setImageResource(R.drawable.ic_check_circle)
        } else {
            holder.ivIndicator.setImageResource(R.drawable.ic_circle)
        }
    }

    private fun selectDevice(holder: Holder, position: Int) {
        selectedDevicePos = position
        holder.ivIndicator.setImageResource(R.drawable.ic_check_circle)

//        var socket: BluetoothSocket? = null
//        var connected = true
//        try {
//            socket = mBluetoothDevice?.createRfcommSocketToServiceRecord(applicationUUID)
//            socket?.connect()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } catch (e2: Exception) {
//            connected = false
//        }

        notifyDataSetChanged()
    }



    override fun getItemCount(): Int {
        return bondedDevices.size
    }


    internal class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvDeviceName: TextView = itemView.findViewById(R.id.tv_device_name)
        var ivIndicator: ImageView = itemView.findViewById(R.id.iv_select_indicator)

    }

}
