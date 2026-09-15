package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class SmsDeliveryReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val flow =
            context.getSharedPreferences(
                "BeaconFlow",
                Context.MODE_PRIVATE
            )

        if (resultCode == Activity.RESULT_OK) {

            // STEP 8
            // Android received SMS delivery confirmation

            flow.edit()
                .putBoolean(
                    "reply_delivered",
                    true
                )
                .apply()

        } else {

            flow.edit()
                .putBoolean(
                    "reply_delivered",
                    false
                )
                .apply()
        }
    }
}