package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SecurityPrivacyActivity : AppCompatActivity() {

    private lateinit var btnDashboard: TextView

    private lateinit var switchWhitelist: Switch
    private lateinit var switchUnknown: Switch
    private lateinit var switchStealth: Switch

    private lateinit var tvCooldownValue: TextView

    private lateinit var btn15: Button
    private lateinit var btn30: Button
    private lateinit var btn60: Button
    private lateinit var btn120: Button

    private lateinit var navHome: TextView
    private lateinit var navGps: TextView
    private lateinit var navSos: TextView
    private lateinit var navCircle: TextView
    private lateinit var navLogs: TextView
    private lateinit var navScreens: TextView

    private var cooldown = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_security_privacy
        )

        connectViews()

        loadSettings()

        setupButtons()

        setupNavigation()
    }


    private fun connectViews() {

        btnDashboard =
            findViewById(R.id.btnDashboard)

        switchWhitelist =
            findViewById(R.id.switchWhitelist)

        switchUnknown =
            findViewById(R.id.switchUnknown)

        switchStealth =
            findViewById(R.id.switchStealth)

        tvCooldownValue =
            findViewById(R.id.tvCooldownValue)

        btn15 =
            findViewById(R.id.btn15)

        btn30 =
            findViewById(R.id.btn30)

        btn60 =
            findViewById(R.id.btn60)

        btn120 =
            findViewById(R.id.btn120)

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


    private fun loadSettings() {

        val preferences =
            getSharedPreferences(
                "BeaconSecurity",
                MODE_PRIVATE
            )

        switchWhitelist.isChecked =
            preferences.getBoolean(
                "whitelist_only",
                true
            )

        switchUnknown.isChecked =
            preferences.getBoolean(
                "silent_unknown",
                true
            )

        switchStealth.isChecked =
            preferences.getBoolean(
                "stealth_mode",
                false
            )

        cooldown =
            preferences.getInt(
                "cooldown",
                30
            )

        updateCooldownButtons()
    }


    private fun setupButtons() {

        btnDashboard.setOnClickListener {
            finish()
        }


        switchWhitelist.setOnCheckedChangeListener { _, _ ->
            saveSettings()
        }


        switchUnknown.setOnCheckedChangeListener { _, _ ->
            saveSettings()
        }


        switchStealth.setOnCheckedChangeListener { _, _ ->
            saveSettings()
        }


        btn15.setOnClickListener {

            cooldown = 15

            saveSettings()

            updateCooldownButtons()
        }


        btn30.setOnClickListener {

            cooldown = 30

            saveSettings()

            updateCooldownButtons()
        }


        btn60.setOnClickListener {

            cooldown = 60

            saveSettings()

            updateCooldownButtons()
        }


        btn120.setOnClickListener {

            cooldown = 120

            saveSettings()

            updateCooldownButtons()
        }
    }


    private fun saveSettings() {

        getSharedPreferences(
            "BeaconSecurity",
            MODE_PRIVATE
        )
            .edit()

            .putBoolean(
                "whitelist_only",
                switchWhitelist.isChecked
            )

            .putBoolean(
                "silent_unknown",
                switchUnknown.isChecked
            )

            .putBoolean(
                "stealth_mode",
                switchStealth.isChecked
            )

            .putInt(
                "cooldown",
                cooldown
            )

            .apply()
    }


    private fun updateCooldownButtons() {

        tvCooldownValue.text =
            "$cooldown seconds"

        val normal =
            Color.parseColor("#030817")

        val selected =
            Color.parseColor("#552080")


        btn15.setBackgroundColor(
            if (cooldown == 15) {
                selected
            } else {
                normal
            }
        )

        btn30.setBackgroundColor(
            if (cooldown == 30) {
                selected
            } else {
                normal
            }
        )

        btn60.setBackgroundColor(
            if (cooldown == 60) {
                selected
            } else {
                normal
            }
        )

        btn120.setBackgroundColor(
            if (cooldown == 120) {
                selected
            } else {
                normal
            }
        )
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
}