package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class CreateGoalActivity : AppCompatActivity() {

    private lateinit var descriptionEditText: EditText
    private lateinit var targetAmountEditText: EditText
    private lateinit var createGoalButton: Button
    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.monthlygoal)

        descriptionEditText = findViewById(R.id.descriptionEditText)
        targetAmountEditText = findViewById(R.id.targetAmountEditText)
        createGoalButton = findViewById(R.id.createGoalButton)

        dbHelper = UserDatabaseHelper(this)

        createGoalButton.setOnClickListener {
            val description = descriptionEditText.text.toString().trim()
            val targetAmount = targetAmountEditText.text.toString().toDoubleOrNull()

            if (description.isNotEmpty() && targetAmount != null && targetAmount > 0) {
                // Save the goal to the database
                dbHelper.insertGoal(description, targetAmount)

                Toast.makeText(this, "Goal Created!", Toast.LENGTH_SHORT).show()

                // Navigate to CashSetupActivity
                val intent = Intent(this, CashSetupActivity::class.java)
                intent.putExtra("goalDescription", description)
                intent.putExtra("targetAmount", targetAmount)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please fill out all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
