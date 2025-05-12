package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddCategoryActivity : AppCompatActivity() {

    private lateinit var categoryNameEditText: EditText
    private lateinit var limitEditText: EditText
    private lateinit var createCategoryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_category)

        categoryNameEditText = findViewById(R.id.categoryNameEditText)
        limitEditText = findViewById(R.id.limitEditText)
        createCategoryButton = findViewById(R.id.createCategoryButton)

        createCategoryButton.setOnClickListener {
            val categoryName = categoryNameEditText.text.toString()
            val limit = limitEditText.text.toString().toDoubleOrNull() ?: 0.0

            if (categoryName.isNotEmpty() && limit > 0) {
                Toast.makeText(this, "Category created!", Toast.LENGTH_SHORT).show()

                // Navigate to HomeActivity
                val intent = Intent(this, HomeActivity::class.java)
                // Optional: pass data
                intent.putExtra("categoryName", categoryName)
                intent.putExtra("limit", limit)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
