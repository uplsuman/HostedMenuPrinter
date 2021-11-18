package app.hmprinter.com.Helpers

import android.util.Log
import app.hmprinter.com.Interfaces.SocketCallbacks
import io.socket.client.IO
import java.lang.Exception
import io.socket.client.Socket;

class SocketConnectivity {
    private lateinit var mSocket: Socket

    fun connectToSocket() {
        //Let's connect to our Chat room! :D
        //Let's connect to our Chat room! :D
        try {

            //This address is the way you can connect to localhost with AVD(Android Virtual Device)
            mSocket = IO.socket("ApiConstant.socketConnectionUrl + restaurantId")
            Log.d("success", "suceess" + mSocket.id())
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect" + e.message)
        }

        mSocket.connect()
        //Register all the listener and callbacks here.
        //Register all the listener and callbacks here.
//        mSocket.on(Socket.EVENT_CONNECT, onConnect)
//        mSocket.on("message", onMessage)
    }
}