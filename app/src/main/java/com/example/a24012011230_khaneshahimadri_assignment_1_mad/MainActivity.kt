package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.Manifest
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
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView

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

import android.content.SharedPreferences
import android.telephony.SmsManager
import android.widget.Toast


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

    private lateinit var navHome: TextView
    private lateinit var navCircle: TextView
    private lateinit var navLogs: TextView

    private lateinit var cardGPS: CardView
    private lateinit var cardCircle: CardView


    // =========================================================
    // GPS
    // =========================================================

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var lastLatitude = 0.0
    private var lastLongitude = 0.0


    // =========================================================
    // SOS
    // =========================================================

    private var mediaPlayer: MediaPlayer? = null

    private var sosActive = false
    private var holding = false

    private var countdown = 3

    private val handler =
        Handler(Looper.getMainLooper())


    // =========================================================
    // COUNTDOWN
    // =========================================================

    private val countdownRunnable =
        object : Runnable {

            override fun run() {

                if (holding && !sosActive) {

                    if (countdown > 0) {

                        tvHold.text =
                            "HOLDING... $countdown"

                        countdown--

                        handler.postDelayed(
                            this,
                            1000
                        )

                    } else {

                        startSOS()
                    }
                }
            }
        }


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

                    val battery =
                        (level * 100) / scale

                    tvBattery.text =
                        "$battery%"
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


        // =====================================================
        // BOTTOM NAVIGATION
        // =====================================================

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


        // =====================================================
        // GPS REQUEST
        // =====================================================

        locationRequest =
            LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                3000L
            )
                .setMinUpdateIntervalMillis(2000L)
                .setMaxUpdateDelayMillis(5000L)
                .build()


        // =====================================================
        // GPS CALLBACK
        // =====================================================

        locationCallback =
            object : LocationCallback() {

                override fun onLocationResult(
                    locationResult: LocationResult
                ) {

                    val location =
                        locationResult.lastLocation

                    if (location != null) {

                        updateLiveLocation(
                            location
                        )
                    }
                }
            }


        // =====================================================
        // START FEATURES
        // =====================================================

        startBatteryMonitoring()

        checkGPS()

        checkSMS()


        // =====================================================
        // SOS BUTTON
        // =====================================================

        setupSOSButton()


        // =====================================================
        // GPS CARD
        // =====================================================

        cardGPS.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    GpsActivity::class.java
                )
            )
        }


        // =====================================================
        // SAFETY CIRCLE CARD
        // =====================================================

        cardCircle.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    CircleActivity::class.java
                )
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

            startActivity(
                Intent(
                    this,
                    CircleActivity::class.java
                )
            )
        }


        navLogs.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    LogsActivity::class.java
                )
            )
        }


        // =====================================================
        // HOME SELECTED
        // =====================================================

        navHome.alpha = 1.0f
        navCircle.alpha = 0.6f
        navLogs.alpha = 0.6f
    }


    // =========================================================
    // BATTERY MONITORING
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
    // GPS STATUS
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

            startLiveLocation()

        } else {

            tvGps.text =
                "OFF"

            stopLiveLocation()

            tvLastLocation.text =
                "Last location • GPS OFF"
        }
    }


    // =========================================================
    // START LIVE LOCATION
    // =========================================================

    private fun startLiveLocation() {

        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
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


        fusedLocationClient.requestLocationUpdates(
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
    // STOP LIVE LOCATION
    // =========================================================

    private fun stopLiveLocation() {

        try {

            fusedLocationClient.removeLocationUpdates(
                locationCallback
            )

        } catch (_: Exception) {
        }
    }


    // =========================================================
    // UPDATE LIVE LOCATION
    // =========================================================

    private fun updateLiveLocation(
        location: Location
    ) {

        lastLatitude =
            location.latitude

        lastLongitude =
            location.longitude


        tvGps.text =
            "READY"


        val time =
            SimpleDateFormat(
                "HH:mm:ss",
                Locale.getDefault()
            ).format(
                Date()
            )


        tvLastLocation.text =
            "Last location • $time"
    }


    // =========================================================
    // SMS STATUS
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
    // SOS BUTTON
    // =========================================================

    private fun setupSOSButton() {

        btnSOS.setOnTouchListener { _, event ->

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {

                    if (sosActive) {

                        stopSOS()

                    } else {

                        holding = true

                        countdown = 3

                        tvHold.text =
                            "HOLDING... 3"


                        handler.removeCallbacks(
                            countdownRunnable
                        )


                        handler.postDelayed(
                            countdownRunnable,
                            1000
                        )
                    }

                    true
                }


                MotionEvent.ACTION_UP -> {

                    if (!sosActive) {

                        holding = false

                        handler.removeCallbacks(
                            countdownRunnable
                        )

                        countdown = 3

                        tvHold.text =
                            "HOLD 3 SEC"
                    }

                    true
                }


                MotionEvent.ACTION_CANCEL -> {

                    holding = false

                    handler.removeCallbacks(
                        countdownRunnable
                    )

                    if (!sosActive) {

                        countdown = 3

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

        holding = false

        countdown = 3


        handler.removeCallbacks(
            countdownRunnable
        )


        // =====================================================
        // UPDATE UI
        // =====================================================

        tvStatus.text =
            "● SOS ACTIVE"

        tvEmergency.text =
            "EMERGENCY ACTIVE"

        tvHold.text =
            "SIREN ON • TAP TO STOP"

        btnSOS.text =
            "SOS\nACTIVE"


        // =====================================================
        // SIREN
        // =====================================================

        startSiren()


        // =====================================================
        // VIBRATION
        // =====================================================

        startVibration()


        // =====================================================
        // STEP 5: SEND EMERGENCY SMS
        // =====================================================

        sendEmergencySms()


        // =====================================================
        // CURRENT LOCATION
        // =====================================================

        if (
            lastLatitude != 0.0 &&
            lastLongitude != 0.0
        ) {

            tvLastLocation.text =
                "Last location • Just now"
        }
    }


    // =========================================================
    // STEP 4: SEND EMERGENCY SMS
    // =========================================================

    private fun sendEmergencySms() {

        // -----------------------------------------------------
        // Check SEND_SMS permission
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

            return
        }


        // -----------------------------------------------------
        // Get saved emergency contacts
        // -----------------------------------------------------

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
        // Create Google Maps location link
        // -----------------------------------------------------

        val locationLink: String

        if (
            lastLatitude != 0.0 &&
            lastLongitude != 0.0
        ) {

            locationLink =
                "https://maps.google.com/?q=$lastLatitude,$lastLongitude"

        } else {

            locationLink =
                "Location currently unavailable"
        }


        // -----------------------------------------------------
        // Emergency SMS message
        // -----------------------------------------------------

        val message =
            "BEACON EMERGENCY ALERT\n" +
                    "I may need help. Please check my location.\n" +
                    "Location: $locationLink"


        // -----------------------------------------------------
        // Send SMS
        // -----------------------------------------------------

        try {

            val smsManager =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

                    getSystemService(
                        SmsManager::class.java
                    )

                } else {

                    @Suppress("DEPRECATION")
                    SmsManager.getDefault()
                }


            var sentToAtLeastOne = false


            // -------------------------------------------------
            // Contact 1
            // -------------------------------------------------

            if (contact1.isNotEmpty()) {

                smsManager.sendTextMessage(
                    contact1,
                    null,
                    message,
                    null,
                    null
                )

                sentToAtLeastOne = true
            }


            // -------------------------------------------------
            // Contact 2
            // -------------------------------------------------

            if (contact2.isNotEmpty()) {

                smsManager.sendTextMessage(
                    contact2,
                    null,
                    message,
                    null,
                    null
                )

                sentToAtLeastOne = true
            }


            // -------------------------------------------------
            // Result
            // -------------------------------------------------

            if (sentToAtLeastOne) {

                Toast.makeText(
                    this,
                    "Emergency SMS sent",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                Toast.makeText(
                    this,
                    "No emergency contacts saved",
                    Toast.LENGTH_LONG
                ).show()
            }


        } catch (e: Exception) {

            Toast.makeText(
                this,
                "SMS could not be sent",
                Toast.LENGTH_LONG
            ).show()

            e.printStackTrace()
        }
    }


    // =========================================================
    // SIREN
    // =========================================================

    private fun startSiren() {

        try {

            mediaPlayer?.release()

            mediaPlayer =
                MediaPlayer.create(
                    this,
                    R.raw.siren
                )


            if (mediaPlayer != null) {

                mediaPlayer?.isLooping =
                    true

                mediaPlayer?.start()

            } else {

                tvHold.text =
                    "SIREN FILE NOT FOUND"
            }

        } catch (e: Exception) {

            tvHold.text =
                "SIREN ERROR"

            e.printStackTrace()
        }
    }


    // =========================================================
    // VIBRATION
    // =========================================================

    private fun startVibration() {

        val vibrator: Vibrator


        if (Build.VERSION.SDK_INT >= 31) {

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


        if (Build.VERSION.SDK_INT >= 26) {

            vibrator.vibrate(

                VibrationEffect.createWaveform(

                    longArrayOf(
                        0,
                        500,
                        300,
                        500
                    ),

                    0
                )
            )

        } else {

            @Suppress("DEPRECATION")

            vibrator.vibrate(

                longArrayOf(
                    0,
                    500,
                    300,
                    500
                ),

                0
            )
        }
    }


    // =========================================================
    // STOP SOS
    // =========================================================

    private fun stopSOS() {

        sosActive = false

        holding = false

        countdown = 3


        handler.removeCallbacks(
            countdownRunnable
        )


        // =====================================================
        // STOP SIREN
        // =====================================================

        mediaPlayer?.let {

            try {

                if (it.isPlaying) {

                    it.stop()
                }

            } catch (_: Exception) {
            }

            it.release()
        }

        mediaPlayer = null


        // =====================================================
        // STOP VIBRATION
        // =====================================================

        stopVibration()


        // =====================================================
        // RESET UI
        // =====================================================

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


        if (Build.VERSION.SDK_INT >= 31) {

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


        // =====================================================
        // LOCATION PERMISSION
        // =====================================================

        if (
            requestCode ==
            LOCATION_REQUEST
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


        // =====================================================
        // STEP 3: SMS PERMISSION RESULT
        // =====================================================

        if (
            requestCode ==
            SMS_PERMISSION_REQUEST
        ) {

            if (
                grantResults.isNotEmpty() &&
                grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) {

                // Permission granted.
                // Send SMS now.

                sendEmergencySms()

            } else {

                Toast.makeText(
                    this,
                    "SMS permission denied. Emergency SMS was not sent.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    // =========================================================
    // ON RESUME
    // =========================================================

    override fun onResume() {

        super.onResume()

        if (::tvGps.isInitialized) {

            checkGPS()
        }
    }


    // =========================================================
    // ON PAUSE
    // =========================================================

    override fun onPause() {

        super.onPause()

        stopLiveLocation()
    }


    // =========================================================
    // ON DESTROY
    // =========================================================

    override fun onDestroy() {

        handler.removeCallbacks(
            countdownRunnable
        )


        stopLiveLocation()


        try {

            unregisterReceiver(
                batteryReceiver
            )

        } catch (_: Exception) {
        }


        mediaPlayer?.let {

            try {

                if (it.isPlaying) {

                    it.stop()
                }

            } catch (_: Exception) {
            }

            it.release()
        }

        mediaPlayer = null


        stopVibration()


        super.onDestroy()
    }


    // =========================================================
    // CONSTANTS
    // =========================================================

    companion object {

        // Step 3: Permission request codes
        private const val LOCATION_REQUEST = 101
        private const val SMS_PERMISSION_REQUEST = 102
    }
}