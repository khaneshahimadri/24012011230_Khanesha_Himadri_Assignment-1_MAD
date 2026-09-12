package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.content.Intent
import android.os.BatteryManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class BeaconControlActivity : AppCompatActivity() {

    private lateinit var btnDashboard: TextView
    private lateinit var btnPower: TextView

    private lateinit var tvRunning: TextView
    private lateinit var tvDaemon: TextView

    private lateinit var btnEditKeyword: TextView
    private lateinit var tvKeyword: TextView

    private lateinit var tvRecipient: TextView
    private lateinit var tvCoordinates: TextView
    private lateinit var tvResponseStatus: TextView
    private lateinit var tvLastTime: TextView

    private lateinit var tvBatteryHealth: TextView

    private lateinit var btnTestBeacon: Button

    private lateinit var navHome: TextView
    private lateinit var navGps: TextView
    private lateinit var navSos: TextView
    private lateinit var navCircle: TextView
    private lateinit var navLogs: TextView
    private lateinit var navScreens: TextView

    private var beaconRunning = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_beacon_control)

        connectViews()

        loadSettings()

        loadTrustedContact()

        loadLocation()

        loadBattery()

        setupButtons()

        setupNavigation()
    }


    private fun connectViews() {

        btnDashboard =
            findViewById(R.id.btnDashboard)

        btnPower =
            findViewById(R.id.btnPower)

        tvRunning =
            findViewById(R.id.tvRunning)

        tvDaemon =
            findViewById(R.id.tvDaemon)

        btnEditKeyword =
            findViewById(R.id.btnEditKeyword)

        tvKeyword =
            findViewById(R.id.tvKeyword)

        tvRecipient =
            findViewById(R.id.tvRecipient)

        tvCoordinates =
            findViewById(R.id.tvCoordinates)

        tvResponseStatus =
            findViewById(R.id.tvResponseStatus)

        tvLastTime =
            findViewById(R.id.tvLastTime)

        tvBatteryHealth =
            findViewById(R.id.tvBatteryHealth)

        btnTestBeacon =
            findViewById(R.id.btnTestBeacon)


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


    private fun setupButtons() {

        // Back to dashboard

        btnDashboard.setOnClickListener {

            finish()
        }


        // Power button

        btnPower.setOnClickListener {

            beaconRunning = !beaconRunning

            saveBeaconStatus()

            updateBeaconStatus()
        }


        // Edit LOCATE keyword

        btnEditKeyword.setOnClickListener {

            showKeywordDialog()
        }


        // Test Beacon

        btnTestBeacon.setOnClickListener {

            testBeacon()
        }
    }


    private fun loadSettings() {

        val preferences =
            getSharedPreferences(
                "BeaconControlSettings",
                MODE_PRIVATE
            )

        beaconRunning =
            preferences.getBoolean(
                "beacon_enabled",
                true
            )

        val keyword =
            preferences.getString(
                "keyword",
                "LOCATE"
            ) ?: "LOCATE"

        tvKeyword.text =
            "\"$keyword\""

        updateBeaconStatus()
    }


    private fun saveBeaconStatus() {

        getSharedPreferences(
            "BeaconControlSettings",
            MODE_PRIVATE
        )
            .edit()
            .putBoolean(
                "beacon_enabled",
                beaconRunning
            )
            .apply()
    }


    private fun updateBeaconStatus() {

        if (beaconRunning) {

            tvRunning.text =
                "RUNNING"

            tvRunning.setTextColor(
                android.graphics.Color.parseColor(
                    "#00E5AE"
                )
            )

            tvDaemon.text =
                "Daemon Active & Listening"

            btnPower.text =
                "⏻"

            btnPower.setTextColor(
                android.graphics.Color.parseColor(
                    "#031510"
                )
            )

        } else {

            tvRunning.text =
                "STOPPED"

            tvRunning.setTextColor(
                android.graphics.Color.parseColor(
                    "#FF526B"
                )
            )

            tvDaemon.text =
                "Beacon Engine Stopped"

            btnPower.text =
                "⏻"

            btnPower.setTextColor(
                android.graphics.Color.parseColor(
                    "#FF526B"
                )
            )
        }
    }


    private fun showKeywordDialog() {

        val input =
            android.widget.EditText(this)

        input.hint =
            "Enter keyword"

        input.setText(
            tvKeyword.text
                .toString()
                .replace("\"", "")
        )

        input.setSelectAllOnFocus(true)


        AlertDialog.Builder(this)

            .setTitle(
                "Edit SMS Trigger Keyword"
            )

            .setMessage(
                "Enter the keyword used for location requests."
            )

            .setView(input)

            .setPositiveButton(
                "Save"
            ) { _, _ ->

                val keyword =
                    input.text
                        .toString()
                        .trim()
                        .uppercase()


                if (keyword.isNotEmpty()) {

                    tvKeyword.text =
                        "\"$keyword\""


                    getSharedPreferences(
                        "BeaconControlSettings",
                        MODE_PRIVATE
                    )
                        .edit()
                        .putString(
                            "keyword",
                            keyword
                        )
                        .apply()


                    Toast.makeText(
                        this,
                        "Keyword changed to $keyword",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            .setNegativeButton(
                "Cancel",
                null
            )

            .show()
    }


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

                tvRecipient.text =
                    name

            } else {

                tvRecipient.text =
                    "Trusted Contact"
            }

        } else {

            tvRecipient.text =
                "No Trusted Contact"
        }
    }


    private fun loadLocation() {

        val preferences =
            getSharedPreferences(
                "BeaconLocation",
                MODE_PRIVATE
            )

        val latitude =
            preferences.getString(
                "latitude",
                null
            )

        val longitude =
            preferences.getString(
                "longitude",
                null
            )

        val timestamp =
            preferences.getLong(
                "timestamp",
                0
            )


        if (
            latitude != null &&
            longitude != null
        ) {

            tvCoordinates.text =
                "$latitude, $longitude"

            tvResponseStatus.text =
                "● GPS Location Ready"

            if (timestamp > 0) {

                val format =
                    java.text.SimpleDateFormat(
                        "hh:mm a",
                        java.util.Locale.getDefault()
                    )

                tvLastTime.text =
                    format.format(
                        java.util.Date(timestamp)
                    )
            }

        } else {

            tvCoordinates.text =
                "Not Available"

            tvResponseStatus.text =
                "● Waiting for GPS"

            tvLastTime.text =
                "No response yet"
        }
    }


    private fun loadBattery() {

        val batteryManager =
            getSystemService(
                BATTERY_SERVICE
            ) as BatteryManager

        val battery =
            batteryManager.getIntProperty(
                BatteryManager.BATTERY_PROPERTY_CAPACITY
            )

        if (battery >= 0) {

            tvBatteryHealth.text =
                "BATTERY\n\n$battery%"

        } else {

            tvBatteryHealth.text =
                "BATTERY\n\n--%"
        }
    }


    private fun testBeacon() {

        if (!beaconRunning) {

            Toast.makeText(
                this,
                "Beacon Engine is stopped",
                Toast.LENGTH_SHORT
            ).show()

            return
        }


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


        if (
            latitude == null ||
            longitude == null
        ) {

            Toast.makeText(
                this,
                "GPS location is not available yet",
                Toast.LENGTH_SHORT
            ).show()

            return
        }


        tvCoordinates.text =
            "$latitude, $longitude"

        tvResponseStatus.text =
            "● Test Response Successful"

        tvLastTime.text =
            "Just now"


        Toast.makeText(
            this,
            "Beacon automatic response tested successfully",
            Toast.LENGTH_SHORT
        ).show()
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

        loadTrustedContact()

        loadLocation()

        loadBattery()
    }
}