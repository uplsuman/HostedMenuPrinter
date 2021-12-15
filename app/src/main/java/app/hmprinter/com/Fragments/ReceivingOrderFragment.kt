package app.hmprinter.com.Fragments

import android.app.AlertDialog

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import app.hmprinter.com.Activities.Counter
import app.hmprinter.com.Helpers.DataStoreManager
import app.hmprinter.com.Helpers.PrintingHelper
import app.hmprinter.com.Helpers.SocketInstance
import app.hmprinter.com.Models.PrinterConfig
import app.hmprinter.com.Models.RestaurantResponse
import app.hmprinter.com.Models.SocketResponse
import app.hmprinter.com.R
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.util.*
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch


class ReceivingOrderFragment : Fragment() {
    private val TAG = ReceivingOrderFragment::class.java.name
    private lateinit var logoutBtn: ImageView
    private lateinit var DeviceNameTv: TextView
    private lateinit var PrintCountTv: TextView
    private lateinit var CurrentDeviceName: String
    private lateinit var selectedDevice: BluetoothConnection
    private var CurrentPrintCount: Int = 0
    private lateinit var changeDeviceTV: TextView
    private lateinit var decBtn: ImageView
    private lateinit var incBtn: ImageView
    private lateinit var selectedCurrentValue: Counter
    private lateinit var dataStoreManager: DataStoreManager
    private var btAdapter: BluetoothAdapter? = null
    private var btDevicesAddress = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStoreManager = DataStoreManager(requireActivity())

    }

    private fun initBtAdapter() {
        btAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    /**
     *  This method fetch the restaurant data from DataStore
     */
    private fun getRestaurantData() {
        dataStoreManager.restaurantData.asLiveData().observe(viewLifecycleOwner, Observer {
            println("getRestaurantData $it")
            val gson = Gson()
            restaurantData = gson.fromJson(it, RestaurantResponse::class.java)
            initSocketConnection()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_receiving_order, container, false)
        getRestaurantData()
        val printerConfig = PrinterConfig.getInstance()
        CurrentDeviceName = printerConfig.deviceName
        CurrentPrintCount = printerConfig.noOfCopies
        selectedDevice = printerConfig.selectedDevice
        btDevicesAddress.add(printerConfig.selectedDevice.device.address)
        initBtAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
        onClicks()
        DeviceNameTv.text = CurrentDeviceName
        PrintCountTv.text = CurrentPrintCount.toString()
        selectedCurrentValue = Counter(CurrentPrintCount)
    }

    /**
     * Function for clear data from data store logout
     */
    private fun logout() = lifecycleScope.launch {
        dataStoreManager.clear()
    }


    /**
     *  This method connects to socket & listens for socket payloads
     *  on receiving payload, the print gets triggered
     */
    private fun initSocketConnection() {
        val onConnect = Emitter.Listener {
            activity?.runOnUiThread {
                println("Socket Connected")
            }
        }

        val onMessage = Emitter.Listener { args ->
            val data = args[0] as JSONObject
            println("Socket Data$data")

            val gson = GsonBuilder().create()
            val socketResponse: SocketResponse =
                gson.fromJson(args[0].toString(), SocketResponse::class.java)

            activity?.runOnUiThread {
                PrintingHelper.getInstance(requireContext())?.printQrReceipt(
                    socketResponse,
                    restaurantData,
                    selectedCurrentValue.getCurrentValue(),
                    selectedDevice
                )
            }
        }

        val mSocket = SocketInstance().getSocketInstance(restaurantId = restaurantData.result!!.Id)
        mSocket.connect()
        mSocket.on(Socket.EVENT_CONNECT, onConnect)
        mSocket.on("message", onMessage)

    }

    /**
     *  This method handles onclick
     */
    private fun onClicks() {
        logoutBtn.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_baseline_info_24)
                .setTitle("Logout")
                .setMessage("Do you Want to Logout ?")
                .setPositiveButton(
                    "Yes"
                ) { dialog, which ->
                    logout()
                    findNavController()
                        .navigate(R.id.action_receive_order_screen_fragment_to_login_screen_fragment)
                }
                .setNegativeButton("No", null).create()
            with(dialog) {
                show()
                getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.orange))
                getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.orange))
            }
        }
        changeDeviceTV.setOnClickListener {
            if (btAdapter?.isDiscovering == true) {
                btAdapter?.cancelDiscovery()
            }

            requireContext().unregisterReceiver(receiver1)

            findNavController()
                .navigate(R.id.action_receive_order_screen_fragment_to_printer_selection_screen_fragment)
        }
        decBtn.setOnClickListener {
            selectedCurrentValue.decreaseValue()
            PrintCountTv.text = selectedCurrentValue.getCurrentValue().toString()
        }
        incBtn.setOnClickListener {
            selectedCurrentValue.increaseValue()
            PrintCountTv.text = selectedCurrentValue.getCurrentValue().toString()
        }
    }

    /**
     *  This method initialize UI views
     */
    private fun initUi(view: View) {
        DeviceNameTv = view.findViewById(R.id.device_name_tv)
        PrintCountTv = view.findViewById(R.id.print_count_tv)
        logoutBtn = view.findViewById(R.id.show_pass_btn)
        incBtn = view.findViewById(R.id.inc_btn_iv)
        decBtn = view.findViewById(R.id.dec_btn_iv)
        changeDeviceTV = view.findViewById(R.id.change_device_tv)
    }


    /**
     * This method initiate near by device scan receiver1
     */
    private fun scanNearByBtDevices() {
        if (btAdapter?.isDiscovering == true) {
            btAdapter?.cancelDiscovery()
        }
        print("WORKING")
        var filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        requireContext().registerReceiver(receiver1, filter)
        filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        requireContext().registerReceiver(receiver1, filter)
        filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        requireContext().registerReceiver(receiver1, filter)
        btAdapter?.startDiscovery()
    }

    /**
     * Receiver to scan near by devices
     */
    private val receiver1 = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!

                    btDevicesAddress.add(device.address)
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    btDevicesAddress.clear()
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    navigateBack()
                }
            }
        }
    }

    /**
     * if device is disconnected navigate back to select printer screen
     */
    private fun navigateBack() {
        if (!btDevicesAddress.contains(selectedDevice.device.address)) {
            if (btAdapter?.isDiscovering == true) {
                btAdapter?.cancelDiscovery()
            }

            requireContext().unregisterReceiver(receiver1)

            findNavController()
                .navigate(R.id.action_receive_order_screen_fragment_to_printer_selection_screen_fragment)
        } else {
            scanNearByBtDevices()
        }
    }

    companion object {
        private lateinit var restaurantData: RestaurantResponse
    }

}