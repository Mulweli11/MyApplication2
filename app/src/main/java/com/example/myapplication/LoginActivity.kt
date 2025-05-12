package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var goToRegister: TextView
    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginpage)

        usernameEditText = findViewById(R.id.editTextLoginUsername)
        passwordEditText = findViewById(R.id.editTextLoginPassword)
        loginButton = findViewById(R.id.loginSubmitButton)
        goToRegister = findViewById(R.id.loginRegisterRedirect)

        dbHelper = UserDatabaseHelper(this)

        loginButton.setOnClickListener {
            val inputUsername = usernameEditText.text.toString().trim()
            val inputPassword = passwordEditText.text.toString().trim()

            if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val isValidUser = dbHelper.isUserValid(inputUsername, inputPassword)

            if (isValidUser) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, CreateGoalActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        goToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
