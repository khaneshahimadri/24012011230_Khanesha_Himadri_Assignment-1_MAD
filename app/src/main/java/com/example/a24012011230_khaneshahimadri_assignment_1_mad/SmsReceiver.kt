package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsManager
import androidx.core.content.ContextCompat


class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            return
        }


        val messages =
            Telephony.Sms.Intents
                .getMessagesFromIntent(intent)


        for (message in messages) {

            val sender =
                message.displayOriginatingAddress ?: ""

            val text =
                message.messageBody ?: ""


            // Check only LOCATE message
            if (text.trim().equals("LOCATE", ignoreCase = true)) {

                checkTrustedContact(
                    context,
                    sender
                )
            }
        }
    }


    // ==================================================
    // CHECK TRUSTED CONTACT
    // ==================================================

    private fun checkTrustedContact(
        context: Context,
        sender: String
    ) {

        val preferences =
            context.getSharedPreferences(
                "BeaconContacts",
                Context.MODE_PRIVATE
            )


        val contact1 =
            preferences.getString(
                "contact1_number",
                ""
            ) ?: ""


        val contact2 =
            preferences.getString(
                "contact2_number",
                ""
            ) ?: ""


        val cleanSender =
            cleanNumber(sender)

        val cleanContact1 =
            cleanNumber(contact1)

        val cleanContact2 =
            cleanNumber(contact2)


        // Only trusted contacts can request location

        if (
            cleanSender == cleanContact1 ||
            cleanSender == cleanContact2
        ) {

            sendLocationReply(
                context,
                sender
            )
        }
    }


    // ==================================================
    // SEND LOCATION REPLY
    // ==================================================

    private fun sendLocationReply(
        context: Context,
        sender: String
    ) {

        // Check SMS permission

        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }


        val locationPreferences =
            context.getSharedPreferences(
                "BeaconLocation",
                Context.MODE_PRIVATE
            )


        val latitude =
            locationPreferences.getString(
                "latitude",
                ""
            ) ?: ""


        val longitude =
            locationPreferences.getString(
                "longitude",
                ""
            ) ?: ""


        var replyMessage =
            "BEACON LOCATION\n"


        if (
            latitude.isNotEmpty() &&
            longitude.isNotEmpty()
        ) {

            replyMessage +=
                "My last known location:\n" +
                        "https://maps.google.com/?q=$latitude,$longitude"

        } else {

            replyMessage +=
                "Location is not available right now."
        }


        try {

            val smsManager =

                if (
                    Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.S
                ) {

                    context.getSystemService(
                        SmsManager::class.java
                    )

                } else {

                    @Suppress("DEPRECATION")

                    SmsManager.getDefault()
                }


            smsManager.sendTextMessage(
                sender,
                null,
                replyMessage,
                null,
                null
            )

        } catch (e: Exception) {

            e.printStackTrace()
        }
    }


    // ==================================================
    // CLEAN PHONE NUMBER
    // ==================================================

    private fun cleanNumber(
        number: String
    ): String {

        return number
            .replace(" ", "")
            .replace("-", "")
            .replace("(", "")
            .replace(")", "")
            .replace("+91", "")
    }
}