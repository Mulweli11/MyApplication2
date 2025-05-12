package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mainRegisterButton: Button
    private lateinit var mainLoginButton: Button
    private lateinit var mainCalculatorButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainRegisterButton = findViewById(R.id.mainRegisterButton)
        mainLoginButton = findViewById(R.id.mainLoginButton)
        mainCalculatorButton = findViewById(R.id.mainCalculatorButton)  // Initialize calculator button


        mainRegisterButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        mainLoginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }


        mainCalculatorButton.setOnClickListener {
            startActivity(Intent(this, CalculatorActivity::class.java))
        }
    }
}
