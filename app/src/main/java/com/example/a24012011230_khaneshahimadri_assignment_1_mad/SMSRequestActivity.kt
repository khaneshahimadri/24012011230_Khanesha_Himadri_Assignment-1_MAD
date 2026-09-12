package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SmsRequestActivity : AppCompatActivity() {

    private lateinit var btnDashboard: TextView

    private lateinit var tvSenderAvatar: TextView
    private lateinit var tvSenderName: TextView
    private lateinit var tvSenderNumber: TextView
    private lateinit var tvTrustedBadge: TextView

    private lateinit var tvPayload: TextView
    private lateinit var tvKeywordMatched: TextView
    private lateinit var tvAccepted: TextView

    private lateinit var btnTrusted1: Button
    private lateinit var btnTrusted2: Button
    private lateinit var btnUnknown: Button

    private lateinit var etMessage: EditText
    private lateinit var btnTransmit: Button

    private lateinit var navHome: TextView
    private lateinit var navGps: TextView
    private lateinit var navSos: TextView
    private lateinit var navCircle: TextView
    private lateinit var navLogs: TextView
    private lateinit var navScreens: TextView

    private var selectedSenderType = 1

    private var contact1Name = ""
    private var contact1Number = ""

    private var contact2Name = ""
    private var contact2Number = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_smsrequest)

        connectViews()

        loadContacts()

        selectContact1()

        setupButtons()

        setupNavigation()
    }


    private fun connectViews() {

        btnDashboard =
            findViewById(R.id.btnDashboard)

        tvSenderAvatar =
            findViewById(R.id.tvSenderAvatar)

        tvSenderName =
            findViewById(R.id.tvSenderName)

        tvSenderNumber =
            findViewById(R.id.tvSenderNumber)

        tvTrustedBadge =
            findViewById(R.id.tvTrustedBadge)

        tvPayload =
            findViewById(R.id.tvPayload)

        tvKeywordMatched =
            findViewById(R.id.tvKeywordMatched)

        tvAccepted =
            findViewById(R.id.tvAccepted)

        btnTrusted1 =
            findViewById(R.id.btnTrusted1)

        btnTrusted2 =
            findViewById(R.id.btnTrusted2)

        btnUnknown =
            findViewById(R.id.btnUnknown)

        etMessage =
            findViewById(R.id.etMessage)

        btnTransmit =
            findViewById(R.id.btnTransmit)

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


    private fun loadContacts() {

        val preferences =
            getSharedPreferences(
                "BeaconContacts",
                MODE_PRIVATE
            )

        contact1Name =
            preferences.getString(
                "contact1_name",
                ""
            ) ?: ""

        contact1Number =
            preferences.getString(
                "contact1_number",
                ""
            ) ?: ""

        contact2Name =
            preferences.getString(
                "contact2_name",
                ""
            ) ?: ""

        contact2Number =
            preferences.getString(
                "contact2_number",
                ""
            ) ?: ""


        btnTrusted1.text =
            if (contact1Name.isNotEmpty()) {

                "$contact1Name\n(Trusted)"

            } else {

                "Contact 1\n(Trusted)"
            }


        btnTrusted2.text =
            if (contact2Name.isNotEmpty()) {

                "$contact2Name\n(Trusted)"

            } else {

                "Contact 2\n(Trusted)"
            }
    }


    private fun setupButtons() {

        btnDashboard.setOnClickListener {

            finish()
        }


        btnTrusted1.setOnClickListener {

            selectedSenderType = 1

            selectContact1()
        }


        btnTrusted2.setOnClickListener {

            selectedSenderType = 2

            selectContact2()
        }


        btnUnknown.setOnClickListener {

            selectedSenderType = 3

            selectUnknown()
        }


        btnTransmit.setOnClickListener {

            processSimulatedSms()
        }
    }


    private fun selectContact1() {

        val name =
            if (contact1Name.isNotEmpty()) {
                contact1Name
            } else {
                "Trusted Contact 1"
            }


        tvSenderName.text =
            name

        tvSenderNumber.text =
            if (contact1Number.isNotEmpty()) {
                contact1Number
            } else {
                "No number selected"
            }

        tvSenderAvatar.text =
            getInitials(name)

        tvTrustedBadge.text =
            "✓ TRUSTED SENDER"


        btnTrusted1.setBackgroundColor(
            Color.parseColor("#073E35")
        )

        btnTrusted2.setBackgroundColor(
            Color.parseColor("#07101E")
        )

        btnUnknown.setBackgroundColor(
            Color.parseColor("#07101E")
        )
    }


    private fun selectContact2() {

        val name =
            if (contact2Name.isNotEmpty()) {
                contact2Name
            } else {
                "Trusted Contact 2"
            }


        tvSenderName.text =
            name

        tvSenderNumber.text =
            if (contact2Number.isNotEmpty()) {
                contact2Number
            } else {
                "No number selected"
            }

        tvSenderAvatar.text =
            getInitials(name)

        tvTrustedBadge.text =
            "✓ TRUSTED SENDER"


        btnTrusted1.setBackgroundColor(
            Color.parseColor("#07101E")
        )

        btnTrusted2.setBackgroundColor(
            Color.parseColor("#073E35")
        )

        btnUnknown.setBackgroundColor(
            Color.parseColor("#07101E")
        )
    }


    private fun selectUnknown() {

        tvSenderName.text =
            "Unknown Number"

        tvSenderNumber.text =
            "+91 00000 00000"

        tvSenderAvatar.text =
            "?"

        tvTrustedBadge.text =
            "✕ BLOCKED SENDER"


        btnTrusted1.setBackgroundColor(
            Color.parseColor("#07101E")
        )

        btnTrusted2.setBackgroundColor(
            Color.parseColor("#07101E")
        )

        btnUnknown.setBackgroundColor(
            Color.parseColor("#45141E")
        )
    }


    private fun processSimulatedSms() {

        val enteredMessage =
            etMessage.text
                .toString()
                .trim()

        if (enteredMessage.isEmpty()) {

            Toast.makeText(
                this,
                "Enter SMS message",
                Toast.LENGTH_SHORT
            ).show()

            return
        }


        val settings =
            getSharedPreferences(
                "BeaconControlSettings",
                MODE_PRIVATE
            )

        val keyword =
            settings.getString(
                "keyword",
                "LOCATE"
            ) ?: "LOCATE"


        tvPayload.text =
            "\"$enteredMessage\""


        // Unknown sender

        if (selectedSenderType == 3) {

            tvKeywordMatched.text =
                "SENDER: BLOCKED"

            tvAccepted.text =
                "STATUS: REJECTED"

            Toast.makeText(
                this,
                "Unknown sender blocked",
                Toast.LENGTH_SHORT
            ).show()

            return
        }


        // Trusted sender but wrong keyword

        if (!enteredMessage.equals(keyword, ignoreCase = true)) {

            tvKeywordMatched.text =
                "KEYWORD: NO MATCH"

            tvAccepted.text =
                "STATUS: REJECTED"

            Toast.makeText(
                this,
                "Keyword does not match",
                Toast.LENGTH_SHORT
            ).show()

            return
        }


        // Trusted sender + correct keyword

        tvKeywordMatched.text =
            "KEYWORD: MATCHED"

        tvAccepted.text =
            "STATUS: ACCEPTED"


        Toast.makeText(
            this,
            "SMS accepted. GPS reply pipeline started.",
            Toast.LENGTH_SHORT
        ).show()
    }


    private fun getInitials(
        name: String
    ): String {

        if (name.isBlank()) {

            return "TC"
        }


        val parts =
            name.trim()
                .split(" ")


        var initials =
            parts[0]
                .take(1)
                .uppercase()


        if (parts.size > 1) {

            initials +=
                parts[1]
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
}