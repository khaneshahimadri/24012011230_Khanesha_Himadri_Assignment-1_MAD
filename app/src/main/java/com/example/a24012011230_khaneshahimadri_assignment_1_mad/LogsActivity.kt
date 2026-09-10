package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LogsActivity : AppCompatActivity() {

    private lateinit var tvBack: TextView
    private lateinit var tvLogCount: TextView

    private lateinit var tvLog1Date: TextView
    private lateinit var tvLog1Details: TextView

    private lateinit var tvLog2Date: TextView
    private lateinit var tvLog2Details: TextView

    private lateinit var btnClearLogs: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_logs)

        // Connect XML views

        tvBack = findViewById(R.id.tvBack)

        tvLogCount = findViewById(R.id.tvLogCount)

        tvLog1Date = findViewById(R.id.tvLog1Date)
        tvLog1Details = findViewById(R.id.tvLog1Details)

        tvLog2Date = findViewById(R.id.tvLog2Date)
        tvLog2Details = findViewById(R.id.tvLog2Details)

        btnClearLogs = findViewById(R.id.btnClearLogs)


        // Back button

        tvBack.setOnClickListener {
            finish()
        }


        // Load saved SOS history

        loadLogs()


        // Clear button

        btnClearLogs.setOnClickListener {

            clearLogs()
        }
    }


    // ---------------------------------------------------------
    // LOAD SAVED LOGS
    // ---------------------------------------------------------

    private fun loadLogs() {

        val preferences =
            getSharedPreferences(
                "BeaconLogs",
                MODE_PRIVATE
            )


        val logCount =
            preferences.getInt(
                "log_count",
                0
            )


        tvLogCount.text =
            "$logCount emergency events"


        // Latest log

        val log1Date =
            preferences.getString(
                "log1_date",
                ""
            ) ?: ""

        val log1Location =
            preferences.getString(
                "log1_location",
                ""
            ) ?: ""

        val log1Sms =
            preferences.getString(
                "log1_sms",
                ""
            ) ?: ""


        // Previous log

        val log2Date =
            preferences.getString(
                "log2_date",
                ""
            ) ?: ""

        val log2Location =
            preferences.getString(
                "log2_location",
                ""
            ) ?: ""

        val log2Sms =
            preferences.getString(
                "log2_sms",
                ""
            ) ?: ""


        // Display first log

        if (log1Date.isNotEmpty()) {

            tvLog1Date.text =
                log1Date

            tvLog1Details.text =
                "Location: $log1Location\nEmergency Alert: $log1Sms"

        } else {

            tvLog1Date.text =
                "No emergency recorded"

            tvLog1Details.text =
                "Location: --\nEmergency Alert: --"
        }


        // Display second log

        if (log2Date.isNotEmpty()) {

            tvLog2Date.text =
                log2Date

            tvLog2Details.text =
                "Location: $log2Location\nEmergency Alert: $log2Sms"

        } else {

            tvLog2Date.text =
                "No emergency recorded"

            tvLog2Details.text =
                "Location: --\nEmergency Alert: --"
        }
    }


    // ---------------------------------------------------------
    // CLEAR LOGS
    // ---------------------------------------------------------

    private fun clearLogs() {

        val preferences =
            getSharedPreferences(
                "BeaconLogs",
                MODE_PRIVATE
            )


        preferences
            .edit()
            .clear()
            .apply()


        loadLogs()


        Toast.makeText(
            this,
            "Emergency history cleared",
            Toast.LENGTH_SHORT
        ).show()
    }
}