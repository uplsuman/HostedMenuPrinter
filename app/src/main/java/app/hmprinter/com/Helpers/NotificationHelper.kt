package app.hmprinter.com.Helpers


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import app.hmprinter.com.R


class NotificationHelper internal constructor(var context: Context) {
    private var HM_ORDER = "HostedMenuOrderChannel"
    private var HM_ORDER_ID = "HostedMenuOrderChannelID"
    private var notificationChannel: NotificationChannel? = null
    private var notificationManager: NotificationManager? = null

    fun sendNotification(orderId: String, Message: String?) {
        playNotificationSound()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification: NotificationCompat.Builder = NotificationCompat.Builder(
                context, HM_ORDER_ID
            )
                .setContentTitle("#$orderId")
                .setContentText(Message)
                .setSmallIcon(R.mipmap.ic_launcher)
            notificationManager!!.notify(1, notification.build())
        } else {
            val notification: Notification = NotificationCompat.Builder(
                context
            )
                .setContentTitle("#$orderId")
                .setContentText(Message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build()
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(111, notification)
        }
    }

    private fun playNotificationSound() {
        try {
            val alarmSound =
                Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.applicationContext.packageName + "/" + R.raw.new_order)
            val r = RingtoneManager.getRingtone(context, alarmSound)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(HM_ORDER_ID, HM_ORDER, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel!!.enableLights(true)
            notificationChannel!!.enableVibration(true)
            notificationChannel!!.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager!!.createNotificationChannel(notificationChannel!!)
        }
    }
}