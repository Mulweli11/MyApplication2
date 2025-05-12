package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CashSetupActivity : AppCompatActivity() {

    private lateinit var amountText: TextView
    private lateinit var continueButton: Button
    private lateinit var dbHelper: UserDatabaseHelper
    private var amountString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_setup)

        amountText = findViewById(R.id.amount)
        continueButton = findViewById(R.id.nextButton)
        dbHelper = UserDatabaseHelper(this)

        val buttons = mapOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2", R.id.btn3 to "3",
            R.id.btn4 to "4", R.id.btn5 to "5", R.id.btn6 to "6",
            R.id.btn7 to "7", R.id.btn8 to "8", R.id.btn9 to "9",
            R.id.btnDot to "."
        )

        buttons.forEach { (id, value) ->
            findViewById<Button>(id).setOnClickListener {
                if (value == "." && amountString.contains(".")) return@setOnClickListener
                amountString += value
                updateAmount()
            }
        }

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            if (amountString.isNotEmpty()) {
                amountString = amountString.dropLast(1)
                updateAmount()
            }
        }

        continueButton.setOnClickListener {
            if (amountString.isNotEmpty()) {
                val amount = amountString.toDoubleOrNull()
                if (amount != null) {
                    dbHelper.insertCashSetup(amount)
                    Toast.makeText(this, "Amount saved", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateAmount() {
        val display = if (amountString.isEmpty()) "0" else amountString
        amountText.text = "$display ZAR"
    }
}
