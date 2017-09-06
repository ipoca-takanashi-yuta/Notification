package jp.ipoca.notification

import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.SeekBar
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 通常通知
        findViewById(R.id.normal_button).setOnClickListener {
            val manager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(NOTIFICATION_ID, makeBuilder().build())
        }

        // BigTextStyle
        findViewById(R.id.big_text_button).setOnClickListener {
            val bigTextStyle = NotificationCompat.BigTextStyle(makeBuilder())
                    .bigText("BigText")
                    .setBigContentTitle("BigContentTitle")
                    .setSummaryText("SummaryText")
            val manager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(NOTIFICATION_ID, bigTextStyle.build())
        }

        // BitPictureStyle
        findViewById(R.id.big_picture_button).setOnClickListener {
            val bigPicture = BitmapFactory.decodeResource(resources, R.drawable.ic_close)
            val bigPictureStyle = NotificationCompat.BigPictureStyle(makeBuilder())
                    .bigPicture(bigPicture)
                    .setBigContentTitle("BigContentTitle")
                    .setSummaryText("SummaryText")
            val manager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(NOTIFICATION_ID, bigPictureStyle.build())
        }

        // InboxStyle
        findViewById(R.id.inbox_button).setOnClickListener {
            val inboxStyle = NotificationCompat.InboxStyle(makeBuilder())
            inboxStyle.setBigContentTitle("BigContentTitle")
            inboxStyle.setSummaryText("SummaryText")
            for (i in 0..9) {
                inboxStyle.addLine("Line" + (i + 1))
            }
            val manager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(NOTIFICATION_ID, inboxStyle.build())
        }

        // 発行した通知の削除
        findViewById(R.id.cancel_button).setOnClickListener {
            val manager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            manager.cancel(NOTIFICATION_ID)     // 通知のIDが同じものを削除できる
        }

        // 数秒後に通知。ポップアップのダイアログも同時に表示する
        val afterButton = findViewById(R.id.after_notification_button) as Button
        val seekBar = findViewById(R.id.seek_bar) as SeekBar
        val alarmCancel = findViewById(R.id.alarm_cancel_button) as Button
        var pendingIntent: PendingIntent? = null

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
               afterButton.text = "${p0?.progress}秒後に通知"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        afterButton.setOnClickListener {
            // intentを発行する時刻
            val time = Calendar.getInstance().apply { add(Calendar.SECOND, seekBar.progress) }
            // BroadcastReceiverを起動し、通知を発行
            val intent = Intent(this, Notifier::class.java).apply {
                // Dialogで表示するメッセージを伝える
                putExtra(Intent.EXTRA_TEXT, "${seekBar.progress}秒後の通知です")
            }
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val manager = getSystemService(ALARM_SERVICE) as AlarmManager
            manager.set(AlarmManager.RTC_WAKEUP, time.timeInMillis, pendingIntent)
        }
        alarmCancel.setOnClickListener {
            if (pendingIntent != null) {
                val manager = getSystemService(ALARM_SERVICE) as AlarmManager
                manager.cancel(pendingIntent)
            }
        }
    }

    // 各StyleではNotificationCompat.Builderをラップするので、Builderを作成するメソッドで共通化
    private fun makeBuilder(): NotificationCompat.Builder {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 9999, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_add)
        return NotificationCompat.Builder(applicationContext)
                .setContentIntent(pendingIntent)
                .setTicker("Ticker")
                .setContentTitle("ContentTitle")
                .setContentText("ContentText")
                .setSmallIcon(R.drawable.ic_edit)
                .setLargeIcon(largeIcon)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_LIGHTS)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                .addAction(R.drawable.ic_close, "アクション", pendingIntent)   // 通知にボタンを追加
    }

    companion object {
        val NOTIFICATION_ID = 10000
    }
}
