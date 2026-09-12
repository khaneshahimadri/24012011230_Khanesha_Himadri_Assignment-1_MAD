package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OfflineArchitectureActivity : AppCompatActivity() {

    private lateinit var btnDashboard: TextView

    private lateinit var tvInternetStatus: TextView
    private lateinit var tvSmsStatus: TextView
    private lateinit var tvGpsStatus: TextView

    private lateinit var navHome: TextView
    private lateinit var navGps: TextView
    private lateinit var navSos: TextView
    private lateinit var navCircle: TextView
    private lateinit var navLogs: TextView
    private lateinit var navScreens: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_offline_architecture
        )

        connectViews()

        checkInternetStatus()

        checkSmsStatus()

        checkGpsStatus()

        setupNavigation()
    }


    private fun connectViews() {

        btnDashboard =
            findViewById(R.id.btnDashboard)

        tvInternetStatus =
            findViewById(R.id.tvInternetStatus)

        tvSmsStatus =
            findViewById(R.id.tvSmsStatus)

        tvGpsStatus =
            findViewById(R.id.tvGpsStatus)

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


        btnDashboard.setOnClickListener {

            finish()
        }
    }


    // =====================================================
    // INTERNET STATUS
    // =====================================================

    private fun checkInternetStatus() {

        val connectivityManager =
            getSystemService(
                CONNECTIVITY_SERVICE
            ) as ConnectivityManager


        val network =
            connectivityManager.activeNetwork


        if (network == null) {

            tvInternetStatus.text =
                "OFFLINE"

            return
        }


        val capabilities =
            connectivityManager
                .getNetworkCapabilities(network)


        val hasInternet =
            capabilities?.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            ) == true


        if (hasInternet) {

            tvInternetStatus.text =
                "ONLINE"

        } else {

            tvInternetStatus.text =
                "OFFLINE"
        }
    }


    // =====================================================
    // SMS STATUS
    // =====================================================

    private fun checkSmsStatus() {

        val hasTelephony =
            packageManager
                .hasSystemFeature(
                    PackageManager.FEATURE_TELEPHONY
                )


        if (hasTelephony) {

            tvSmsStatus.text =
                "AVAILABLE"

        } else {

            tvSmsStatus.text =
                "N/A"
        }
    }


    // =====================================================
    // GPS STATUS
    // =====================================================

    private fun checkGpsStatus() {

        val hasGps =
            packageManager
                .hasSystemFeature(
                    PackageManager.FEATURE_LOCATION_GPS
                )


        if (hasGps) {

            tvGpsStatus.text =
                "AVAILABLE"

        } else {

            tvGpsStatus.text =
                "N/A"
        }
    }


    // =====================================================
    // BOTTOM NAVIGATION
    // =====================================================

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

        checkInternetStatus()

        checkSmsStatus()

        checkGpsStatus()
    }
}