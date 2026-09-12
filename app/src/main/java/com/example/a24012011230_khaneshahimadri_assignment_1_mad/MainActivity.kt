package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.telephony.SmsManager
import android.view.MotionEvent
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

    private lateinit var btnSOS: TextView

    private lateinit var tvStatus: TextView
    private lateinit var tvEmergency: TextView
    private lateinit var tvHold: TextView

    private lateinit var tvGps: TextView
    private lateinit var tvGpsAccuracy: TextView

    private lateinit var tvSms: TextView
    private lateinit var tvSignalValue: TextView
    private lateinit var tvBattery: TextView

    private lateinit var cardGPS: CardView
    private lateinit var cardCircle: CardView
    private lateinit var cardTestBeacon: CardView
    private lateinit var cardZeroInternet: CardView

    private lateinit var tvContactAvatar: TextView
    private lateinit var tvContactName: TextView
    private lateinit var tvContactNumber: TextView


    // =========================================================
    // BOTTOM NAVIGATION
    // =========================================================

    private lateinit var navHome: TextView
    private lateinit var navGps: TextView
    private lateinit var navSos: TextView
    private lateinit var navCircle: TextView
    private lateinit var navLogs: TextView
    private lateinit var navScreens: TextView


    // =========================================================
    // GPS
    // =========================================================

    private lateinit var fusedLocationClient:
            FusedLocationProviderClient

    private lateinit var locationCallback:
            LocationCallback

    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null


    // =========================================================
    // SOS
    // =========================================================

    private var mediaPlayer: MediaPlayer? = null

    private var vibrator: Vibrator? = null

    private val handler =
        Handler(Looper.getMainLooper())

    private var buttonPressed = false

    private var sosActive = false


    companion object {

        private const val PERMISSION_REQUEST = 100
    }


    // =========================================================
    // SOS 3 SECOND TIMER
    // =========================================================

    private val sosRunnable = Runnable {

        if (buttonPressed && !sosActive) {

            startSOS()
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
                        BatteryManager.EXTRA_LEVEL,
                        -1
                    ) ?: -1

                val scale =
                    intent?.getIntExtra(
                        BatteryManager.EXTRA_SCALE,
                        -1
                    ) ?: -1


                if (level >= 0 && scale > 0) {

                    val batteryPercentage =
                        level * 100 / scale

                    tvBattery.text =
                        "$batteryPercentage%"
                }
            }
        }


    // =========================================================
    // ON CREATE
    // =========================================================

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        // =====================================================
        // CONNECT UI
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

        tvGpsAccuracy =
            findViewById(R.id.tvGpsAccuracy)

        tvSms =
            findViewById(R.id.tvSms)

        tvSignalValue =
            findViewById(R.id.tvSignalValue)

        tvBattery =
            findViewById(R.id.tvBattery)


        cardGPS =
            findViewById(R.id.cardGPS)

        cardCircle =
            findViewById(R.id.cardCircle)

        cardTestBeacon =
            findViewById(R.id.cardTestBeacon)

        cardZeroInternet =
            findViewById(R.id.cardZeroInternet)


        tvContactAvatar =
            findViewById(R.id.tvContactAvatar)

        tvContactName =
            findViewById(R.id.tvContactName)

        tvContactNumber =
            findViewById(R.id.tvContactNumber)


        // =====================================================
        // BOTTOM NAVIGATION
        // =====================================================

        navHome =
            findViewById(R.id.navHome)

        navGps =
            findViewById(R.id.navGps)

        navSos =
            findViewById(R.id.navSos)

        navCircle =
            findViewById(R.id.navCircle)

        navLogs =
            findViewById(R.id.navLogs)

        navScreens =
            findViewById(R.id.navScreens)


        // =====================================================
        // GPS SETUP
        // =====================================================

        fusedLocationClient =
            LocationServices
                .getFusedLocationProviderClient(this)

        createLocationCallback()


        // =====================================================
        // BATTERY
        // =====================================================

        registerReceiver(
            batteryReceiver,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )


        // =====================================================
        // STATUS
        // =====================================================

        checkSmsStatus()

        checkCellularStatus()

        loadTrustedContact()


        // =====================================================
        // SOS BUTTON
        // =====================================================

        btnSOS.setOnTouchListener { _, event ->

            when (event.action) {


                // -------------------------------------------------
                // FINGER PRESSED
                // -------------------------------------------------

                MotionEvent.ACTION_DOWN -> {


                    // If SOS is already active,
                    // tap again to STOP it.

                    if (sosActive) {

                        stopSOS()

                        return@setOnTouchListener true
                    }


                    // Start 3 second hold

                    buttonPressed = true

                    tvHold.text =
                        "HOLD FOR 3 SECONDS..."


                    handler.removeCallbacks(
                        sosRunnable
                    )


                    handler.postDelayed(
                        sosRunnable,
                        3000
                    )


                    true
                }


                // -------------------------------------------------
                // FINGER RELEASED
                // -------------------------------------------------

                MotionEvent.ACTION_UP -> {


                    // If SOS has NOT started,
                    // cancel the 3 second timer.

                    if (!sosActive) {

                        buttonPressed = false

                        handler.removeCallbacks(
                            sosRunnable
                        )

                        tvHold.text =
                            "3s REMAINING"
                    }


                    // IMPORTANT:
                    //
                    // If SOS already started,
                    // DO NOT call stopSOS() here.
                    //
                    // Siren keeps running until
                    // user taps SOS again.


                    true
                }


                // -------------------------------------------------
                // TOUCH CANCELLED
                // -------------------------------------------------

                MotionEvent.ACTION_CANCEL -> {


                    if (!sosActive) {

                        buttonPressed = false

                        handler.removeCallbacks(
                            sosRunnable
                        )

                        tvHold.text =
                            "3s REMAINING"
                    }


                    true
                }


                else -> true
            }
        }


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
        // CIRCLE CARD
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
        // TEST BEACON
        // =====================================================

        cardTestBeacon.setOnClickListener {

            Toast.makeText(
                this,
                "BEACON ready for LOCATE SMS",
                Toast.LENGTH_SHORT
            ).show()
        }


        // =====================================================
        // ZERO INTERNET
        // =====================================================

        cardZeroInternet.setOnClickListener {

            Toast.makeText(
                this,
                "GPS and SMS can work without mobile internet",
                Toast.LENGTH_SHORT
            ).show()
        }


        // =====================================================
        // BOTTOM NAVIGATION
        // =====================================================


        // BEACON

        navHome.setOnClickListener {

            Toast.makeText(
                this,
                "Beacon Home",
                Toast.LENGTH_SHORT
            ).show()
        }


        // GPS

        navGps.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    GpsActivity::class.java
                )
            )
        }


        // SOS

        navSos.setOnClickListener {

            Toast.makeText(
                this,
                "Hold SOS for 3 seconds",
                Toast.LENGTH_SHORT
            ).show()
        }


        // CIRCLE

        navCircle.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    CircleActivity::class.java
                )
            )
        }


        // LOGS

        navLogs.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    LogsActivity::class.java
                )
            )
        }


        // =====================================================
        // SCREENS - FIXED
        // =====================================================

        navScreens.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ScreensActivity::class.java
                )
            )
        }


        // =====================================================
        // PERMISSIONS
        // =====================================================

        requestRequiredPermissions()
    }


    // =========================================================
    // START SOS
    // =========================================================

    private fun startSOS() {


        // Prevent duplicate SOS

        if (sosActive) {

            return
        }


        sosActive = true

        buttonPressed = false


        handler.removeCallbacks(
            sosRunnable
        )


        // =====================================================
        // CHANGE UI
        // =====================================================

        tvStatus.text =
            "● SOS ACTIVE"

        tvEmergency.text =
            "⚠ EMERGENCY SOS DISPATCH"

        tvHold.text =
            "TAP SOS TO STOP"

        btnSOS.text =
            "SOS ACTIVE\nTAP TO STOP"


        // =====================================================
        // REMOVE OLD MEDIA PLAYER
        // =====================================================

        try {

            if (
                mediaPlayer != null &&
                mediaPlayer!!.isPlaying
            ) {

                mediaPlayer!!.stop()
            }

        } catch (_: Exception) {

        }


        try {

            mediaPlayer?.release()

        } catch (_: Exception) {

        }


        mediaPlayer = null


        // =====================================================
        // START SIREN
        // =====================================================

        mediaPlayer =
            MediaPlayer.create(
                this,
                R.raw.siren
            )


        if (mediaPlayer == null) {

            sosActive = false

            tvStatus.text =
                "● ONLINE"

            tvHold.text =
                "3s REMAINING"

            btnSOS.text =
                "SOS\nHOLD 3 SEC"


            Toast.makeText(
                this,
                "Siren file not found",
                Toast.LENGTH_SHORT
            ).show()


            return
        }


        mediaPlayer?.isLooping =
            true


        mediaPlayer?.start()


        // =====================================================
        // START VIBRATION
        // =====================================================

        vibrator =
            getSystemService(
                VIBRATOR_SERVICE
            ) as Vibrator


        val vibrationPattern =
            longArrayOf(
                0,
                500,
                300,
                500,
                300
            )


        @Suppress("DEPRECATION")
        vibrator?.vibrate(
            vibrationPattern,
            0
        )


        // =====================================================
        // SEND EMERGENCY SMS
        // =====================================================

        sendEmergencySms()


        // =====================================================
        // SAVE LOG
        // =====================================================

        saveSOSLog()


        Toast.makeText(
            this,
            "SOS activated",
            Toast.LENGTH_SHORT
        ).show()
    }


    // =========================================================
    // STOP SOS
    // =========================================================

    private fun stopSOS() {


        handler.removeCallbacks(
            sosRunnable
        )


        buttonPressed = false

        sosActive = false


        // =====================================================
        // STOP SIREN
        // =====================================================

        try {

            if (
                mediaPlayer != null &&
                mediaPlayer!!.isPlaying
            ) {

                mediaPlayer!!.stop()
            }

        } catch (_: Exception) {

        }


        try {

            mediaPlayer?.release()

        } catch (_: Exception) {

        }


        mediaPlayer = null


        // =====================================================
        // STOP VIBRATION
        // =====================================================

        try {

            vibrator?.cancel()

        } catch (_: Exception) {

        }


        vibrator = null


        // =====================================================
        // RESET UI
        // =====================================================

        tvStatus.text =
            "● ONLINE"

        tvEmergency.text =
            "⚠ EMERGENCY SOS DISPATCH"

        tvHold.text =
            "3s REMAINING"

        btnSOS.text =
            "SOS\nHOLD 3 SEC"


        Toast.makeText(
            this,
            "SOS stopped",
            Toast.LENGTH_SHORT
        ).show()
    }


    // =========================================================
    // CREATE LOCATION CALLBACK
    // =========================================================

    private fun createLocationCallback() {


        locationCallback =
            object : LocationCallback() {


                override fun onLocationResult(
                    result: LocationResult
                ) {


                    val location =
                        result.lastLocation
                            ?: return


                    currentLatitude =
                        location.latitude


                    currentLongitude =
                        location.longitude


                    // Update Home screen

                    tvGps.text =
                        "GPS Live"


                    tvGpsAccuracy.text =
                        "± ${
                            String.format(
                                Locale.US,
                                "%.1f",
                                location.accuracy
                            )
                        } m"


                    // Save latest location

                    getSharedPreferences(
                        "BeaconLocation",
                        MODE_PRIVATE
                    )
                        .edit()

                        .putString(
                            "latitude",
                            location.latitude.toString()
                        )

                        .putString(
                            "longitude",
                            location.longitude.toString()
                        )

                        .putFloat(
                            "accuracy",
                            location.accuracy
                        )

                        .putLong(
                            "altitude",
                            location.altitude.toBits()
                        )

                        .putLong(
                            "timestamp",
                            System.currentTimeMillis()
                        )

                        .apply()
                }
            }
    }


    // =========================================================
    // START LOCATION
    // =========================================================

    private fun startLocationUpdates() {


        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }


        val locationRequest =
            LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                3000L
            )

                .setMinUpdateIntervalMillis(
                    1500L
                )

                .build()


        fusedLocationClient
            .requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
    }


    // =========================================================
    // CHECK SMS
    // =========================================================

    private fun checkSmsStatus() {


        val hasTelephony =
            packageManager
                .hasSystemFeature(
                    PackageManager.FEATURE_TELEPHONY
                )


        if (hasTelephony) {

            tvSms.text =
                "SMS Ready"

        } else {

            tvSms.text =
                "SMS N/A"
        }
    }


    // =========================================================
    // CHECK CELLULAR
    // =========================================================

    private fun checkCellularStatus() {


        val hasTelephony =
            packageManager
                .hasSystemFeature(
                    PackageManager.FEATURE_TELEPHONY
                )


        if (hasTelephony) {

            tvSignalValue.text =
                "Signal"

        } else {

            tvSignalValue.text =
                "No Signal"
        }
    }


    // =========================================================
    // LOAD TRUSTED CONTACT
    // =========================================================

    private fun loadTrustedContact() {


        val preferences =
            getSharedPreferences(
                "BeaconContacts",
                MODE_PRIVATE
            )


        val name =
            preferences.getString(
                "contact1_name",
                ""
            ) ?: ""


        val number =
            preferences.getString(
                "contact1_number",
                ""
            ) ?: ""


        if (number.isNotEmpty()) {


            if (name.isNotEmpty()) {

                tvContactName.text =
                    name

            } else {

                tvContactName.text =
                    "Trusted Contact"
            }


            tvContactNumber.text =
                number


            tvContactAvatar.text =
                getInitials(name)


        } else {


            tvContactName.text =
                "No Trusted Contact"


            tvContactNumber.text =
                "Tap Circle to add contact"


            tvContactAvatar.text =
                "TC"
        }
    }


    // =========================================================
    // CONTACT INITIALS
    // =========================================================

    private fun getInitials(
        name: String
    ): String {


        if (name.isBlank()) {

            return "TC"
        }


        val words =
            name.trim()
                .split(" ")


        var initials =
            words[0]
                .take(1)
                .uppercase()


        if (words.size > 1) {

            initials +=
                words[1]
                    .take(1)
                    .uppercase()
        }


        return initials
    }


    // =========================================================
    // SEND EMERGENCY SMS
    // =========================================================

    private fun sendEmergencySms() {


        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {


            Toast.makeText(
                this,
                "SMS permission not granted",
                Toast.LENGTH_SHORT
            ).show()


            return
        }


        val preferences =
            getSharedPreferences(
                "BeaconContacts",
                MODE_PRIVATE
            )


        val number1 =
            preferences.getString(
                "contact1_number",
                ""
            ) ?: ""


        val number2 =
            preferences.getString(
                "contact2_number",
                ""
            ) ?: ""


        if (
            number1.isEmpty() &&
            number2.isEmpty()
        ) {


            Toast.makeText(
                this,
                "No trusted contact added",
                Toast.LENGTH_SHORT
            ).show()


            return
        }


        // =====================================================
        // CREATE MESSAGE
        // =====================================================

        var message =
            "BEACON EMERGENCY ALERT!\n" +
                    "I may need help."


        if (
            currentLatitude != null &&
            currentLongitude != null
        ) {


            message +=
                "\nLocation:\n" +
                        "https://maps.google.com/?q=" +
                        "$currentLatitude,$currentLongitude"


        } else {


            message +=
                "\nLocation unavailable."
        }


        // =====================================================
        // SEND
        // =====================================================

        try {


            @Suppress("DEPRECATION")
            val smsManager =
                SmsManager.getDefault()


            if (number1.isNotEmpty()) {


                smsManager.sendTextMessage(
                    number1,
                    null,
                    message,
                    null,
                    null
                )
            }


            if (number2.isNotEmpty()) {


                smsManager.sendTextMessage(
                    number2,
                    null,
                    message,
                    null,
                    null
                )
            }


            Toast.makeText(
                this,
                "Emergency SMS sent",
                Toast.LENGTH_SHORT
            ).show()


        } catch (e: Exception) {


            Toast.makeText(
                this,
                "Unable to send SMS",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    // =========================================================
    // SAVE SOS LOG
    // =========================================================

    private fun saveSOSLog() {


        val preferences =
            getSharedPreferences(
                "BeaconLogs",
                MODE_PRIVATE
            )


        // Keep previous log as log 2

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


        // Current date

        val date =
            SimpleDateFormat(
                "dd MMM yyyy • hh:mm a",
                Locale.getDefault()
            )
                .format(
                    Date()
                )


        val locationStatus =
            if (
                currentLatitude != null &&
                currentLongitude != null
            ) {

                "Available"

            } else {

                "Not Available"
            }


        val oldCount =
            preferences.getInt(
                "log_count",
                0
            )


        val editor =
            preferences.edit()


        // Move old log 1 to log 2

        if (oldDate.isNotEmpty()) {


            editor.putString(
                "log2_date",
                oldDate
            )


            editor.putString(
                "log2_location",
                oldLocation
            )


            editor.putString(
                "log2_sms",
                oldSms
            )
        }


        // Save new log

        editor.putString(
            "log1_date",
            date
        )


        editor.putString(
            "log1_location",
            locationStatus
        )


        editor.putString(
            "log1_sms",
            "Triggered"
        )


        editor.putInt(
            "log_count",
            oldCount + 1
        )


        editor.apply()
    }


    // =========================================================
    // REQUEST PERMISSIONS
    // =========================================================

    private fun requestRequiredPermissions() {


        val permissions =
            mutableListOf<String>()


        // LOCATION

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {


            permissions.add(
                Manifest.permission.ACCESS_FINE_LOCATION
            )


            permissions.add(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }


        // SEND SMS

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {


            permissions.add(
                Manifest.permission.SEND_SMS
            )
        }


        // RECEIVE SMS

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {


            permissions.add(
                Manifest.permission.RECEIVE_SMS
            )
        }


        if (permissions.isNotEmpty()) {


            ActivityCompat.requestPermissions(
                this,
                permissions.toTypedArray(),
                PERMISSION_REQUEST
            )
        }
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


        if (requestCode == PERMISSION_REQUEST) {


            if (
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {


                startLocationUpdates()
            }
        }
    }


    // =========================================================
    // ON RESUME
    // =========================================================

    override fun onResume() {

        super.onResume()


        // Contact may have changed
        // inside CircleActivity

        loadTrustedContact()


        // Start GPS

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {


            startLocationUpdates()
        }
    }


    // =========================================================
    // ON PAUSE
    // =========================================================

    override fun onPause() {

        super.onPause()


        // Stop GPS updates while another
        // Activity is open.

        fusedLocationClient
            .removeLocationUpdates(
                locationCallback
            )


        // VERY IMPORTANT:
        //
        // DO NOT CALL stopSOS() HERE.
        //
        // Otherwise opening GPS/Circle/Logs/Screens
        // would automatically stop the siren.
    }


    // =========================================================
    // ON DESTROY
    // =========================================================

    override fun onDestroy() {


        handler.removeCallbacks(
            sosRunnable
        )


        // Stop siren only when this Activity
        // is actually destroyed.

        try {

            if (
                mediaPlayer != null &&
                mediaPlayer!!.isPlaying
            ) {

                mediaPlayer!!.stop()
            }

        } catch (_: Exception) {

        }


        try {

            mediaPlayer?.release()

        } catch (_: Exception) {

        }


        mediaPlayer = null


        // Stop vibration

        try {

            vibrator?.cancel()

        } catch (_: Exception) {

        }


        vibrator = null


        // Unregister battery receiver

        try {

            unregisterReceiver(
                batteryReceiver
            )

        } catch (_: Exception) {

        }


        super.onDestroy()
    }
}