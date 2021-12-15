package app.hmprinter.com.Helpers

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import app.hmprinter.com.Constants.AppConstant
import app.hmprinter.com.Interfaces.BottomSheetCallback
import java.io.IOException
import java.lang.reflect.Method

class BluetoothConnectionHelper {

    class PairADevice(c: Context, device: BluetoothDevice,
                      private val callback: BottomSheetCallback
    ) :
        AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = false
        private val unPairedDevices: BluetoothDevice = device
        @SuppressLint("StaticFieldLeak")
        private val context: Context = c

        override fun onPreExecute() {
            super.onPreExecute()
            Log.i("IsConnected", "Pairing to Bluetooth....")
        }

        @Throws(Exception::class)
        fun createBond(btDevice: BluetoothDevice): Boolean {
            val class1 = Class.forName(AppConstant.BLUETOOTH_DEVICE_CLASS_NAME)
            val createBondMethod: Method = class1.getMethod(AppConstant.CREATE_BOND)
            return createBondMethod.invoke(btDevice) as Boolean
        }

        override fun doInBackground(vararg p0: Void?): String? {
            try {
                connectSuccess = createBond(unPairedDevices)
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if (!connectSuccess) {
                Log.i("IsConnected", "Couldn't Pair")

                val toast = Toast.makeText(context, AppConstant.COULD_NOT_ABLE_TO_PAIR, Toast.LENGTH_SHORT)
                toast.show()
            } else {
                Log.i("IsConnected", "Bluetooth Paired & Connected")

                val toast =
                    Toast.makeText(context, AppConstant.PAIRED_AND_CONNECTED, Toast.LENGTH_SHORT)
                toast.show()
                callback.save()
            }
        }
    }
}