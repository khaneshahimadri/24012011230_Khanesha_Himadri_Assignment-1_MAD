package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BeaconHealthActivity : AppCompatActivity() {

    private lateinit var btnDashboard: TextView

    private lateinit var tvHealthCircle: TextView
    private lateinit var tvOverallTitle: TextView
    private lateinit var tvOverallInfo: TextView

    private lateinit var tvGpsInfo: TextView
    private lateinit var tvGpsStatus: TextView

    private lateinit var tvSmsInfo: TextView
    private lateinit var tvSmsStatus: TextView

    private lateinit var tvBatteryInfo: TextView
    private lateinit var tvBatteryStatus: TextView

    private lateinit var tvSignalInfo: TextView
    private lateinit var tvSignalStatus: TextView

    private lateinit var tvDataStatus: TextView

    private lateinit var btnRunDiagnostic: Button

    private lateinit var navHome: TextView
    private lateinit var navGps: TextView
    private lateinit var navSos: TextView
    private lateinit var navCircle: TextView
    private lateinit var navLogs: TextView
    private lateinit var navScreens: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_beacon_health
        )

        connectViews()

        runDiagnostic()

        btnDashboard.setOnClickListener {
            finish()
        }

        btnRunDiagnostic.setOnClickListener {

            runDiagnostic()

            Toast.makeText(
                this,
                "Hardware self-test completed",
                Toast.LENGTH_SHORT
            ).show()
        }

        setupNavigation()
    }


    private fun connectViews() {

        btnDashboard =
            findViewById(R.id.btnDashboard)

        tvHealthCircle =
            findViewById(R.id.tvHealthCircle)

        tvOverallTitle =
            findViewById(R.id.tvOverallTitle)

        tvOverallInfo =
            findViewById(R.id.tvOverallInfo)

        tvGpsInfo =
            findViewById(R.id.tvGpsInfo)

        tvGpsStatus =
            findViewById(R.id.tvGpsStatus)

        tvSmsInfo =
            findViewById(R.id.tvSmsInfo)

        tvSmsStatus =
            findViewById(R.id.tvSmsStatus)

        tvBatteryInfo =
            findViewById(R.id.tvBatteryInfo)

        tvBatteryStatus =
            findViewById(R.id.tvBatteryStatus)

        tvSignalInfo =
            findViewById(R.id.tvSignalInfo)

        tvSignalStatus =
            findViewById(R.id.tvSignalStatus)

        tvDataStatus =
            findViewById(R.id.tvDataStatus)

        btnRunDiagnostic =
            findViewById(R.id.btnRunDiagnostic)

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
    }


    private fun runDiagnostic() {

        var score = 0
        val total = 4


        // =========================================
        // GPS
        // =========================================

        val hasGps =
            packageManager.hasSystemFeature(
                PackageManager.FEATURE_LOCATION_GPS
            )

        val locationPreferences =
            getSharedPreferences(
                "BeaconLocation",
                MODE_PRIVATE
            )

        val latitude =
            locationPreferences.getString(
                "latitude",
                null
            )

        val longitude =
            locationPreferences.getString(
                "longitude",
                null
            )

        val accuracy =
            locationPreferences.getFloat(
                "accuracy",
                -1f
            )


        if (hasGps) {

            score++

            tvGpsStatus.text =
                "100%\nOK"

            if (
                latitude != null &&
                longitude != null
            ) {

                if (accuracy >= 0) {

                    tvGpsInfo.text =
                        "GPS Fix Ready • Accuracy ±" +
                                String.format(
                                    "%.1f",
                                    accuracy
                                ) +
                                " m"

                } else {

                    tvGpsInfo.text =
                        "GNSS hardware available • GPS fix saved"
                }

            } else {

                tvGpsInfo.text =
                    "GNSS hardware available • Waiting for GPS fix"
            }

        } else {

            tvGpsStatus.text =
                "N/A"

            tvGpsInfo.text =
                "GPS hardware not available"
        }


        // =========================================
        // SMS
        // =========================================

        val hasSms =
            packageManager.hasSystemFeature(
                PackageManager.FEATURE_TELEPHONY
            )

        if (hasSms) {

            score++

            tvSmsStatus.text =
                "READY\nOK"

            tvSmsInfo.text =
                "Cellular SMS hardware available"

        } else {

            tvSmsStatus.text =
                "N/A"

            tvSmsInfo.text =
                "Telephony hardware unavailable"
        }


        // =========================================
        // BATTERY
        // =========================================

        val batteryManager =
            getSystemService(
                BATTERY_SERVICE
            ) as BatteryManager

        val battery =
            batteryManager.getIntProperty(
                BatteryManager.BATTERY_PROPERTY_CAPACITY
            )

        tvBatteryInfo.text =
            "Battery Level: $battery%"

        if (battery >= 20) {

            score++

            tvBatteryStatus.text =
                "OPTIMAL"

        } else if (battery >= 10) {

            score++

            tvBatteryStatus.text =
                "LOW"

        } else {

            tvBatteryStatus.text =
                "CRITICAL"
        }


        // =========================================
        // CELLULAR RADIO
        // =========================================

        if (hasSms) {

            score++

            tvSignalStatus.text =
                "READY"

            tvSignalInfo.text =
                "Cellular radio hardware available"

        } else {

            tvSignalStatus.text =
                "N/A"

            tvSignalInfo.text =
                "No cellular radio detected"
        }


        // =========================================
        // INTERNET
        // =========================================

        val connectivityManager =
            getSystemService(
                CONNECTIVITY_SERVICE
            ) as ConnectivityManager

        val network =
            connectivityManager.activeNetwork

        var internetAvailable = false

        if (network != null) {

            val capabilities =
                connectivityManager
                    .getNetworkCapabilities(network)

            internetAvailable =
                capabilities?.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                ) == true
        }


        if (internetAvailable) {

            tvDataStatus.text =
                "ONLINE"

        } else {

            tvDataStatus.text =
                "OFFLINE"
        }


        // =========================================
        // HEALTH %
        // =========================================

        val health =
            (score * 100) / total

        tvHealthCircle.text =
            "$health%\nHEALTH"


        if (health >= 90) {

            tvOverallTitle.text =
                "All Hardware Subsystems Operational"

            tvOverallInfo.text =
                "GPS, SMS, cellular radio and power systems are ready for BEACON operation."

        } else if (health >= 70) {

            tvOverallTitle.text =
                "BEACON Operational"

            tvOverallInfo.text =
                "Most core emergency subsystems are ready. Check individual telemetry cards."

        } else {

            tvOverallTitle.text =
                "System Attention Required"

            tvOverallInfo.text =
                "Some core hardware or services are currently unavailable."
        }
    }


    private fun setupNavigation() {

        navHome.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )

            finish()
        }


        navGps.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    GpsActivity::class.java
                )
            )
        }


        navSos.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )

            Toast.makeText(
                this,
                "Hold SOS for 3 seconds",
                Toast.LENGTH_SHORT
            ).show()

            finish()
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


        navScreens.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ScreensActivity::class.java
                )
            )
        }
    }


    override fun onResume() {
        super.onResume()

        runDiagnostic()
    }
}