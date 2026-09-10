package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.provider.Telephony
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import android.os.Handler
import android.os.Looper
import java.util.concurrent.atomic.AtomicBoolean

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        // Make sure this is an incoming SMS broadcast
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            return
        }

        // RECEIVE_SMS permission check
        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        // SEND_SMS permission check
        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        // Read incoming SMS
        val messages = try {
            Telephony.Sms.Intents.getMessagesFromIntent(intent)
        } catch (e: Exception) {
            return
        }

        if (messages.isEmpty()) {
            return
        }

        // Get sender number
        val sender = messages[0].originatingAddress ?: return

        // Combine SMS parts
        val messageBody = messages.joinToString("") {
            it.messageBody ?: ""
        }.trim()

        // We only respond to LOCATE
        if (!messageBody.equals("LOCATE", ignoreCase = true)) {
            return
        }

        // Security: only trusted Safety Circle contacts can request location
        if (!isTrustedContact(context, sender)) {
            return
        }

        // Keep receiver alive while getting location
        val pendingResult = goAsync()

        getLocationAndReply(
            context,
            sender,
            pendingResult
        )
    }

    private fun isTrustedContact(
        context: Context,
        incomingNumber: String
    ): Boolean {

        val preferences = context.getSharedPreferences(
            "BeaconContacts",
            Context.MODE_PRIVATE
        )

        val contact1 = preferences.getString(
            "contact1_number",
            ""
        ) ?: ""

        val contact2 = preferences.getString(
            "contact2_number",
            ""
        ) ?: ""

        return samePhoneNumber(incomingNumber, contact1) ||
                samePhoneNumber(incomingNumber, contact2)
    }

    private fun samePhoneNumber(
        number1: String,
        number2: String
    ): Boolean {

        val digits1 = number1.filter { it.isDigit() }
        val digits2 = number2.filter { it.isDigit() }

        if (digits1.isEmpty() || digits2.isEmpty()) {
            return false
        }

        // Exact match
        if (digits1 == digits2) {
            return true
        }

        // Handles +91XXXXXXXXXX vs XXXXXXXXXX
        if (digits1.length >= 10 && digits2.length >= 10) {
            return digits1.takeLast(10) == digits2.takeLast(10)
        }

        return false
    }

    private fun getLocationAndReply(
        context: Context,
        sender: String,
        pendingResult: PendingResult
    ) {

        val finished = AtomicBoolean(false)

        fun finish(replyMessage: String) {

            if (!finished.compareAndSet(false, true)) {
                return
            }

            sendSms(
                context,
                sender,
                replyMessage
            )

            pendingResult.finish()
        }

        // Check location permission
        val fineLocation =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarseLocation =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (!fineLocation && !coarseLocation) {

            finish(
                "BEACON LOCATION\nLocation permission is not available."
            )

            return
        }

        try {

            val locationClient =
                LocationServices.getFusedLocationProviderClient(context)

            val cancellationTokenSource =
                CancellationTokenSource()

            // Give GPS a few seconds to provide a fresh location
            val timeoutHandler =
                Handler(Looper.getMainLooper())

            timeoutHandler.postDelayed({

                cancellationTokenSource.cancel()

                // If fresh GPS wasn't obtained,
                // use the last saved location.
                val lastLocation =
                    getSavedLocation(context)

                if (lastLocation != null) {

                    finish(
                        createLocationMessage(
                            lastLocation.first,
                            lastLocation.second,
                            true
                        )
                    )

                } else {

                    finish(
                        "BEACON LOCATION\nLocation is currently unavailable."
                    )
                }

            }, 7000)

            locationClient
                .getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                )
                .addOnSuccessListener { location ->

                    timeoutHandler.removeCallbacksAndMessages(null)

                    if (location != null) {

                        // Save latest location
                        saveLocation(
                            context,
                            location.latitude,
                            location.longitude
                        )

                        finish(
                            createLocationMessage(
                                location.latitude,
                                location.longitude,
                                false
                            )
                        )

                    } else {

                        val lastLocation =
                            getSavedLocation(context)

                        if (lastLocation != null) {

                            finish(
                                createLocationMessage(
                                    lastLocation.first,
                                    lastLocation.second,
                                    true
                                )
                            )

                        } else {

                            finish(
                                "BEACON LOCATION\nLocation is currently unavailable."
                            )
                        }
                    }
                }
                .addOnFailureListener {

                    timeoutHandler.removeCallbacksAndMessages(null)

                    val lastLocation =
                        getSavedLocation(context)

                    if (lastLocation != null) {

                        finish(
                            createLocationMessage(
                                lastLocation.first,
                                lastLocation.second,
                                true
                            )
                        )

                    } else {

                        finish(
                            "BEACON LOCATION\nUnable to get location."
                        )
                    }
                }

        } catch (e: Exception) {

            val lastLocation =
                getSavedLocation(context)

            if (lastLocation != null) {

                finish(
                    createLocationMessage(
                        lastLocation.first,
                        lastLocation.second,
                        true
                    )
                )

            } else {

                finish(
                    "BEACON LOCATION\nUnable to get location."
                )
            }
        }
    }

    private fun createLocationMessage(
        latitude: Double,
        longitude: Double,
        isLastKnown: Boolean
    ): String {

        val mapsLink =
            "https://maps.google.com/?q=$latitude,$longitude"

        return if (isLastKnown) {

            "BEACON LOCATION\n" +
                    "Last known location:\n" +
                    mapsLink

        } else {

            "BEACON LOCATION\n" +
                    "Current location:\n" +
                    mapsLink
        }
    }

    private fun saveLocation(
        context: Context,
        latitude: Double,
        longitude: Double
    ) {

        context.getSharedPreferences(
            "BeaconLocation",
            Context.MODE_PRIVATE
        )
            .edit()
            .putString(
                "latitude",
                latitude.toString()
            )
            .putString(
                "longitude",
                longitude.toString()
            )
            .putLong(
                "timestamp",
                System.currentTimeMillis()
            )
            .apply()
    }

    private fun getSavedLocation(
        context: Context
    ): Pair<Double, Double>? {

        val preferences =
            context.getSharedPreferences(
                "BeaconLocation",
                Context.MODE_PRIVATE
            )

        val latitude =
            preferences.getString(
                "latitude",
                null
            )?.toDoubleOrNull()

        val longitude =
            preferences.getString(
                "longitude",
                null
            )?.toDoubleOrNull()

        if (latitude == null || longitude == null) {
            return null
        }

        return Pair(
            latitude,
            longitude
        )
    }

    private fun sendSms(
        context: Context,
        phoneNumber: String,
        message: String
    ) {

        try {

            val smsManager =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {

                    context.getSystemService(
                        SmsManager::class.java
                    )

                } else {

                    @Suppress("DEPRECATION")
                    SmsManager.getDefault()
                }

            smsManager.sendTextMessage(
                phoneNumber,
                null,
                message,
                null,
                null
            )

        } catch (e: Exception) {

            Toast.makeText(
                context,
                "Unable to send location SMS",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}