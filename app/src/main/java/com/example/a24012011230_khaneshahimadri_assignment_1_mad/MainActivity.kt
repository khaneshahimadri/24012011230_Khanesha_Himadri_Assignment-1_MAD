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

    // =====================================================
    // UI
    // =====================================================

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

    private lateinit var navHome: TextView
    private lateinit var navGps: TextView
    private lateinit var navSos: TextView
    private lateinit var navCircle: TextView
    private lateinit var navLogs: TextView
    private lateinit var navScreens: TextView

    // =====================================================
    // GPS
    // =====================================================

    private lateinit var fusedLocationClient:
            FusedLocationProviderClient

    private lateinit var locationCallback:
            LocationCallback

    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null

    // =====================================================
    // SOS
    // =====================================================

    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    private val handler =
        Handler(Looper.getMainLooper())

    private var buttonPressed = false
    private var sosActive = false

    companion object {
        private const val PERMISSION_REQUEST = 100
    }

    // =====================================================
    // SOS 3 SECOND TIMER
    // =====================================================

    private val sosRunnable =
        Runnable {

            if (
                buttonPressed &&
                !sosActive
            ) {

                startSOS()
            }
        }

    // =====================================================
    // BATTERY RECEIVER
    // =====================================================

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

                if (
                    level >= 0 &&
                    scale > 0
                ) {

                    val batteryPercentage =
                        level * 100 / scale

                    tvBattery.text =
                        "$batteryPercentage%"
                }
            }
        }

    // =====================================================
    // ON CREATE
    // =====================================================

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_main
        )

        // =================================================
        // CONNECT XML
        // =================================================

        btnSOS =
            findViewById(
                R.id.btnSOS
            )

        tvStatus =
            findViewById(
                R.id.tvStatus
            )

        tvEmergency =
            findViewById(
                R.id.tvEmergency
            )

        tvHold =
            findViewById(
                R.id.tvHold
            )

        tvGps =
            findViewById(
                R.id.tvGps
            )

        tvGpsAccuracy =
            findViewById(
                R.id.tvGpsAccuracy
            )

        tvSms =
            findViewById(
                R.id.tvSms
            )

        tvSignalValue =
            findViewById(
                R.id.tvSignalValue
            )

        tvBattery =
            findViewById(
                R.id.tvBattery
            )

        cardGPS =
            findViewById(
                R.id.cardGPS
            )

        cardCircle =
            findViewById(
                R.id.cardCircle
            )

        cardTestBeacon =
            findViewById(
                R.id.cardTestBeacon
            )

        cardZeroInternet =
            findViewById(
                R.id.cardZeroInternet
            )

        tvContactAvatar =
            findViewById(
                R.id.tvContactAvatar
            )

        tvContactName =
            findViewById(
                R.id.tvContactName
            )

        tvContactNumber =
            findViewById(
                R.id.tvContactNumber
            )

        navHome =
            findViewById(
                R.id.navHome
            )

        navGps =
            findViewById(
                R.id.navGps
            )

        navSos =
            findViewById(
                R.id.navSos
            )

        navCircle =
            findViewById(
                R.id.navCircle
            )

        navLogs =
            findViewById(
                R.id.navLogs
            )

        navScreens =
            findViewById(
                R.id.navScreens
            )

        // =================================================
        // GPS SETUP
        // =================================================

        fusedLocationClient =
            LocationServices
                .getFusedLocationProviderClient(
                    this
                )

        createLocationCallback()

        // =================================================
        // BATTERY
        // =================================================

        registerReceiver(
            batteryReceiver,
            IntentFilter(
                Intent.ACTION_BATTERY_CHANGED
            )
        )

        // =================================================
        // STATUS
        // =================================================

        checkSmsStatus()
        checkCellularStatus()
        loadTrustedContact()

        // =================================================
        // SOS BUTTON
        // =================================================

        btnSOS.setOnTouchListener { _, event ->

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {

                    // If SOS already active,
                    // one new tap stops it immediately

                    if (sosActive) {

                        stopSOS()

                        return@setOnTouchListener true
                    }

                    // Start holding

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


                MotionEvent.ACTION_UP -> {

                    // If SOS has NOT started,
                    // cancel the hold

                    if (!sosActive) {

                        buttonPressed = false

                        handler.removeCallbacks(
                            sosRunnable
                        )

                        tvHold.text =
                            "3s REMAINING"
                    }

                    // If SOS is already active,
                    // releasing finger does nothing

                    true
                }


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

        // =================================================
        // GPS CARD
        // =================================================

        cardGPS.setOnClickListener {

            val intent =
                Intent(
                    this,
                    GpsActivity::class.java
                )

            startActivity(intent)
        }

        // =================================================
        // CIRCLE CARD
        // =================================================

        cardCircle.setOnClickListener {

            val intent =
                Intent(
                    this,
                    CircleActivity::class.java
                )

            startActivity(intent)
        }

        // =================================================
        // TEST BEACON
        // =================================================

        cardTestBeacon.setOnClickListener {

            Toast.makeText(
                this,
                "BEACON ready for LOCATE SMS",
                Toast.LENGTH_SHORT
            ).show()
        }

        // =================================================
        // ZERO INTERNET
        // =================================================

        cardZeroInternet.setOnClickListener {

            Toast.makeText(
                this,
                "GPS and SMS can work without mobile internet",
                Toast.LENGTH_SHORT
            ).show()
        }

        // =================================================
        // NAVIGATION
        // =================================================

        navHome.setOnClickListener {

            Toast.makeText(
                this,
                "Beacon Home",
                Toast.LENGTH_SHORT
            ).show()
        }

        navGps.setOnClickListener {

            val intent =
                Intent(
                    this,
                    GpsActivity::class.java
                )

            startActivity(intent)
        }

        navCircle.setOnClickListener {

            val intent =
                Intent(
                    this,
                    CircleActivity::class.java
                )

            startActivity(intent)
        }

        navLogs.setOnClickListener {

            val intent =
                Intent(
                    this,
                    LogsActivity::class.java
                )

            startActivity(intent)
        }

        navSos.setOnClickListener {

            Toast.makeText(
                this,
                "Hold SOS for 3 seconds",
                Toast.LENGTH_SHORT
            ).show()
        }

        navScreens.setOnClickListener {

            Toast.makeText(
                this,
                "BEACON Emergency System",
                Toast.LENGTH_SHORT
            ).show()
        }

        // =================================================
        // PERMISSIONS
        // =================================================

        requestRequiredPermissions()
    }

    // =====================================================
    // START SOS
    // =====================================================

    private fun startSOS() {

        if (sosActive) {
            return
        }

        sosActive = true

        // IMPORTANT
        // We no longer need the holding state after activation
        buttonPressed = false

        handler.removeCallbacks(
            sosRunnable
        )

        // =================================================
        // UPDATE UI
        // =================================================

        tvStatus.text =
            "● SOS ACTIVE"

        tvEmergency.text =
            "⚠ SOS DISPATCH ACTIVE"

        tvHold.text =
            "TAP SOS TO STOP"

        btnSOS.text =
            "SOS ACTIVE\nTAP TO STOP"

        // =================================================
        // CLEAR OLD PLAYER
        // =================================================

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

        // =================================================
        // START SIREN
        // =================================================

        mediaPlayer =
            MediaPlayer.create(
                this,
                R.raw.siren
            )

        if (mediaPlayer == null) {

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

        // =================================================
        // START VIBRATION
        // =================================================

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

        // =================================================
        // SEND SMS
        // =================================================

        sendEmergencySms()

        // =================================================
        // SAVE LOG
        // =================================================

        saveSOSLog()

        Toast.makeText(
            this,
            "SOS activated",
            Toast.LENGTH_SHORT
        ).show()
    }

    // =====================================================
    // STOP SOS
    // =====================================================

    private fun stopSOS() {

        handler.removeCallbacks(
            sosRunnable
        )

        buttonPressed = false
        sosActive = false

        // =================================================
        // STOP SIREN
        // =================================================

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

        // =================================================
        // STOP VIBRATION
        // =================================================

        try {

            vibrator?.cancel()

        } catch (_: Exception) {
        }

        // =================================================
        // RESET UI
        // =================================================

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

    // =====================================================
    // GPS CALLBACK
    // =====================================================

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

                    // Save location for LOCATE SMS receiver

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
                        .putLong(
                            "timestamp",
                            System.currentTimeMillis()
                        )
                        .apply()
                }
            }
    }

    // =====================================================
    // START GPS UPDATES
    // =====================================================

    private fun startLocationUpdates() {

        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
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

    // =====================================================
    // SMS STATUS
    // =====================================================

    private fun checkSmsStatus() {

        val hasTelephony =
            packageManager.hasSystemFeature(
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

    // =====================================================
    // CELLULAR STATUS
    // =====================================================

    private fun checkCellularStatus() {

        val hasTelephony =
            packageManager.hasSystemFeature(
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

    // =====================================================
    // LOAD TRUSTED CONTACT
    // =====================================================

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

            tvContactName.text =
                if (name.isNotEmpty()) {
                    name
                } else {
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

    // =====================================================
    // GET INITIALS
    // =====================================================

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

    // =====================================================
    // SEND EMERGENCY SMS
    // =====================================================

    private fun sendEmergencySms() {

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
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
                "Unable to send emergency SMS",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // =====================================================
    // SAVE SOS LOG
    // =====================================================

    private fun saveSOSLog() {

        val preferences =
            getSharedPreferences(
                "BeaconLogs",
                MODE_PRIVATE
            )

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

        val date =
            SimpleDateFormat(
                "dd MMM yyyy • hh:mm a",
                Locale.getDefault()
            ).format(
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

        val currentCount =
            preferences.getInt(
                "log_count",
                0
            )

        val editor =
            preferences.edit()

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
            currentCount + 1
        )

        editor.apply()
    }

    // =====================================================
    // PERMISSIONS
    // =====================================================

    private fun requestRequiredPermissions() {

        val permissions =
            mutableListOf<String>()

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            permissions.add(
                Manifest.permission.ACCESS_FINE_LOCATION
            )

            permissions.add(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            permissions.add(
                Manifest.permission.SEND_SMS
            )
        }

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED
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

    // =====================================================
    // RESUME
    // =====================================================

    override fun onResume() {

        super.onResume()

        loadTrustedContact()

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            startLocationUpdates()
        }
    }

    // =====================================================
    // PAUSE
    // =====================================================

    override fun onPause() {

        super.onPause()

        fusedLocationClient
            .removeLocationUpdates(
                locationCallback
            )

        // IMPORTANT:
        // DO NOT stop SOS here.
        // Siren should keep playing when finger is released.
    }

    // =====================================================
    // DESTROY
    // =====================================================

    override fun onDestroy() {

        handler.removeCallbacks(
            sosRunnable
        )

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

        try {

            vibrator?.cancel()

        } catch (_: Exception) {
        }

        try {

            unregisterReceiver(
                batteryReceiver
            )

        } catch (_: Exception) {
        }

        super.onDestroy()
    }
}