package app.hmprinter.com.Adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import app.hmprinter.com.R

class DeviceListAdapter(
    private val context: Context,
    private val btDeviceName: java.util.ArrayList<String>,
    private val btDeviceAddress: java.util.ArrayList<String>
) : BaseAdapter() {
    private lateinit var address: TextView
    private lateinit var name: TextView

    override fun getCount(): Int {
        return btDeviceName.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.device_item, parent, false)
        name = convertView!!.findViewById(R.id.btName)
        address = convertView.findViewById(R.id.btAddress)
        name.text = btDeviceName[position]
        address.text = btDeviceAddress[position]
        return convertView
    }
}