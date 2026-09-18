package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.SmsMessage
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class SmsReceiver : BroadcastReceiver() {

    companion object {

        private const val CHANNEL_ID =
            "BEACON_EMERGENCY_CHANNEL"

        private const val NOTIFICATION_ID =
            999
    }


    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        if (intent.action !=
            "android.provider.Telephony.SMS_RECEIVED"
        ) {
            return
        }


        val bundle =
            intent.extras ?: return


        val pdus =
            bundle["pdus"] as? Array<*> ?: return


        var senderNumber = ""
        var completeMessage = ""


        for (pdu in pdus) {

            val smsMessage =

                if (Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.M
                ) {

                    val format =
                        bundle.getString("format")

                    SmsMessage.createFromPdu(
                        pdu as ByteArray,
                        format
                    )

                } else {

                    @Suppress("DEPRECATION")
                    SmsMessage.createFromPdu(
                        pdu as ByteArray
                    )
                }


            senderNumber =
                smsMessage.originatingAddress ?: ""

            completeMessage +=
                smsMessage.messageBody ?: ""
        }


        // ==========================================
        // EMERGENCY MESSAGE RECEIVED
        // ==========================================

        if (
            completeMessage.contains(
                "BEACON EMERGENCY ALERT!",
                ignoreCase = true
            )
        ) {

            showEmergencyNotification(
                context,
                senderNumber,
                completeMessage
            )

            return
        }


        // ==========================================
        // LOCATE REQUEST
        // ==========================================
        //
        // Keep your existing LOCATE logic here
        // if you already have it.
    }


    private fun showEmergencyNotification(
        context: Context,
        sender: String,
        message: String
    ) {

        createEmergencyChannel(context)


        // Open BEACON when notification tapped

        val openIntent =
            Intent(
                context,
                MainActivity::class.java
            )


        openIntent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP


        val pendingIntent =
            PendingIntent.getActivity(
                context,
                100,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )


        // ==========================================
        // CREATE NOTIFICATION
        // ==========================================

        val notification =
            NotificationCompat.Builder(
                context,
                CHANNEL_ID
            )

                .setSmallIcon(
                    R.mipmap.ic_launcher
                )

                .setContentTitle(
                    "🚨 BEACON EMERGENCY ALERT"
                )

                .setContentText(
                    "Emergency alert received from $sender"
                )

                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(message)
                )

                .setPriority(
                    NotificationCompat.PRIORITY_MAX
                )

                .setCategory(
                    NotificationCompat.CATEGORY_ALARM
                )

                .setVisibility(
                    NotificationCompat.VISIBILITY_PUBLIC
                )

                .setAutoCancel(true)

                .setContentIntent(
                    pendingIntent
                )

                .build()


        // ==========================================
        // SHOW NOTIFICATION
        // ==========================================

        try {

            NotificationManagerCompat
                .from(context)
                .notify(
                    NOTIFICATION_ID,
                    notification
                )

        } catch (e: SecurityException) {

            e.printStackTrace()
        }
    }


    private fun createEmergencyChannel(
        context: Context
    ) {

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {

            // siren.mp3 from res/raw

            val soundUri =
                Uri.parse(
                    "android.resource://" +
                            context.packageName +
                            "/" +
                            R.raw.siren
                )


            val audioAttributes =
                AudioAttributes.Builder()

                    .setUsage(
                        AudioAttributes.USAGE_ALARM
                    )

                    .setContentType(
                        AudioAttributes.CONTENT_TYPE_SONIFICATION
                    )

                    .build()


            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    "BEACON Emergency Alerts",
                    NotificationManager.IMPORTANCE_HIGH
                )


            channel.description =
                "Emergency alerts from trusted BEACON users"


            channel.enableVibration(true)


            channel.vibrationPattern =
                longArrayOf(
                    0,
                    500,
                    300,
                    500,
                    300,
                    500
                )


            channel.setSound(
                soundUri,
                audioAttributes
            )


            val notificationManager =
                context.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager


            notificationManager.createNotificationChannel(
                channel
            )
        }
    }
}