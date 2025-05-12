package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.lzyzsd.circleprogress.DonutProgress

class HomeActivity : AppCompatActivity() {

    private lateinit var incomeText: TextView
    private lateinit var availableText: TextView
    private lateinit var spentText: TextView
    private lateinit var donutProgress: DonutProgress
    private lateinit var calculatorButton: Button
    private lateinit var addExpensesButton: Button
    private lateinit var addCategoryButton: Button
    private lateinit var goalsTextView: TextView
    private lateinit var goalProgressText: TextView
    private lateinit var goalProgressBar: ProgressBar
    private lateinit var greetingText: TextView  // Add greeting TextView

    private lateinit var dbHelper: UserDatabaseHelper

    @SuppressLint("MissingInflatedId", "CutPasteId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Bind UI components
        incomeText = findViewById(R.id.incomeText)
        availableText = findViewById(R.id.availableText)
        spentText = findViewById(R.id.spentText)
        donutProgress = findViewById(R.id.circleProgress)
        calculatorButton = findViewById(R.id.mainCalculatorButton)
        addExpensesButton = findViewById(R.id.addExpensesButton)
        addCategoryButton = findViewById(R.id.addCategoryButton)
        goalsTextView = findViewById(R.id.goalsTextView)
        goalProgressText = findViewById(R.id.goalProgressText)
        goalProgressBar = findViewById(R.id.goalProgressBar)
        greetingText = findViewById(R.id.greeting) // Bind greeting TextView

        dbHelper = UserDatabaseHelper(this)

        // Fetch username from SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "Guest") ?: "Mulweli"

        // Set the greeting text with the username
        greetingText.text = "Welcome $username!"

        // Fetch income from the income table
        val income = dbHelper.getIncome()
        if (income == 0.0) {
            Toast.makeText(this, "No amount set. Please set an amount first.", Toast.LENGTH_SHORT).show()
            return
        }

        // Display income as balance as well
        incomeText.text = "R${"%,.2f".format(income)}" // Update income display to reflect balance

        // Example calculation for expenses and available balance will be finished i part 3
        val spent = income * 0.2 // Assuming 20% is spent
        val available = income - spent
        val percent = if (income > 0) (spent / income * 100).toInt() else 0

        // Update UI with financial info
        availableText.text = "AVAILABLE\nR${"%,.2f".format(available)}"
        spentText.text = "SPENT\nR${"%,.2f".format(spent)}"
        donutProgress.progress = percent.toFloat()
        donutProgress.text = "$percent%"

        goalProgressText.text = "You're $percent% towards your goal!"
        goalProgressBar.progress = percent

        // Fetch and display all goals
        val goals = dbHelper.getAllGoals()
        goalsTextView.text = if (goals.isNotEmpty()) {
            goals.joinToString("\n\n")
        } else {
            "No goals set yet."
        }

        // Navigation button listeners
        calculatorButton.setOnClickListener {
            startActivity(Intent(this, CalculatorActivity::class.java))
        }

        addExpensesButton.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        addCategoryButton.setOnClickListener {
            startActivity(Intent(this, AddCategoryActivity::class.java))
        }
    }
}
