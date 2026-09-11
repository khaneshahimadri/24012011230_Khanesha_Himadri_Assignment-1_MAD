package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.cardview.widget.CardView

class LogsActivity : AppCompatActivity() {

    private lateinit var btnClearLogs: AppCompatImageButton

    private lateinit var filterAll: TextView
    private lateinit var filterSuccess: TextView
    private lateinit var filterBlocked: TextView
    private lateinit var filterEmergency: TextView

    private lateinit var cardEmergency: CardView
    private lateinit var cardSuccess: CardView
    private lateinit var cardSuccess2: CardView
    private lateinit var cardBlocked: CardView
    private lateinit var cardDiagnostic: CardView
    private lateinit var cardLastSuccess: CardView

    private lateinit var tvLog1Date: TextView
    private lateinit var tvLog1Details: TextView

    private lateinit var tvLog2Title: TextView
    private lateinit var tvLog2Date: TextView
    private lateinit var tvLog2Details: TextView
    private lateinit var tvContactNumber: TextView

    private lateinit var tvSuccess2Title: TextView
    private lateinit var tvSuccess2Number: TextView

    private lateinit var tvLastSuccessTitle: TextView
    private lateinit var tvLastSuccessNumber: TextView

    private lateinit var btnViewEmergencyLocation: Button
    private lateinit var btnViewSuccessLocation: Button
    private lateinit var btnViewSuccessLocation2: Button
    private lateinit var btnViewLastLocation: Button

    private lateinit var navHome: TextView
    private lateinit var navGps: TextView
    private lateinit var navSos: TextView
    private lateinit var navCircle: TextView
    private lateinit var navLogs: TextView
    private lateinit var navScreens: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_logs)

        connectViews()

        loadLogs()

        setupFilters()

        setupButtons()

        setupNavigation()
    }

    private fun connectViews() {

        btnClearLogs =
            findViewById(R.id.btnClearLogs)

        filterAll =
            findViewById(R.id.filterAll)

        filterSuccess =
            findViewById(R.id.filterSuccess)

        filterBlocked =
            findViewById(R.id.filterBlocked)

        filterEmergency =
            findViewById(R.id.filterEmergency)

        cardEmergency =
            findViewById(R.id.cardEmergency)

        cardSuccess =
            findViewById(R.id.cardSuccess)

        cardSuccess2 =
            findViewById(R.id.cardSuccess2)

        cardBlocked =
            findViewById(R.id.cardBlocked)

        cardDiagnostic =
            findViewById(R.id.cardDiagnostic)

        cardLastSuccess =
            findViewById(R.id.cardLastSuccess)

        tvLog1Date =
            findViewById(R.id.tvLog1Date)

        tvLog1Details =
            findViewById(R.id.tvLog1Details)

        tvLog2Title =
            findViewById(R.id.tvLog2Title)

        tvLog2Date =
            findViewById(R.id.tvLog2Date)

        tvLog2Details =
            findViewById(R.id.tvLog2Details)

        tvContactNumber =
            findViewById(R.id.tvContactNumber)

        tvSuccess2Title =
            findViewById(R.id.tvSuccess2Title)

        tvSuccess2Number =
            findViewById(R.id.tvSuccess2Number)

        tvLastSuccessTitle =
            findViewById(R.id.tvLastSuccessTitle)

        tvLastSuccessNumber =
            findViewById(R.id.tvLastSuccessNumber)

        btnViewEmergencyLocation =
            findViewById(R.id.btnViewEmergencyLocation)

        btnViewSuccessLocation =
            findViewById(R.id.btnViewSuccessLocation)

        btnViewSuccessLocation2 =
            findViewById(R.id.btnViewSuccessLocation2)

        btnViewLastLocation =
            findViewById(R.id.btnViewLastLocation)

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

    private fun loadLogs() {

        val logs =
            getSharedPreferences(
                "BeaconLogs",
                MODE_PRIVATE
            )

        val log1Date =
            logs.getString(
                "log1_date",
                ""
            ) ?: ""

        val log1Location =
            logs.getString(
                "log1_location",
                ""
            ) ?: ""

        val log1Sms =
            logs.getString(
                "log1_sms",
                ""
            ) ?: ""

        if (log1Date.isNotEmpty()) {

            tvLog1Date.text =
                log1Date

            tvLog1Details.text =
                "EMERGENCY SOS broadcast to Trusted Circle with GPS Fix.\n" +
                        "Location: $log1Location • Alert: $log1Sms"

        } else {

            tvLog1Date.text =
                "No event"

            tvLog1Details.text =
                "No emergency SOS event recorded yet."
        }

        val log2Date =
            logs.getString(
                "log2_date",
                ""
            ) ?: ""

        val log2Location =
            logs.getString(
                "log2_location",
                ""
            ) ?: ""

        if (log2Date.isNotEmpty()) {

            tvLog2Date.text =
                log2Date

            tvLog2Details.text =
                "Previous trusted emergency event.\n" +
                        "Location: $log2Location"

        } else {

            tvLog2Date.text =
                "No previous event"
        }

        loadContact()
    }

    private fun loadContact() {

        val contacts =
            getSharedPreferences(
                "BeaconContacts",
                MODE_PRIVATE
            )

        val name =
            contacts.getString(
                "contact1_name",
                ""
            ) ?: ""

        val number =
            contacts.getString(
                "contact1_number",
                ""
            ) ?: ""

        if (name.isNotEmpty()) {

            tvLog2Title.text = name
            tvSuccess2Title.text = name
            tvLastSuccessTitle.text = name
        }

        if (number.isNotEmpty()) {

            tvContactNumber.text = number
            tvSuccess2Number.text = number
            tvLastSuccessNumber.text = number
        }
    }

    private fun setupFilters() {

        filterAll.setOnClickListener {

            showAll()
        }

        filterSuccess.setOnClickListener {

            showSuccess()
        }

        filterBlocked.setOnClickListener {

            showBlocked()
        }

        filterEmergency.setOnClickListener {

            showEmergency()
        }
    }

    private fun showAll() {

        cardEmergency.visibility = View.VISIBLE
        cardSuccess.visibility = View.VISIBLE
        cardSuccess2.visibility = View.VISIBLE
        cardBlocked.visibility = View.VISIBLE
        cardDiagnostic.visibility = View.VISIBLE
        cardLastSuccess.visibility = View.VISIBLE
    }

    private fun showSuccess() {

        cardEmergency.visibility = View.GONE
        cardSuccess.visibility = View.VISIBLE
        cardSuccess2.visibility = View.VISIBLE
        cardBlocked.visibility = View.GONE
        cardDiagnostic.visibility = View.VISIBLE
        cardLastSuccess.visibility = View.VISIBLE
    }

    private fun showBlocked() {

        cardEmergency.visibility = View.GONE
        cardSuccess.visibility = View.GONE
        cardSuccess2.visibility = View.GONE
        cardBlocked.visibility = View.VISIBLE
        cardDiagnostic.visibility = View.GONE
        cardLastSuccess.visibility = View.GONE
    }

    private fun showEmergency() {

        cardEmergency.visibility = View.VISIBLE
        cardSuccess.visibility = View.GONE
        cardSuccess2.visibility = View.GONE
        cardBlocked.visibility = View.GONE
        cardDiagnostic.visibility = View.GONE
        cardLastSuccess.visibility = View.GONE
    }

    private fun setupButtons() {

        btnClearLogs.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Clear Activity Logs?")
                .setMessage(
                    "This will remove your saved emergency history."
                )
                .setPositiveButton("Clear") { _, _ ->

                    clearLogs()
                }
                .setNegativeButton(
                    "Cancel",
                    null
                )
                .show()
        }

        btnViewEmergencyLocation.setOnClickListener {

            openSavedLocation()
        }

        btnViewSuccessLocation.setOnClickListener {

            openSavedLocation()
        }

        btnViewSuccessLocation2.setOnClickListener {

            openSavedLocation()
        }

        btnViewLastLocation.setOnClickListener {

            openSavedLocation()
        }
    }

    private fun clearLogs() {

        getSharedPreferences(
            "BeaconLogs",
            MODE_PRIVATE
        )
            .edit()
            .clear()
            .apply()

        loadLogs()

        Toast.makeText(
            this,
            "Activity logs cleared",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun openSavedLocation() {

        val location =
            getSharedPreferences(
                "BeaconLocation",
                MODE_PRIVATE
            )

        val latitude =
            location.getString(
                "latitude",
                ""
            ) ?: ""

        val longitude =
            location.getString(
                "longitude",
                ""
            ) ?: ""

        if (
            latitude.isEmpty() ||
            longitude.isEmpty()
        ) {

            Toast.makeText(
                this,
                "Location not available",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val geoUri =
            Uri.parse(
                "geo:$latitude,$longitude?q=$latitude,$longitude"
            )

        val intent =
            Intent(
                Intent.ACTION_VIEW,
                geoUri
            )

        try {

            startActivity(intent)

        } catch (e: Exception) {

            val webUri =
                Uri.parse(
                    "https://maps.google.com/?q=$latitude,$longitude"
                )

            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    webUri
                )
            )
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

            showAll()
        }

        navScreens.setOnClickListener {

            Toast.makeText(
                this,
                "Screens",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onResume() {

        super.onResume()

        loadLogs()
    }
}