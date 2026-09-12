package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.BatteryManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class LocationResponseActivity : AppCompatActivity() {

    private lateinit var btnDashboard: TextView

    private lateinit var tvContactAvatar: TextView
    private lateinit var tvContactName: TextView
    private lateinit var tvContactNumber: TextView

    private lateinit var tvSmsText: TextView

    private lateinit var btnCopySms: TextView
    private lateinit var btnCopyUrl: TextView
    private lateinit var btnOpenMap: Button

    private lateinit var tvMapUrl: TextView

    private lateinit var tvTelemetryCoordinates: TextView
    private lateinit var tvTelemetryAccuracy: TextView

    private lateinit var navHome: TextView
    private lateinit var navGps: TextView
    private lateinit var navSos: TextView
    private lateinit var navCircle: TextView
    private lateinit var navLogs: TextView
    private lateinit var navScreens: TextView

    private var latitude: String? = null
    private var longitude: String? = null

    private var accuracy: Float? = null
    private var altitude: Double? = null

    private var mapUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_location_response
        )

        connectViews()

        loadContact()

        loadLocation()

        setupButtons()

        setupNavigation()
    }


    private fun connectViews() {

        btnDashboard =
            findViewById(R.id.btnDashboard)

        tvContactAvatar =
            findViewById(R.id.tvContactAvatar)

        tvContactName =
            findViewById(R.id.tvContactName)

        tvContactNumber =
            findViewById(R.id.tvContactNumber)

        tvSmsText =
            findViewById(R.id.tvSmsText)

        btnCopySms =
            findViewById(R.id.btnCopySms)

        btnCopyUrl =
            findViewById(R.id.btnCopyUrl)

        btnOpenMap =
            findViewById(R.id.btnOpenMap)

        tvMapUrl =
            findViewById(R.id.tvMapUrl)

        tvTelemetryCoordinates =
            findViewById(R.id.tvTelemetryCoordinates)

        tvTelemetryAccuracy =
            findViewById(R.id.tvTelemetryAccuracy)

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


    private fun loadContact() {

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


        if (name.isNotEmpty()) {

            tvContactName.text =
                name

            tvContactAvatar.text =
                getInitials(name)

        } else {

            tvContactName.text =
                "Trusted Contact"

            tvContactAvatar.text =
                "TC"
        }


        tvContactNumber.text =
            if (number.isNotEmpty()) {

                number

            } else {

                "No number available"
            }
    }


    private fun loadLocation() {

        val locationPreferences =
            getSharedPreferences(
                "BeaconLocation",
                MODE_PRIVATE
            )


        latitude =
            locationPreferences.getString(
                "latitude",
                null
            )

        longitude =
            locationPreferences.getString(
                "longitude",
                null
            )


        accuracy =
            locationPreferences.getFloat(
                "accuracy",
                -1f
            )


        altitude =
            if (
                locationPreferences.contains(
                    "altitude"
                )
            ) {

                Double.fromBits(
                    locationPreferences.getLong(
                        "altitude",
                        0L
                    )
                )

            } else {

                null
            }


        if (
            latitude != null &&
            longitude != null
        ) {

            mapUrl =
                "https://maps.google.com/?q=$latitude,$longitude"


            tvMapUrl.text =
                mapUrl


            val battery =
                getBatteryLevel()


            val accuracyText =
                if (
                    accuracy != null &&
                    accuracy!! >= 0
                ) {

                    "± ${
                        String.format(
                            Locale.US,
                            "%.1f",
                            accuracy
                        )
                    } m"

                } else {

                    "Available"
                }


            val altitudeText =
                if (altitude != null) {

                    "${
                        String.format(
                            Locale.US,
                            "%.1f",
                            altitude
                        )
                    } m"

                } else {

                    "N/A"
                }


            val message =

                "BEACON LOCATION:\n" +
                        "Lat: $latitude\n" +
                        "Lng: $longitude\n" +
                        "Accuracy: $accuracyText\n" +
                        "Alt: $altitudeText | Bat: $battery%\n" +
                        "Map: $mapUrl"


            tvSmsText.text =
                message


            tvTelemetryCoordinates.text =
                "Latitude / Longitude\n" +
                        "$latitude\n" +
                        "$longitude"


            tvTelemetryAccuracy.text =
                "Accuracy Radius\n$accuracyText"

        } else {

            tvMapUrl.text =
                "Location unavailable"


            tvSmsText.text =
                "BEACON LOCATION:\n" +
                        "GPS coordinates are not available yet."


            tvTelemetryCoordinates.text =
                "Latitude / Longitude\n--\n--"


            tvTelemetryAccuracy.text =
                "Accuracy Radius\n--"
        }
    }


    private fun setupButtons() {

        btnDashboard.setOnClickListener {

            finish()
        }


        btnCopySms.setOnClickListener {

            copyText(
                "BEACON SMS",
                tvSmsText.text.toString()
            )
        }


        btnCopyUrl.setOnClickListener {

            if (mapUrl.isEmpty()) {

                Toast.makeText(
                    this,
                    "Location not available",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            copyText(
                "BEACON Map URL",
                mapUrl
            )
        }


        btnOpenMap.setOnClickListener {

            openMap()
        }
    }


    private fun copyText(
        label: String,
        text: String
    ) {

        val clipboard =
            getSystemService(
                Context.CLIPBOARD_SERVICE
            ) as ClipboardManager


        val clip =
            ClipData.newPlainText(
                label,
                text
            )


        clipboard.setPrimaryClip(
            clip
        )


        Toast.makeText(
            this,
            "Copied",
            Toast.LENGTH_SHORT
        ).show()
    }


    private fun openMap() {

        if (
            latitude == null ||
            longitude == null
        ) {

            Toast.makeText(
                this,
                "GPS location not available",
                Toast.LENGTH_SHORT
            ).show()

            return
        }


        val geoUri =
            Uri.parse(
                "geo:$latitude,$longitude?q=$latitude,$longitude"
            )


        val mapIntent =
            Intent(
                Intent.ACTION_VIEW,
                geoUri
            )


        if (
            mapIntent.resolveActivity(
                packageManager
            ) != null
        ) {

            startActivity(
                mapIntent
            )

        } else {

            val webIntent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(mapUrl)
                )

            startActivity(
                webIntent
            )
        }
    }


    private fun getBatteryLevel(): Int {

        val batteryManager =
            getSystemService(
                BATTERY_SERVICE
            ) as BatteryManager


        return batteryManager.getIntProperty(
            BatteryManager.BATTERY_PROPERTY_CAPACITY
        )
    }


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

        loadContact()

        loadLocation()
    }
}