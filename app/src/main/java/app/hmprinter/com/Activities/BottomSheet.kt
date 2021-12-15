package app.hmprinter.com.Activities

import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import app.hmprinter.com.Helpers.BluetoothConnectionHelper
import app.hmprinter.com.Helpers.PrintingHelper
import app.hmprinter.com.Interfaces.BottomSheetCallback
import app.hmprinter.com.Models.PrinterConfig
import app.hmprinter.com.R
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Counter(CurrentStatus: Int) {
    private var selectedValue: Int = CurrentStatus
    fun increaseValue() {
        this.selectedValue += 1
    }

    fun decreaseValue() {
        if (selectedValue > 1) {
            this.selectedValue -= 1
        }
    }

    fun getCurrentValue(): Int {
        return this.selectedValue
    }
}

class BottomSheet : BottomSheetDialogFragment() {
    private lateinit var deviceName: String
    private lateinit var deviceAddress: BluetoothDevice
    private var currentPrintNumber: Int = 0
    private lateinit var selectedCurrentValue: Counter
    private lateinit var incBtn: ImageView
    private lateinit var decBtn: ImageView
    private lateinit var crossImageView: ImageView
    private lateinit var pairBtn: Button
    private lateinit var btnTest: Button
    private lateinit var selectedValue: TextView
    private lateinit var deviceTitle: TextView
    private lateinit var printConfig: PrinterConfig
    private var isPaired: Boolean = true
    private lateinit var selectedDevice: BluetoothConnection
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        printConfig = PrinterConfig.getInstance()
        deviceName = printConfig.deviceName
        deviceAddress = printConfig.deviceAddress
        currentPrintNumber = printConfig.noOfCopies
        selectedCurrentValue = Counter(currentPrintNumber)
        isPaired = printConfig.isPaired
        return inflater.inflate(R.layout.bottom_sheet_layout, container, false)
    }

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetTranparent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pairBtn = view.findViewById(R.id.pair_btn)
        incBtn = view.findViewById(R.id.inc_btn_iv)
        crossImageView = view.findViewById(R.id.iv_cross)
        decBtn = view.findViewById(R.id.dec_btn_iv)
        btnTest = view.findViewById(R.id.test_print_btn)
        deviceTitle = view.findViewById(R.id.device_name_tv)
        deviceTitle.text = deviceName
        selectedValue = view.findViewById(R.id.selected_value_tv)
        selectedValue.text = selectedCurrentValue.getCurrentValue().toString()

        if (!isPaired) {
            btnTest.visibility = View.GONE
            pairBtn.visibility = View.VISIBLE
        } else {
            pairBtn.visibility = View.GONE
            btnTest.visibility = View.VISIBLE
        }

        incBtn.setOnClickListener {
            selectedCurrentValue.increaseValue()
            selectedValue.text = selectedCurrentValue.getCurrentValue().toString()
        }
        decBtn.setOnClickListener {
            selectedCurrentValue.decreaseValue()
            selectedValue.text = selectedCurrentValue.getCurrentValue().toString()
        }
        pairBtn.setOnClickListener {
            printConfig.deviceName = deviceName
            printConfig.noOfCopies = selectedCurrentValue.getCurrentValue()
            BluetoothConnectionHelper.PairADevice(
                requireActivity(),
                deviceAddress,
                object : BottomSheetCallback {
                    override fun save() {
                        isPaired = true
                        printConfig.isPaired = true
                        pairBtn.visibility = View.GONE
                        btnTest.visibility = View.VISIBLE

                    }
                }
            ).execute()
        }
        btnTest.setOnClickListener {
            printConfig.deviceName = deviceName
            printConfig.noOfCopies = selectedCurrentValue.getCurrentValue()
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            val device = bluetoothAdapter.getRemoteDevice(deviceAddress.toString())
            selectedDevice = BluetoothConnection(device)
            printConfig.selectedDevice = selectedDevice
            PrintingHelper.getInstance(requireActivity().application)?.testPrinter(selectedDevice)
            findNavControllerSafely()?.navigate(R.id.action_printer_selection_screen_fragment_to_receive_order_screen_fragment)
            dismiss()
        }
        crossImageView.setOnClickListener {
            dismiss()
        }


    }

    /**
     * Extension function to handle nav controller on fragment recreated
     */
    private fun Fragment.findNavControllerSafely(): NavController? {
        return if (isAdded) {
            findNavController()
        } else {
            null
        }
    }
}