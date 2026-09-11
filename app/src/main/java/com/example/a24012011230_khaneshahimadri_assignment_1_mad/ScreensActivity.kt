package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class ScreensActivity : AppCompatActivity() {

    private lateinit var btnClose: TextView

    private lateinit var cardHomeDashboard: CardView
    private lateinit var cardEmergencySOS: CardView
    private lateinit var cardBeaconControl: CardView
    private lateinit var cardTrustedCircle: CardView
    private lateinit var cardOfflineArchitecture: CardView
    private lateinit var cardGpsTactical: CardView
    private lateinit var cardSmsParser: CardView
    private lateinit var cardLocationResponse: CardView
    private lateinit var cardActivityLog: CardView
    private lateinit var cardSecurity: CardView
    private lateinit var cardHealth: CardView
    private lateinit var cardCompleteFlow: CardView

    // Bottom Navigation

    private lateinit var navHome: TextView
    private lateinit var navGps: TextView
    private lateinit var navSos: TextView
    private lateinit var navCircle: TextView
    private lateinit var navLogs: TextView
    private lateinit var navScreens: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_screens)

        // =========================
        // CONNECT VIEWS
        // =========================

        btnClose =
            findViewById(R.id.btnClose)

        cardHomeDashboard =
            findViewById(R.id.cardHomeDashboard)

        cardEmergencySOS =
            findViewById(R.id.cardEmergencySOS)

        cardBeaconControl =
            findViewById(R.id.cardBeaconControl)

        cardTrustedCircle =
            findViewById(R.id.cardTrustedCircle)

        cardOfflineArchitecture =
            findViewById(R.id.cardOfflineArchitecture)

        cardGpsTactical =
            findViewById(R.id.cardGpsTactical)

        cardSmsParser =
            findViewById(R.id.cardSmsParser)

        cardLocationResponse =
            findViewById(R.id.cardLocationResponse)

        cardActivityLog =
            findViewById(R.id.cardActivityLog)

        cardSecurity =
            findViewById(R.id.cardSecurity)

        cardHealth =
            findViewById(R.id.cardHealth)

        cardCompleteFlow =
            findViewById(R.id.cardCompleteFlow)


        // Bottom Navigation

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


        // =========================
        // CLOSE BUTTON
        // =========================

        btnClose.setOnClickListener {

            finish()
        }


        // =========================
        // 1. HOME DASHBOARD
        // =========================

        cardHomeDashboard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )

            finish()
        }


        // =========================
        // 2. EMERGENCY SOS
        // =========================

        cardEmergencySOS.setOnClickListener {

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


        // =========================
        // 3. BEACON CONTROL
        // =========================

        cardBeaconControl.setOnClickListener {

            Toast.makeText(
                this,
                "Beacon Control",
                Toast.LENGTH_SHORT
            ).show()
        }


        // =========================
        // 4. TRUSTED CIRCLE
        // =========================

        cardTrustedCircle.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    CircleActivity::class.java
                )
            )
        }


        // =========================
        // 5. OFFLINE ARCHITECTURE
        // =========================

        cardOfflineArchitecture.setOnClickListener {

            Toast.makeText(
                this,
                "Offline Architecture",
                Toast.LENGTH_SHORT
            ).show()
        }


        // =========================
        // 6. GPS
        // =========================

        cardGpsTactical.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    GpsActivity::class.java
                )
            )
        }


        // =========================
        // 7. SMS PARSER
        // =========================

        cardSmsParser.setOnClickListener {

            Toast.makeText(
                this,
                "SMS Request Parser",
                Toast.LENGTH_SHORT
            ).show()
        }


        // =========================
        // 8. LOCATION RESPONSE
        // =========================

        cardLocationResponse.setOnClickListener {

            Toast.makeText(
                this,
                "Location Response",
                Toast.LENGTH_SHORT
            ).show()
        }


        // =========================
        // 9. ACTIVITY LOG
        // =========================

        cardActivityLog.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    LogsActivity::class.java
                )
            )
        }


        // =========================
        // 10. SECURITY
        // =========================

        cardSecurity.setOnClickListener {

            Toast.makeText(
                this,
                "Security & Privacy",
                Toast.LENGTH_SHORT
            ).show()
        }


        // =========================
        // 11. HEALTH
        // =========================

        cardHealth.setOnClickListener {

            Toast.makeText(
                this,
                "Beacon Health Diagnostics",
                Toast.LENGTH_SHORT
            ).show()
        }


        // =========================
        // 12. COMPLETE FLOW
        // =========================

        cardCompleteFlow.setOnClickListener {

            Toast.makeText(
                this,
                "Complete Beacon Flow",
                Toast.LENGTH_SHORT
            ).show()
        }


        // =========================
        // BOTTOM NAVIGATION
        // =========================

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

            Toast.makeText(
                this,
                "You are already on Screens",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}