package app.hmprinter.com.Helpers

import android.util.Log
import app.hmprinter.com.Constants.ApiConstant
import app.hmprinter.com.Constants.AppConstant
import io.socket.client.IO
import java.lang.Exception
import io.socket.client.Socket;

class SocketInstance {
    private lateinit var mSocket: Socket
    private val TAG = SocketInstance::class.java.name

    fun getSocketInstance(restaurantId: String): Socket {
        Log.d(TAG, ApiConstant.socketConnectionUrl + restaurantId)
        try {
            mSocket = IO.socket(ApiConstant.socketConnectionUrl + restaurantId)
            Log.d(TAG, AppConstant.SUCCESSFULLY_CONNECTED_TO_HOSTED_MENU + mSocket.id())
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, AppConstant.FAILED_TO_CONNECT_HOSTED_MENU + e.message)
        }

        return mSocket


    }
}