package app.hmprinter.com.Fragments

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import app.hmprinter.com.Activities.BottomSheet
import app.hmprinter.com.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SelectPrinterFragment : Fragment() {
    private var bondedDevices: Set<BluetoothDevice>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val bottomSheetFragment = BottomSheet();
        bottomSheetFragment.show(requireActivity().supportFragmentManager, "Bottom Sheet Dialog" )
        super.onCreate(savedInstanceState)
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter != null && bluetoothAdapter.bondedDevices.isNotEmpty()) {
            bondedDevices = bluetoothAdapter.bondedDevices
            println("bondedDevices$bondedDevices")
        } else {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_select_printer, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }



    companion object {

    }
}