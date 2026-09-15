package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import java.util.Locale

class CompleteFlowActivity : AppCompatActivity() {

    // Header
    private lateinit var btnDashboard: TextView
    private lateinit var btnRunSimulation: Button

    // ScrollView
    private lateinit var flowScrollView: NestedScrollView

    // Step Cards
    private lateinit var cardStep1: CardView
    private lateinit var cardStep2: CardView
    private lateinit var cardStep3: CardView
    private lateinit var cardStep4: CardView
    private lateinit var cardStep5: CardView
    private lateinit var cardStep6: CardView
    private lateinit var cardStep7: CardView
    private lateinit var cardStep8: CardView

    // Step Icons
    private lateinit var iconStep1: TextView
    private lateinit var iconStep2: TextView
    private lateinit var iconStep3: TextView
    private lateinit var iconStep4: TextView
    private lateinit var iconStep5: TextView
    private lateinit var iconStep6: TextView
    private lateinit var iconStep7: TextView
    private lateinit var iconStep8: TextView

    // Details
    private lateinit var detailStep1: TextView
    private lateinit var detailStep2: TextView
    private lateinit var detailStep3: TextView
    private lateinit var detailStep4: TextView
    private lateinit var detailStep5: TextView
    private lateinit var detailStep6: TextView
    private lateinit var detailStep7: TextView
    private lateinit var detailStep8: TextView

    // Status labels created from Kotlin
    private lateinit var statusStep1: TextView
    private lateinit var statusStep2: TextView
    private lateinit var statusStep3: TextView
    private lateinit var statusStep4: TextView
    private lateinit var statusStep5: TextView
    private lateinit var statusStep6: TextView
    private lateinit var statusStep7: TextView
    private lateinit var statusStep8: TextView

    // Navigation
    private lateinit var navHome: TextView
    private lateinit var navGps: TextView
    private lateinit var navSos: TextView
    private lateinit var navCircle: TextView
    private lateinit var navLogs: TextView
    private lateinit var navScreens: TextView

    // Simulation
    private val handler = Handler(Looper.getMainLooper())

    private var currentStep = 0
    private var simulationRunning = false

    private lateinit var cards: Array<CardView>
    private lateinit var icons: Array<TextView>
    private lateinit var statuses: Array<TextView>
    private lateinit var details: Array<TextView>

    // Original card color
    private val normalCardColor = Color.parseColor("#07101F")

    // Green completed color
    private val greenColor = Color.parseColor("#00E5B0")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_complete_flow)

        connectViews()

        createStatusLabels()

        setupArrays()

        loadFlowData()

        resetSimulation()

        btnDashboard.setOnClickListener {
            finish()
        }

        btnRunSimulation.setOnClickListener {

            if (!simulationRunning) {
                startSimulation()
            }
        }

        setupNavigation()
    }

    // =========================================================
    // CONNECT EXISTING XML
    // =========================================================

    private fun connectViews() {

        btnDashboard = findViewById(R.id.btnDashboard)
        btnRunSimulation = findViewById(R.id.btnRunSimulation)


        flowScrollView =
            findViewById<ViewGroup>(android.R.id.content)
                .getChildAt(0) as NestedScrollView

        // Cards
        cardStep1 = findViewById(R.id.cardStep1)
        cardStep2 = findViewById(R.id.cardStep2)
        cardStep3 = findViewById(R.id.cardStep3)
        cardStep4 = findViewById(R.id.cardStep4)
        cardStep5 = findViewById(R.id.cardStep5)
        cardStep6 = findViewById(R.id.cardStep6)
        cardStep7 = findViewById(R.id.cardStep7)
        cardStep8 = findViewById(R.id.cardStep8)

        // Icons
        iconStep1 = findViewById(R.id.iconStep1)
        iconStep2 = findViewById(R.id.iconStep2)
        iconStep3 = findViewById(R.id.iconStep3)
        iconStep4 = findViewById(R.id.iconStep4)
        iconStep5 = findViewById(R.id.iconStep5)
        iconStep6 = findViewById(R.id.iconStep6)
        iconStep7 = findViewById(R.id.iconStep7)
        iconStep8 = findViewById(R.id.iconStep8)

        // Details
        detailStep1 = findViewById(R.id.detailStep1)
        detailStep2 = findViewById(R.id.detailStep2)
        detailStep3 = findViewById(R.id.detailStep3)
        detailStep4 = findViewById(R.id.detailStep4)
        detailStep5 = findViewById(R.id.detailStep5)
        detailStep6 = findViewById(R.id.detailStep6)
        detailStep7 = findViewById(R.id.detailStep7)
        detailStep8 = findViewById(R.id.detailStep8)

        // Navigation
        navHome = findViewById(R.id.navHome)
        navGps = findViewById(R.id.navGps)
        navSos = findViewById(R.id.navSos)
        navCircle = findViewById(R.id.navCircle)
        navLogs = findViewById(R.id.navLogs)
        navScreens = findViewById(R.id.navScreens)
    }

    // =========================================================
    // CREATE "COMPLETED ✓" LABELS
    // No XML redesign needed
    // =========================================================

    private fun createStatusLabels() {

        statusStep1 = addStatusToCard(cardStep1)
        statusStep2 = addStatusToCard(cardStep2)
        statusStep3 = addStatusToCard(cardStep3)
        statusStep4 = addStatusToCard(cardStep4)
        statusStep5 = addStatusToCard(cardStep5)
        statusStep6 = addStatusToCard(cardStep6)
        statusStep7 = addStatusToCard(cardStep7)
        statusStep8 = addStatusToCard(cardStep8)
    }

    private fun addStatusToCard(card: CardView): TextView {

        val container =
            card.getChildAt(0) as ConstraintLayout

        val status = TextView(this)

        status.text = ""
        status.setTextColor(greenColor)
        status.textSize = 9f
        status.gravity = Gravity.CENTER
        status.setTypeface(
            status.typeface,
            android.graphics.Typeface.BOLD
        )

        status.id = android.view.View.generateViewId()

        val params =
            ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                dpToPx(25)
            )

        params.endToEnd =
            ConstraintLayout.LayoutParams.PARENT_ID

        params.topToTop =
            ConstraintLayout.LayoutParams.PARENT_ID

        params.topMargin = dpToPx(12)
        params.marginEnd = dpToPx(10)

        status.layoutParams = params

        container.addView(status)

        return status
    }

    // =========================================================
    // ARRAYS
    // =========================================================

    private fun setupArrays() {

        cards = arrayOf(
            cardStep1,
            cardStep2,
            cardStep3,
            cardStep4,
            cardStep5,
            cardStep6,
            cardStep7,
            cardStep8
        )

        icons = arrayOf(
            iconStep1,
            iconStep2,
            iconStep3,
            iconStep4,
            iconStep5,
            iconStep6,
            iconStep7,
            iconStep8
        )

        statuses = arrayOf(
            statusStep1,
            statusStep2,
            statusStep3,
            statusStep4,
            statusStep5,
            statusStep6,
            statusStep7,
            statusStep8
        )

        details = arrayOf(
            detailStep1,
            detailStep2,
            detailStep3,
            detailStep4,
            detailStep5,
            detailStep6,
            detailStep7,
            detailStep8
        )
    }

    // =========================================================
    // LOAD YOUR REAL DATA
    // =========================================================

    private fun loadFlowData() {

        val contacts =
            getSharedPreferences(
                "BeaconContacts",
                MODE_PRIVATE
            )

        val contactName =
            contacts.getString(
                "contact1_name",
                ""
            ) ?: ""

        val contactNumber =
            contacts.getString(
                "contact1_number",
                ""
            ) ?: ""

        val displayName =
            if (contactName.isNotEmpty()) {
                contactName
            } else {
                "Trusted Contact"
            }

        detailStep1.text =
            if (contactNumber.isNotEmpty()) {

                "$displayName sends emergency SMS from $contactNumber"

            } else {

                "Trusted contact sends emergency SMS."
            }

        // Keyword

        val control =
            getSharedPreferences(
                "BeaconControlSettings",
                MODE_PRIVATE
            )

        val keyword =
            control.getString(
                "keyword",
                "LOCATE"
            ) ?: "LOCATE"

        detailStep2.text =
            "Payload text: \"$keyword\" via cellular SMS."

        detailStep3.text =
            "Android SmsReceiver processes the message locally."

        detailStep4.text =
            if (contactNumber.isNotEmpty()) {

                "Sender verified against Trusted Circle whitelist."

            } else {

                "Sender checked against Trusted Circle."
            }

        // GPS

        val location =
            getSharedPreferences(
                "BeaconLocation",
                MODE_PRIVATE
            )

        val latitude =
            location.getString(
                "latitude",
                null
            )

        val longitude =
            location.getString(
                "longitude",
                null
            )

        val accuracy =
            location.getFloat(
                "accuracy",
                -1f
            )

        if (latitude != null && longitude != null) {

            val accuracyText =
                if (accuracy >= 0f) {

                    " ±${
                        String.format(
                            Locale.US,
                            "%.1f",
                            accuracy
                        )
                    }m"

                } else {
                    ""
                }

            detailStep5.text =
                "Acquired fix: $latitude, $longitude$accuracyText"

            val mapUrl =
                "https://maps.google.com/?q=$latitude,$longitude"

            detailStep6.text = mapUrl

            detailStep7.text =
                "BEACON location payload is ready for SMS transmission."

            detailStep8.text =
                "$displayName can open the Google Maps link."

        } else {

            detailStep5.text =
                "Waiting for GPS data..."

            detailStep6.text =
                "Map URL not available"

            detailStep7.text =
                "Location payload ready for SMS dispatch."

            detailStep8.text =
                "Emergency location exchange completed."
        }
    }

    // =========================================================
    // START SIMULATION
    // =========================================================

    private fun startSimulation() {

        handler.removeCallbacksAndMessages(null)

        resetSimulation()

        simulationRunning = true
        currentStep = 0

        btnRunSimulation.isEnabled = false

        btnRunSimulation.text =
            "△  Running..."

        // Start first step
        handler.postDelayed({
            runNextStep()
        }, 300)
    }

    // =========================================================
    // RUN STEP BY STEP
    // =========================================================

    private fun runNextStep() {

        if (currentStep >= cards.size) {

            simulationFinished()

            return
        }

        val stepIndex = currentStep

        // Show current step
        showExecuting(stepIndex)

        // Automatically scroll to current card
        scrollToCard(cards[stepIndex])

        // Wait before completing
        handler.postDelayed({

            // Complete current step
            showCompleted(stepIndex)

            // Move to next
            currentStep = stepIndex + 1

            // Small pause between steps
            handler.postDelayed({

                runNextStep()

            }, 250)

        }, 1100)
    }

    // =========================================================
    // EXECUTING
    // =========================================================

    private fun showExecuting(index: Int) {

        statuses[index].text =
            "EXECUTING"

        statuses[index].setTextColor(
            greenColor
        )

        /*
         * Highlight active card.
         * We keep your same dark UI and only add green outline.
         */
        cards[index].setCardBackgroundColor(
            Color.parseColor("#071A1C")
        )

        cards[index].cardElevation =
            dpToPx(2).toFloat()

        // Green icon
        icons[index].setTextColor(
            greenColor
        )
    }

    // =========================================================
    // COMPLETED
    // =========================================================

    private fun showCompleted(index: Int) {

        statuses[index].text =
            "COMPLETED ✓"

        statuses[index].setTextColor(
            greenColor
        )

        /*
         * Keep completed card highlighted.
         */
        cards[index].setCardBackgroundColor(
            Color.parseColor("#071A1C")
        )

        icons[index].setTextColor(
            greenColor
        )
    }

    // =========================================================
    // RESET
    // =========================================================

    private fun resetSimulation() {

        currentStep = 0

        for (i in cards.indices) {

            statuses[i].text = ""

            cards[i].setCardBackgroundColor(
                normalCardColor
            )

            cards[i].cardElevation = 0f

            icons[i].setTextColor(
                Color.parseColor("#D8E1EC")
            )
        }

        btnRunSimulation.isEnabled = true

        btnRunSimulation.text =
            "▶  Run Simulation"
    }

    // =========================================================
    // AUTO SCROLL
    // =========================================================

    private fun scrollToCard(card: CardView) {

        card.post {

            val cardLocation =
                IntArray(2)

            val scrollLocation =
                IntArray(2)

            card.getLocationOnScreen(
                cardLocation
            )

            flowScrollView.getLocationOnScreen(
                scrollLocation
            )

            /*
             * Keep active card around upper-middle area.
             */
            val target =
                flowScrollView.scrollY +
                        cardLocation[1] -
                        scrollLocation[1] -
                        dpToPx(110)

            flowScrollView.smoothScrollTo(
                0,
                target.coerceAtLeast(0)
            )
        }
    }

    // =========================================================
    // FINISHED
    // =========================================================

    private fun simulationFinished() {

        simulationRunning = false

        btnRunSimulation.isEnabled = true

        btnRunSimulation.text =
            "✓  Completed"

        /*
         * IMPORTANT:
         * No Toast here.
         * No white dialogue box.
         *
         * Completion is shown directly inside cards.
         */

        handler.postDelayed({

            if (!isFinishing) {

                btnRunSimulation.text =
                    "▶  Run Again"
            }

        }, 2000)
    }

    // =========================================================
    // DP TO PIXEL
    // =========================================================

    private fun dpToPx(dp: Int): Int {

        return (
                dp *
                        resources.displayMetrics.density
                ).toInt()
    }

    // =========================================================
    // NAVIGATION
    // =========================================================

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

    // =========================================================
    // REFRESH
    // =========================================================

    override fun onResume() {
        super.onResume()

        if (!simulationRunning) {
            loadFlowData()
        }
    }

    // =========================================================
    // CLEAN UP
    // =========================================================

    override fun onDestroy() {

        handler.removeCallbacksAndMessages(null)

        super.onDestroy()
    }
}