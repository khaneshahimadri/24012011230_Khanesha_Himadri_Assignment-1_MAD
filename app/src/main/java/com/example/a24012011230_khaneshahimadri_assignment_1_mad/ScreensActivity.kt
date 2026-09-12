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

    private lateinit var navHome: TextView
    private lateinit var navGps: TextView
    private lateinit var navSos: TextView
    private lateinit var navCircle: TextView
    private lateinit var navLogs: TextView
    private lateinit var navScreens: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_screens)

        connectViews()
        setupCards()
        setupBottomNavigation()
    }

    private fun connectViews() {

        btnClose = findViewById(R.id.btnClose)

        cardHomeDashboard = findViewById(R.id.cardHomeDashboard)
        cardEmergencySOS = findViewById(R.id.cardEmergencySOS)
        cardBeaconControl = findViewById(R.id.cardBeaconControl)
        cardTrustedCircle = findViewById(R.id.cardTrustedCircle)
        cardOfflineArchitecture = findViewById(R.id.cardOfflineArchitecture)
        cardGpsTactical = findViewById(R.id.cardGpsTactical)
        cardSmsParser = findViewById(R.id.cardSmsParser)
        cardLocationResponse = findViewById(R.id.cardLocationResponse)
        cardActivityLog = findViewById(R.id.cardActivityLog)
        cardSecurity = findViewById(R.id.cardSecurity)
        cardHealth = findViewById(R.id.cardHealth)
        cardCompleteFlow = findViewById(R.id.cardCompleteFlow)

        navHome = findViewById(R.id.navHome)
        navGps = findViewById(R.id.navGps)
        navSos = findViewById(R.id.navSos)
        navCircle = findViewById(R.id.navCircle)
        navLogs = findViewById(R.id.navLogs)
        navScreens = findViewById(R.id.navScreens)
    }

    private fun setupCards() {

        btnClose.setOnClickListener {
            finish()
        }

        cardHomeDashboard.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
            finish()
        }

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

        cardBeaconControl.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    BeaconControlActivity::class.java
                )
            )
        }



        cardTrustedCircle.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    CircleActivity::class.java
                )
            )
        }

        cardOfflineArchitecture.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    OfflineArchitectureActivity::class.java
                )
            )
        }


        cardGpsTactical.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    GpsActivity::class.java
                )
            )
        }

        cardSmsParser.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SmsRequestActivity::class.java
                )
            )
        }

        cardLocationResponse.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    LocationResponseActivity::class.java
                )
            )
        }

        cardActivityLog.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    LogsActivity::class.java
                )
            )
        }

        cardSecurity.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SecurityPrivacyActivity::class.java
                )
            )
        }

        cardHealth.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    BeaconHealthActivity::class.java
                )
            )
        }



        cardCompleteFlow.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    CompleteFlowActivity::class.java
                )
            )
        }
    }


    private fun setupBottomNavigation() {

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