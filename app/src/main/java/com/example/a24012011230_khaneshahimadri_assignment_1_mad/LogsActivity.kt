package com.example.a24012011230_khaneshahimadri_assignment_1_mad

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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

        tvBack = findViewById(R.id.tvBack)
        tvLogCount = findViewById(R.id.tvLogCount)

        tvLog1Date = findViewById(R.id.tvLog1Date)
        tvLog1Details = findViewById(R.id.tvLog1Details)

        tvLog2Date = findViewById(R.id.tvLog2Date)
        tvLog2Details = findViewById(R.id.tvLog2Details)

        btnClearLogs = findViewById(R.id.btnClearLogs)

        tvBack.setOnClickListener {
            finish()
        }

        btnClearLogs.setOnClickListener {

            tvLogCount.text = "0 emergency events recorded"

            tvLog1Date.text = "No events yet"
            tvLog1Details.text = "Location: --   •   SMS: --"

            tvLog2Date.text = "No events yet"
            tvLog2Details.text = "Location: --   •   SMS: --"
        }
    }
}