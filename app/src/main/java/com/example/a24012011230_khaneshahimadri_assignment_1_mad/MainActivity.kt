package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.telephony.SmsManager
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {


    // =========================================================
    // UI
    // =========================================================

    private lateinit var btnSOS: Button

    private lateinit var tvStatus: TextView
    private lateinit var tvEmergency: TextView
    private lateinit var tvHold: TextView

    private lateinit var tvGps: TextView
    private lateinit var tvSms: TextView
    private lateinit var tvBattery: TextView
    private lateinit var tvLastLocation: TextView

    private lateinit var cardGPS: CardView
    private lateinit var cardCircle: CardView

    private lateinit var navHome: TextView
    private lateinit var navCircle: TextView
    private lateinit var navLogs: TextView


    // =========================================================
    // GPS
    // =========================================================

    private lateinit var fusedLocationClient:
            FusedLocationProviderClient

    private lateinit var locationRequest:
            LocationRequest

    private lateinit var locationCallback:
            LocationCallback

    private var lastLatitude = 0.0
    private var lastLongitude = 0.0


    // =========================================================
    // SOS
    // =========================================================

    private var mediaPlayer: MediaPlayer? = null

    private var sosActive = false

    private var holdingSOS = false

    private val handler =
        Handler(Looper.getMainLooper())


    private val sosRunnable =
        Runnable {

            if (holdingSOS && !sosActive) {

                startSOS()
            }
        }


    // =========================================================
    // SMS STATUS VARIABLES
    // =========================================================

    private var totalSmsToSend = 0

    private var smsResultsReceived = 0

    private var smsSentSuccessfully = 0

    private var smsFailed = 0


    // =========================================================
    // BATTERY RECEIVER
    // =========================================================

    private val batteryReceiver =
        object : BroadcastReceiver() {

            override fun onReceive(
                context: Context?,
                intent: Intent?
            ) {

                val level =
                    intent?.getIntExtra(
                        "level",
                        -1
                    ) ?: -1


                val scale =
                    intent?.getIntExtra(
                        "scale",
                        100
                    ) ?: 100


                if (level >= 0 && scale > 0) {

                    val batteryPercentage =
                        (level * 100) / scale


                    tvBattery.text =
                        "$batteryPercentage%"
                }
            }
        }


    // =========================================================
    // SMS SENT RECEIVER
    // =========================================================

    private val smsSentReceiver =
        object : BroadcastReceiver() {

            override fun onReceive(
                context: Context?,
                intent: Intent?
            ) {

                smsResultsReceived++


                // Android confirms that SMS was sent

                if (resultCode == Activity.RESULT_OK) {

                    smsSentSuccessfully++

                } else {

                    smsFailed++
                }


                // Check when all SMS attempts are finished

                if (smsResultsReceived >= totalSmsToSend) {

                    if (
                        smsSentSuccessfully == totalSmsToSend
                    ) {

                        updateSmsLog(
                            "Sent Successfully"
                        )


                        Toast.makeText(
                            this@MainActivity,
                            "Emergency SMS sent successfully",
                            Toast.LENGTH_SHORT
                        ).show()


                    } else if (
                        smsSentSuccessfully > 0 &&
                        smsFailed > 0
                    ) {

                        updateSmsLog(
                            "Partially Sent"
                        )


                        Toast.makeText(
                            this@MainActivity,
                            "Some emergency SMS messages failed",
                            Toast.LENGTH_SHORT
                        ).show()


                    } else {

                        updateSmsLog(
                            "Failed"
                        )


                        Toast.makeText(
                            this@MainActivity,
                            "Emergency SMS failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }


    // =========================================================
    // ON CREATE
    // =========================================================

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_main
        )


        // =====================================================
        // FIND VIEWS
        // =====================================================

        btnSOS =
            findViewById(R.id.btnSOS)

        tvStatus =
            findViewById(R.id.tvStatus)

        tvEmergency =
            findViewById(R.id.tvEmergency)

        tvHold =
            findViewById(R.id.tvHold)

        tvGps =
            findViewById(R.id.tvGps)

        tvSms =
            findViewById(R.id.tvSms)

        tvBattery =
            findViewById(R.id.tvBattery)

        tvLastLocation =
            findViewById(R.id.tvLastLocation)

        cardGPS =
            findViewById(R.id.cardGPS)

        cardCircle =
            findViewById(R.id.cardCircle)

        navHome =
            findViewById(R.id.navHome)

        navCircle =
            findViewById(R.id.navCircle)

        navLogs =
            findViewById(R.id.navLogs)


        // =====================================================
        // GPS CLIENT
        // =====================================================

        fusedLocationClient =
            LocationServices
                .getFusedLocationProviderClient(this)


        locationRequest =
            LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                3000L
            )
                .setMinUpdateIntervalMillis(
                    2000L
                )
                .build()


        locationCallback =
            object : LocationCallback() {

                override fun onLocationResult(
                    locationResult: LocationResult
                ) {

                    val location =
                        locationResult.lastLocation


                    if (location != null) {

                        updateLocation(
                            location
                        )
                    }
                }
            }


        // =====================================================
        // REGISTER SMS STATUS RECEIVER
        // =====================================================

        val smsFilter =
            IntentFilter(
                SMS_SENT_ACTION
            )


        ContextCompat.registerReceiver(
            this,
            smsSentReceiver,
            smsFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )


        // =====================================================
        // START FEATURES
        // =====================================================

        startBatteryMonitoring()

        checkGPS()

        checkSMS()

        requestSmsPermissions()

        setupSOSButton()


        // =====================================================
        // GPS CARD
        // =====================================================

        cardGPS.setOnClickListener {

            val intent =
                Intent(
                    this,
                    GpsActivity::class.java
                )

            startActivity(
                intent
            )
        }


        // =====================================================
        // CIRCLE CARD
        // =====================================================

        cardCircle.setOnClickListener {

            val intent =
                Intent(
                    this,
                    CircleActivity::class.java
                )

            startActivity(
                intent
            )
        }


        // =====================================================
        // BOTTOM NAVIGATION
        // =====================================================

        navHome.setOnClickListener {

            navHome.alpha = 1.0f

            navCircle.alpha = 0.6f

            navLogs.alpha = 0.6f
        }


        navCircle.setOnClickListener {

            val intent =
                Intent(
                    this,
                    CircleActivity::class.java
                )

            startActivity(
                intent
            )
        }


        navLogs.setOnClickListener {

            val intent =
                Intent(
                    this,
                    LogsActivity::class.java
                )

            startActivity(
                intent
            )
        }


        navHome.alpha = 1.0f

        navCircle.alpha = 0.6f

        navLogs.alpha = 0.6f
    }


    // =========================================================
    // BATTERY
    // =========================================================

    private fun startBatteryMonitoring() {

        val filter =
            IntentFilter(
                Intent.ACTION_BATTERY_CHANGED
            )


        ContextCompat.registerReceiver(
            this,
            batteryReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }


    // =========================================================
    // CHECK GPS
    // =========================================================

    private fun checkGPS() {

        val locationManager =
            getSystemService(
                Context.LOCATION_SERVICE
            ) as LocationManager


        val gpsEnabled =
            try {

                locationManager.isProviderEnabled(
                    LocationManager.GPS_PROVIDER
                )

            } catch (e: Exception) {

                false
            }


        if (gpsEnabled) {

            tvGps.text =
                "READY"

            startLocationUpdates()

        } else {

            tvGps.text =
                "OFF"

            tvLastLocation.text =
                "Last location • GPS OFF"
        }
    }


    // =========================================================
    // START LIVE LOCATION
    // =========================================================

    private fun startLocationUpdates() {

        val finePermission =
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED


        val coarsePermission =
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED


        if (
            !finePermission &&
            !coarsePermission
        ) {

            ActivityCompat.requestPermissions(
                this,

                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),

                LOCATION_REQUEST
            )


            return
        }


        tvGps.text =
            "SEARCHING..."


        fusedLocationClient
            .requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            .addOnSuccessListener {

                tvGps.text =
                    "READY"
            }
            .addOnFailureListener {

                tvGps.text =
                    "ERROR"

                tvLastLocation.text =
                    "Last location • Unavailable"
            }
    }


    // =========================================================
    // UPDATE LOCATION
    // =========================================================

    private fun updateLocation(
        location: Location
    ) {

        lastLatitude =
            location.latitude

        lastLongitude =
            location.longitude


        tvGps.text =
            "READY"


        val currentTime =
            SimpleDateFormat(
                "HH:mm:ss",
                Locale.getDefault()
            ).format(
                Date()
            )


        tvLastLocation.text =
            "Last location • $currentTime"


        // Save latest GPS location

        getSharedPreferences(
            "BeaconLocation",
            MODE_PRIVATE
        )
            .edit()
            .putString(
                "latitude",
                lastLatitude.toString()
            )
            .putString(
                "longitude",
                lastLongitude.toString()
            )
            .putLong(
                "timestamp",
                System.currentTimeMillis()
            )
            .apply()
    }


    // =========================================================
    // STOP LOCATION
    // =========================================================

    private fun stopLocationUpdates() {

        if (
            ::fusedLocationClient.isInitialized
        ) {

            fusedLocationClient
                .removeLocationUpdates(
                    locationCallback
                )
        }
    }


    // =========================================================
    // CHECK SMS
    // =========================================================

    private fun checkSMS() {

        val hasTelephony =
            packageManager.hasSystemFeature(
                PackageManager.FEATURE_TELEPHONY
            )


        if (hasTelephony) {

            tvSms.text =
                "READY"

        } else {

            tvSms.text =
                "N/A"
        }
    }


    // =========================================================
    // REQUEST SMS PERMISSIONS
    // =========================================================

    private fun requestSmsPermissions() {

        val permissionsNeeded =
            mutableListOf<String>()


        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            permissionsNeeded.add(
                Manifest.permission.SEND_SMS
            )
        }


        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            permissionsNeeded.add(
                Manifest.permission.RECEIVE_SMS
            )
        }


        if (
            permissionsNeeded.isNotEmpty()
        ) {

            ActivityCompat.requestPermissions(
                this,
                permissionsNeeded.toTypedArray(),
                SMS_PERMISSION_REQUEST
            )
        }
    }


    // =========================================================
    // SOS BUTTON
    // =========================================================

    private fun setupSOSButton() {

        btnSOS.setOnTouchListener { _, event ->

            when (
                event.action
            ) {

                MotionEvent.ACTION_DOWN -> {

                    if (
                        sosActive
                    ) {

                        stopSOS()

                    } else {

                        holdingSOS = true


                        tvHold.text =
                            "KEEP HOLDING..."


                        handler.removeCallbacks(
                            sosRunnable
                        )


                        handler.postDelayed(
                            sosRunnable,
                            3000L
                        )
                    }


                    true
                }


                MotionEvent.ACTION_UP -> {

                    if (
                        !sosActive
                    ) {

                        holdingSOS = false


                        handler.removeCallbacks(
                            sosRunnable
                        )


                        tvHold.text =
                            "HOLD 3 SEC"
                    }


                    true
                }


                MotionEvent.ACTION_CANCEL -> {

                    holdingSOS = false


                    handler.removeCallbacks(
                        sosRunnable
                    )


                    if (
                        !sosActive
                    ) {

                        tvHold.text =
                            "HOLD 3 SEC"
                    }


                    true
                }


                else -> false
            }
        }
    }


    // =========================================================
    // START SOS
    // =========================================================

    private fun startSOS() {

        sosActive = true

        holdingSOS = false


        tvStatus.text =
            "● SOS ACTIVE"

        tvEmergency.text =
            "EMERGENCY ACTIVE"

        tvHold.text =
            "SIREN ON • TAP TO STOP"

        btnSOS.text =
            "SOS\nACTIVE"


        // Siren

        startSiren()


        // Vibration

        startVibration()


        // Send SMS and get starting status

        val smsStatus =
            sendEmergencySms()


        // Save SOS log

        saveSosLog(
            smsStatus
        )


        if (
            lastLatitude != 0.0 &&
            lastLongitude != 0.0
        ) {

            tvLastLocation.text =
                "Last location • Just now"
        }
    }


    // =========================================================
    // SEND EMERGENCY SMS
    // =========================================================

    private fun sendEmergencySms(): String {

        val preferences =
            getSharedPreferences(
                "BeaconContacts",
                MODE_PRIVATE
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


        // -----------------------------------------------------
        // NO CONTACT
        // -----------------------------------------------------

        if (
            contact1.isBlank() &&
            contact2.isBlank()
        ) {

            Toast.makeText(
                this,
                "No emergency contact selected",
                Toast.LENGTH_LONG
            ).show()


            return "No trusted contact"
        }


        // -----------------------------------------------------
        // SMS PERMISSION
        // -----------------------------------------------------

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,

                arrayOf(
                    Manifest.permission.SEND_SMS
                ),

                SMS_PERMISSION_REQUEST
            )


            return "Permission Required"
        }


        // -----------------------------------------------------
        // LOCATION LINK
        // -----------------------------------------------------

        val locationText =
            if (
                lastLatitude != 0.0 &&
                lastLongitude != 0.0
            ) {

                "https://maps.google.com/?q=$lastLatitude,$lastLongitude"

            } else {

                "Location currently unavailable"
            }


        // -----------------------------------------------------
        // MESSAGE
        // -----------------------------------------------------

        val message =
            "BEACON EMERGENCY ALERT\n" +
                    "I may need help. Please check my location.\n" +
                    "Location: $locationText"


        // Reset SMS counters

        totalSmsToSend = 0

        smsResultsReceived = 0

        smsSentSuccessfully = 0

        smsFailed = 0


        if (
            contact1.isNotBlank()
        ) {

            totalSmsToSend++
        }


        if (
            contact2.isNotBlank()
        ) {

            totalSmsToSend++
        }


        try {

            val smsManager =
                if (
                    Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.S
                ) {

                    getSystemService(
                        SmsManager::class.java
                    )

                } else {

                    @Suppress("DEPRECATION")

                    SmsManager.getDefault()
                }


            // -------------------------------------------------
            // SEND CONTACT 1
            // -------------------------------------------------

            if (
                contact1.isNotBlank()
            ) {

                sendSmsWithStatus(
                    smsManager,
                    contact1,
                    message,
                    1001
                )
            }


            // -------------------------------------------------
            // SEND CONTACT 2
            // -------------------------------------------------

            if (
                contact2.isNotBlank()
            ) {

                sendSmsWithStatus(
                    smsManager,
                    contact2,
                    message,
                    1002
                )
            }


            Toast.makeText(
                this,
                "Sending emergency SMS...",
                Toast.LENGTH_SHORT
            ).show()


            return "Sending..."


        } catch (
            e: Exception
        ) {

            Toast.makeText(
                this,
                "Unable to send emergency SMS",
                Toast.LENGTH_LONG
            ).show()


            return "Failed"
        }
    }


    // =========================================================
    // SEND ONE SMS WITH STATUS
    // =========================================================

    private fun sendSmsWithStatus(
        smsManager: SmsManager,
        phoneNumber: String,
        message: String,
        requestCode: Int
    ) {

        val sentIntent =
            Intent(
                SMS_SENT_ACTION
            )


        sentIntent.setPackage(
            packageName
        )


        val sentPendingIntent =
            PendingIntent.getBroadcast(
                this,
                requestCode,
                sentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )


        smsManager.sendTextMessage(
            phoneNumber,
            null,
            message,
            sentPendingIntent,
            null
        )
    }


    // =========================================================
    // SAVE SOS LOG
    // =========================================================

    private fun saveSosLog(
        smsStatus: String
    ) {

        val preferences =
            getSharedPreferences(
                "BeaconLogs",
                MODE_PRIVATE
            )


        // Save old latest log as second log

        val oldDate =
            preferences.getString(
                "log1_date",
                ""
            ) ?: ""


        val oldLocation =
            preferences.getString(
                "log1_location",
                ""
            ) ?: ""


        val oldSms =
            preferences.getString(
                "log1_sms",
                ""
            ) ?: ""


        // -----------------------------------------------------
        // CURRENT DATE AND TIME
        // -----------------------------------------------------

        val currentTime =
            SimpleDateFormat(
                "dd MMM yyyy • hh:mm a",
                Locale.getDefault()
            ).format(
                Date()
            )


        // -----------------------------------------------------
        // LOCATION STATUS
        // -----------------------------------------------------

        val locationStatus =
            if (
                lastLatitude != 0.0 &&
                lastLongitude != 0.0
            ) {

                "Available"

            } else {

                "Unavailable"
            }


        // -----------------------------------------------------
        // COUNT EVENTS
        // -----------------------------------------------------

        var count =
            preferences.getInt(
                "log_count",
                0
            )


        count++


        // -----------------------------------------------------
        // SAVE
        // -----------------------------------------------------

        preferences
            .edit()

            // Old Log 1 becomes Log 2

            .putString(
                "log2_date",
                oldDate
            )

            .putString(
                "log2_location",
                oldLocation
            )

            .putString(
                "log2_sms",
                oldSms
            )


            // New event becomes Log 1

            .putString(
                "log1_date",
                currentTime
            )

            .putString(
                "log1_location",
                locationStatus
            )

            .putString(
                "log1_sms",
                smsStatus
            )

            .putInt(
                "log_count",
                count
            )

            .apply()
    }


    // =========================================================
    // UPDATE SMS STATUS IN LATEST LOG
    // =========================================================

    private fun updateSmsLog(
        status: String
    ) {

        val preferences =
            getSharedPreferences(
                "BeaconLogs",
                MODE_PRIVATE
            )


        preferences
            .edit()
            .putString(
                "log1_sms",
                status
            )
            .apply()
    }


    // =========================================================
    // START SIREN
    // =========================================================

    private fun startSiren() {

        try {

            mediaPlayer?.release()


            mediaPlayer =
                MediaPlayer.create(
                    this,
                    R.raw.siren
                )


            if (
                mediaPlayer != null
            ) {

                mediaPlayer?.isLooping =
                    true

                mediaPlayer?.start()

            } else {

                tvHold.text =
                    "SIREN FILE NOT FOUND"
            }


        } catch (
            e: Exception
        ) {

            tvHold.text =
                "SIREN ERROR"
        }
    }


    // =========================================================
    // START VIBRATION
    // =========================================================

    private fun startVibration() {

        val vibrator: Vibrator


        if (
            Build.VERSION.SDK_INT >= 31
        ) {

            val manager =
                getSystemService(
                    Context.VIBRATOR_MANAGER_SERVICE
                ) as VibratorManager


            vibrator =
                manager.defaultVibrator

        } else {

            @Suppress("DEPRECATION")

            vibrator =
                getSystemService(
                    Context.VIBRATOR_SERVICE
                ) as Vibrator
        }


        val pattern =
            longArrayOf(
                0,
                500,
                300,
                500
            )


        if (
            Build.VERSION.SDK_INT >= 26
        ) {

            vibrator.vibrate(
                VibrationEffect.createWaveform(
                    pattern,
                    0
                )
            )

        } else {

            @Suppress("DEPRECATION")

            vibrator.vibrate(
                pattern,
                0
            )
        }
    }


    // =========================================================
    // STOP SOS
    // =========================================================

    private fun stopSOS() {

        sosActive = false

        holdingSOS = false


        handler.removeCallbacks(
            sosRunnable
        )


        mediaPlayer?.let {

            try {

                if (
                    it.isPlaying
                ) {

                    it.stop()
                }

            } catch (
                e: Exception
            ) {

            }


            it.release()
        }


        mediaPlayer = null


        stopVibration()


        tvStatus.text =
            "● ONLINE"

        tvEmergency.text =
            "EMERGENCY"

        tvHold.text =
            "HOLD 3 SEC"

        btnSOS.text =
            "SOS"
    }


    // =========================================================
    // STOP VIBRATION
    // =========================================================

    private fun stopVibration() {

        val vibrator: Vibrator


        if (
            Build.VERSION.SDK_INT >= 31
        ) {

            val manager =
                getSystemService(
                    Context.VIBRATOR_MANAGER_SERVICE
                ) as VibratorManager


            vibrator =
                manager.defaultVibrator

        } else {

            @Suppress("DEPRECATION")

            vibrator =
                getSystemService(
                    Context.VIBRATOR_SERVICE
                ) as Vibrator
        }


        vibrator.cancel()
    }


    // =========================================================
    // PERMISSION RESULT
    // =========================================================

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )


        // LOCATION

        if (
            requestCode == LOCATION_REQUEST
        ) {

            val fineGranted =
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED


            val coarseGranted =
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED


            if (
                fineGranted ||
                coarseGranted
            ) {

                checkGPS()

            } else {

                tvGps.text =
                    "DENIED"

                tvLastLocation.text =
                    "Last location • Permission denied"
            }
        }


        // SMS

        if (
            requestCode == SMS_PERMISSION_REQUEST
        ) {

            checkSMS()
        }
    }


    // =========================================================
    // ON RESUME
    // =========================================================

    override fun onResume() {

        super.onResume()


        if (
            ::tvGps.isInitialized
        ) {

            checkGPS()
        }
    }


    // =========================================================
    // ON PAUSE
    // =========================================================

    override fun onPause() {

        super.onPause()

        stopLocationUpdates()
    }


    // =========================================================
    // ON DESTROY
    // =========================================================

    override fun onDestroy() {

        handler.removeCallbacks(
            sosRunnable
        )


        stopLocationUpdates()


        mediaPlayer?.release()

        mediaPlayer = null


        stopVibration()


        // Remove battery receiver

        try {

            unregisterReceiver(
                batteryReceiver
            )

        } catch (
            e: Exception
        ) {

        }


        // Remove SMS status receiver

        try {

            unregisterReceiver(
                smsSentReceiver
            )

        } catch (
            e: Exception
        ) {

        }


        super.onDestroy()
    }


    // =========================================================
    // CONSTANTS
    // =========================================================

    companion object {

        private const val LOCATION_REQUEST =
            101

        private const val SMS_PERMISSION_REQUEST =
            102

        private const val SMS_SENT_ACTION =
            "BEACON_SMS_SENT"
    }
}