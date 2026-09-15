package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class CompleteFlowActivity : AppCompatActivity() {

    // Header
    private lateinit var btnDashboard: TextView
    private lateinit var btnRunSimulation: Button

    // Flow details
    private lateinit var detailStep1: TextView
    private lateinit var detailStep2: TextView
    private lateinit var detailStep3: TextView
    private lateinit var detailStep4: TextView
    private lateinit var detailStep5: TextView
    private lateinit var detailStep6: TextView
    private lateinit var detailStep7: TextView
    private lateinit var detailStep8: TextView

    // Bottom navigation
    private lateinit var navHome: TextView
    private lateinit var navGps: TextView
    private lateinit var navSos: TextView
    private lateinit var navCircle: TextView
    private lateinit var navLogs: TextView
    private lateinit var navScreens: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_complete_flow)

        connectViews()
        loadFlowData()

        // Dashboard button
        btnDashboard.setOnClickListener {
            finish()
        }

        // Run simulation button
        btnRunSimulation.setOnClickListener {
            runSimulation()
        }

        setupNavigation()
    }

    // =========================================================
    // CONNECT XML VIEWS
    // =========================================================

    private fun connectViews() {

        btnDashboard = findViewById(R.id.btnDashboard)
        btnRunSimulation = findViewById(R.id.btnRunSimulation)

        detailStep1 = findViewById(R.id.detailStep1)
        detailStep2 = findViewById(R.id.detailStep2)
        detailStep3 = findViewById(R.id.detailStep3)
        detailStep4 = findViewById(R.id.detailStep4)
        detailStep5 = findViewById(R.id.detailStep5)
        detailStep6 = findViewById(R.id.detailStep6)
        detailStep7 = findViewById(R.id.detailStep7)
        detailStep8 = findViewById(R.id.detailStep8)

        navHome = findViewById(R.id.navHome)
        navGps = findViewById(R.id.navGps)
        navSos = findViewById(R.id.navSos)
        navCircle = findViewById(R.id.navCircle)
        navLogs = findViewById(R.id.navLogs)
        navScreens = findViewById(R.id.navScreens)
    }

    // =========================================================
    // LOAD REAL DATA
    // =========================================================

    private fun loadFlowData() {

        // -----------------------------------------------------
        // STEP 1 - TRUSTED CONTACT
        // -----------------------------------------------------

        val contacts = getSharedPreferences(
            "BeaconContacts",
            MODE_PRIVATE
        )

        val contactName = contacts.getString(
            "contact1_name",
            ""
        ) ?: ""

        val contactNumber = contacts.getString(
            "contact1_number",
            ""
        ) ?: ""

        val displayName = if (contactName.isNotEmpty()) {
            contactName
        } else {
            "Trusted Contact"
        }

        detailStep1.text = if (contactNumber.isNotEmpty()) {
            "$displayName sends emergency SMS from $contactNumber"
        } else {
            "No trusted contact configured yet."
        }

        // -----------------------------------------------------
        // STEP 2 - LOCATE KEYWORD
        // -----------------------------------------------------

        val control = getSharedPreferences(
            "BeaconControlSettings",
            MODE_PRIVATE
        )

        val keyword = control.getString(
            "keyword",
            "LOCATE"
        ) ?: "LOCATE"

        detailStep2.text =
            "Payload text: \"$keyword\" via cellular SMS."

        // -----------------------------------------------------
        // STEP 3 - SMS RECEIVER
        // -----------------------------------------------------

        detailStep3.text =
            "Android SmsReceiver processes the message locally."

        // -----------------------------------------------------
        // STEP 4 - VERIFY TRUSTED CONTACT
        // -----------------------------------------------------

        detailStep4.text = if (contactNumber.isNotEmpty()) {
            "Sender verified against Trusted Circle whitelist."
        } else {
            "Verification unavailable because no trusted contact is saved."
        }

        // -----------------------------------------------------
        // GET SAVED GPS LOCATION
        // -----------------------------------------------------

        val location = getSharedPreferences(
            "BeaconLocation",
            MODE_PRIVATE
        )

        val latitude = location.getString(
            "latitude",
            null
        )

        val longitude = location.getString(
            "longitude",
            null
        )

        val accuracy = location.getFloat(
            "accuracy",
            -1f
        )

        // -----------------------------------------------------
        // STEPS 5 - 8
        // -----------------------------------------------------

        if (latitude != null && longitude != null) {

            val accuracyText = if (accuracy >= 0f) {

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

            // Step 5
            detailStep5.text =
                "Acquired fix: $latitude, $longitude$accuracyText"

            // Step 6
            val mapUrl =
                "https://maps.google.com/?q=$latitude,$longitude"

            detailStep6.text = mapUrl

            // Step 7
            detailStep7.text =
                "BEACON location payload is ready for SMS transmission."

            // Step 8
            detailStep8.text =
                "$displayName can open the Google Maps link to view the coordinates."

        } else {

            detailStep5.text =
                "GPS coordinates are not available yet."

            detailStep6.text =
                "Map URL cannot be generated without coordinates."

            detailStep7.text =
                "SMS response waits for a valid saved GPS fix."

            detailStep8.text =
                "Location exchange is waiting for GPS data."
        }
    }

    // =========================================================
    // RUN SIMULATION
    // =========================================================

    private fun runSimulation() {

        // Get trusted contact
        val contacts = getSharedPreferences(
            "BeaconContacts",
            MODE_PRIVATE
        )

        val contactName = contacts.getString(
            "contact1_name",
            "Trusted Contact"
        ) ?: "Trusted Contact"

        val number = contacts.getString(
            "contact1_number",
            ""
        ) ?: ""

        // Check contact
        if (number.isEmpty()) {

            Toast.makeText(
                this,
                "Simulation stopped: Add a trusted contact first",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        // Get keyword
        val control = getSharedPreferences(
            "BeaconControlSettings",
            MODE_PRIVATE
        )

        val keyword = control.getString(
            "keyword",
            "LOCATE"
        ) ?: "LOCATE"

        // Get GPS
        val location = getSharedPreferences(
            "BeaconLocation",
            MODE_PRIVATE
        )

        val latitude = location.getString(
            "latitude",
            null
        )

        val longitude = location.getString(
            "longitude",
            null
        )

        // Check GPS
        if (latitude == null || longitude == null) {

            Toast.makeText(
                this,
                "Simulation stopped: Open GPS screen and get location first",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        // Create Google Maps URL
        val mapUrl =
            "https://maps.google.com/?q=$latitude,$longitude"

        // Update flow screen
        detailStep1.text =
            "✓ $contactName identified as Trusted Circle contact."

        detailStep2.text =
            "✓ SMS \"$keyword\" received."

        detailStep3.text =
            "✓ SmsReceiver intercepted the incoming message."

        detailStep4.text =
            "✓ Sender verified against Trusted Circle whitelist."

        detailStep5.text =
            "✓ GPS fix acquired: $latitude, $longitude"

        detailStep6.text =
            "✓ Google Maps link created:\n$mapUrl"

        detailStep7.text =
            "✓ BEACON SMS response prepared successfully."

        detailStep8.text =
            "✓ Location ready for delivery to $contactName."

        btnRunSimulation.text =
            "✓ SIMULATION COMPLETE"

        Toast.makeText(
            this,
            "BEACON Autonomous Flow Completed ✓",
            Toast.LENGTH_LONG
        ).show()
    }

    // =========================================================
    // BOTTOM NAVIGATION
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

    // =========================================================
    // REFRESH WHEN RETURNING TO SCREEN
    // =========================================================

    override fun onResume() {
        super.onResume()

        loadFlowData()

        btnRunSimulation.text =
            "▶ RUN SIMULATION"
    }
}