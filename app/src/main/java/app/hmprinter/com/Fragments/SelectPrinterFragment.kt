package app.hmprinter.com.Fragments


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.hmprinter.com.Activities.BottomSheet
import app.hmprinter.com.Adapters.DeviceListAdapter
import app.hmprinter.com.Constants.AppConstant
import app.hmprinter.com.Helpers.DataStoreManager
import app.hmprinter.com.Interfaces.BottomSheetCallback
import app.hmprinter.com.Models.PrinterConfig
import app.hmprinter.com.R
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import kotlinx.coroutines.launch

class SelectPrinterFragment : Fragment(), BottomSheetCallback {
    private val TAG = SelectPrinterFragment::class.java.name
    private var btAdapter: BluetoothAdapter? = null
    private lateinit var refreshBtn: ImageView;
    private var btDevicesName = ArrayList<String>()
    private var btDevicesAddress = ArrayList<String>()
    private lateinit var bottomSheetFragment: BottomSheet
    private lateinit var logout: ImageView
    private lateinit var lvDeviceList: ListView
    private lateinit var lvAdapter: DeviceListAdapter
    private lateinit var dataStoreManager: DataStoreManager
    var unPairedBTDevicesAddress = ArrayList<String>()
    private var PairedBTDevicesAddress = ArrayList<String>()

    companion object {
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_bluetoothAdapter: BluetoothAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBtAdapter()
        initBtPermissions()
        bottomSheetFragment = BottomSheet()
        dataStoreManager = DataStoreManager(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =
            inflater.inflate(R.layout.fragment_select_printer, container, false)
        initUi(view)
        onClicks()
        initListView()
        return view
    }

    /**
     * This method checks & requests for bluetooth permissions
     */
    private fun initBtPermissions() {
        // Device doesn't support Bluetooth
        if (btAdapter == null) Toast.makeText(
            requireContext().applicationContext,
            AppConstant.BT_NOT_SUPPORTED,
            Toast.LENGTH_SHORT
        ).show() else if (btAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, AppConstant.REQUEST_ENABLE_BT)
        }

        val permission1 =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH)
        val permission2 =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_ADMIN)
        val permission3 = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val permission4 = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission1 != PackageManager.PERMISSION_GRANTED
            || permission2 != PackageManager.PERMISSION_GRANTED
            || permission3 != PackageManager.PERMISSION_GRANTED
            || permission4 != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                AppConstant.REQUEST_ENABLE_BT
            )
        } else {
            Log.d(TAG, "Permissions Granted")
            checkPairedBTDevices()
            scanNearByBtDevices()

        }
    }

    /**
     * This method initiate near by device scan receiver
     */
    private fun scanNearByBtDevices() {
        if (btAdapter?.isDiscovering == true) {
            btAdapter?.cancelDiscovery()
        }
        print("WORKING")
        var filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        requireContext().registerReceiver(receiver, filter)
        filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        requireContext().registerReceiver(receiver, filter)
        filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        requireContext().registerReceiver(receiver, filter)
        btAdapter?.startDiscovery()
    }

    /**
     * This method fetch all previously paired devices
     */
    private fun checkPairedBTDevices() {
        if (btAdapter == null) {
            Log.d(TAG, "\nBluetooth NOT supported. Aborting.")
            return
        } else {
            if (btAdapter!!.isEnabled) {
                Log.d(TAG, "\nBluetooth is enabled...")

                // Listing paired devices
                Log.d(TAG, "\nPaired Devices are:")
                val bluetoothDevicesList = BluetoothPrintersConnections().list
                    for (device in bluetoothDevicesList!!) if (!btDevicesAddress.contains(device.device.address)) {
                        btDevicesName.add(device.device.name)
                        btDevicesAddress.add(device.device.address)
                        PairedBTDevicesAddress.add(device.device.address)
                        Log.d(TAG, "\nDevice Name: ${device.device.name}")
                    }
            } else {
                //Prompt user to turn on Bluetooth
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, AppConstant.REQUEST_ENABLE_BT)
            }
        }
    }

    /**
     * Receiver to scan near by devices
     */
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.

                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address

                    val msg: String = if (deviceName.isNullOrBlank()) {
                        deviceHardwareAddress
                        //                        if (!btDevicesAddress.contains(deviceHardwareAddress)) {
                        //                            btDevicesName.add("")
                        //                            btDevicesAddress.add(deviceHardwareAddress)
                        //                        }
                    } else {
                        if (!btDevicesAddress.contains(deviceHardwareAddress)) {
                            btDevicesName.add(deviceName)
                            btDevicesAddress.add(deviceHardwareAddress)
                            unPairedBTDevicesAddress.add(deviceHardwareAddress)
                        }
                        "$deviceName $deviceHardwareAddress"
                    }

                    initListView()
                    println(msg)
                    Log.d("DISCOVERING-DEVICE", msg)
                    lvAdapter.notifyDataSetChanged()

                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {

                    Log.d("DISCOVERING-STARTED", "isDiscovering")
                    lvAdapter.notifyDataSetChanged()
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.d("DISCOVERING-FINISHED", "FinishedDiscovering")
                    lvAdapter.notifyDataSetChanged()
//                    refreshBtn.clearAnimation();
                }
            }
        }
    }

    /**
     * This method gets triggered on saving a device connection
     */
    override fun save() {
        findNavController()
            .navigate(R.id.action_printer_selection_screen_fragment_to_receive_order_screen_fragment)
    }

    /**
     * This method initiate bluetooth adapter
     */
    private fun initBtAdapter() {
        btAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    /**
     * onActivityResult to handle bluetooth permissions
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.REQUEST_ENABLE_BT) {
            checkPairedBTDevices()
            scanNearByBtDevices()
        }
    }

    /**
     *  This method initiate list view
     */
    @SuppressLint("LongLogTag")
    private fun initListView() {
        lvAdapter = DeviceListAdapter(requireActivity(), btDevicesName, btDevicesAddress)
        lvDeviceList.adapter = lvAdapter
        lvDeviceList.setOnItemClickListener { parent, view, position, id ->
            val element = lvDeviceList.getItemAtPosition(position)
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            val device = bluetoothAdapter.getRemoteDevice(btDevicesAddress[element as Int])
            val printerConfig: PrinterConfig = PrinterConfig.getInstance()
            printerConfig.noOfCopies = 1
            printerConfig.deviceName = btDevicesName[element]
            printerConfig.deviceAddress = device
            printerConfig.isPaired =
                !(unPairedBTDevicesAddress.toSet().minus(PairedBTDevicesAddress.toSet())).contains(
                    btDevicesAddress[element]
                )
            bottomSheetFragment.show(
                requireActivity().supportFragmentManager,
                AppConstant.BOTTOM_SHEET_DIALOG_TAG
            )
        }

    }

    /**
     * Function for clear data from data store logout
     */
    private fun logout()=lifecycleScope.launch {
        dataStoreManager.clear()
    }

    /**
     *  This method handles onclicks
     */
    private fun onClicks() {
        logout.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_baseline_info_24)
                .setTitle("Logout")
                .setMessage("Do you Want to Logout ?")
                .setPositiveButton("Yes"
                ) { dialog, which ->
                    logout()
            findNavController()
                .navigate(R.id.action_printer_selection_screen_fragment_to_login_screen_fragment)
                }
                .setNegativeButton("No", null).create()
            with(dialog) {
                show()
                getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.orange))
                getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.orange))
            }
        }

        refreshBtn.setOnClickListener {
            val anim: Animation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_anti_clockwise)
            refreshBtn.startAnimation(anim)
            btDevicesName.clear()
            btDevicesAddress.clear()
            checkPairedBTDevices()
            scanNearByBtDevices()
        }
    }

    /**
     *  This method initialize UI views
     */
    private fun initUi(view: View) {
        lvDeviceList = view.findViewById(R.id.lv_device_list)
        logout = view.findViewById(R.id.show_pass_btn)
        refreshBtn = view.findViewById(R.id.refresh_btn)
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().unregisterReceiver(receiver)
    }


}