package app.hmprinter.com.Fragments


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.hmprinter.com.R
import android.content.Intent
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.hmprinter.com.Adapters.CustomDeviceAdapter
import app.hmprinter.com.Constants.AppConstant
import app.hmprinter.com.Models.BluetoothDevicesModel


class SelectPrinterFragment : Fragment() {
    private val TAG = SelectPrinterFragment::class.java.name
    var btAdapter: BluetoothAdapter? = null
    private lateinit var btDevices: Set<BluetoothDevice>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_select_printer, container, false)
        initBtAdapter()
        checkBTState();
        return view
    }

    private fun initBtAdapter() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();

    }

    private fun checkBTState() {
        // Checks for the Bluetooth support and then makes sure it is turned on
        // If it isn't turned on, request to turn it on
        // List paired devices
        if (btAdapter == null) {
            Log.d(TAG, "\nBluetooth NOT supported. Aborting.")
            return
        } else {
            if (btAdapter!!.isEnabled) {
                Log.d(TAG, "\nBluetooth is enabled...")

                // Listing paired devices
                Log.d(TAG, "\nPaired Devices are:")

                val devices = btAdapter!!.bondedDevices
                btDevices = devices
                for (device in devices) {
                    Log.d(TAG, "\nDevice Name: ${device.name}")
                }
            } else {
                //Prompt user to turn on Bluetooth
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, AppConstant.REQUEST_ENABLE_BT)
            }
        }
    }

    /* It is called when an activity completes.*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.REQUEST_ENABLE_BT) {
            checkBTState()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv_device_list: RecyclerView = view.findViewById(R.id.rv_device_list)
        rv_device_list.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false)

        val devices = ArrayList<BluetoothDevicesModel>()
        for (device in btDevices) {
            devices.add(BluetoothDevicesModel(device.name) { device.address })
        }


        val adapter = CustomDeviceAdapter(devices)
        rv_device_list.adapter = adapter
    }



    companion object {
    }
}