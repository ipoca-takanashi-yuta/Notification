package jp.ipoca.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat

class Notifier : BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {
        // ただ通知するだけ
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 9999, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setTicker("Ticker")
                .setContentTitle("ContentTitle")
                .setContentText("ContentText")
                .setSmallIcon(R.drawable.ic_edit)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_LIGHTS)
                .setAutoCancel(true)
        val manager = context?.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(MainActivity.NOTIFICATION_ID, builder.build())

        // 同時にPopUpDialogの表示もする
        val intent2 = Intent(context, PopUpActivity::class.java).apply {
            putExtra(Intent.EXTRA_TEXT, p1?.getStringExtra(Intent.EXTRA_TEXT))
            // 別タスクでactivityを起動
            // ManifestのtaskAffinity、excludeFromRecentsを設定
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent2)
    }
}