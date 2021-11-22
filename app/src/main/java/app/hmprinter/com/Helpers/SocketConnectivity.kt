package app.hmprinter.com.Helpers

import android.util.Log
import app.hmprinter.com.API.ApiImplementation
import app.hmprinter.com.Constants.ApiConstant
import app.hmprinter.com.Constants.AppConstant
import app.hmprinter.com.Interfaces.SocketCallbacks
import io.socket.client.IO
import java.lang.Exception
import io.socket.client.Socket;

class SocketConnectivity {
    private lateinit var mSocket: Socket
    private val TAG = SocketConnectivity::class.java.name

    fun connectToSocket(restaurantId: String) {
        try {
            mSocket = IO.socket(ApiConstant.socketConnectionUrl + restaurantId)
            Log.d(TAG, AppConstant.SUCCESSFULLY_CONNECTED_TO_HOSTED_MENU + mSocket.id())
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, AppConstant.FAILED_TO_CONNECT_HOSTED_MENU + e.message)
        }

        mSocket.connect()

        //Register all the listener and callbacks here.
//        mSocket.on(Socket.EVENT_CONNECT, onConnect)
//        mSocket.on("message", onMessage)
    }
}