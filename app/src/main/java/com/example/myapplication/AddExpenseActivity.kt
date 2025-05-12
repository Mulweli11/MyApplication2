package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddExpenseActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var photoImageView: ImageView
    private var imageUri: Uri? = null  // Store selected image URI
    private lateinit var userDatabaseHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addexpense)

        // Initialize the database helper
        userDatabaseHelper = UserDatabaseHelper(this)

        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val amountEditText = findViewById<EditText>(R.id.amountEditText)
        val limitEditText = findViewById<EditText>(R.id.limitEditText)
        val categorySpinner = findViewById<Spinner>(R.id.categorySpinner)
        val attachPhotoTextView = findViewById<TextView>(R.id.attachPhotoTextView)
        val addButton = findViewById<Button>(R.id.addButton)
        photoImageView = findViewById(R.id.photoImageView)

        // Set up category choices
        val categories = arrayOf("Food", "Transport", "Shopping", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        categorySpinner.adapter = adapter

        // Select photo from gallery
        attachPhotoTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        addButton.setOnClickListener {
            val description = descriptionEditText.text.toString().trim()
            val amount = amountEditText.text.toString().trim()
            val limit = limitEditText.text.toString().trim()
            val category = categorySpinner.selectedItem?.toString()

            if (description.isEmpty() || amount.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            } else {
                // Save expense to the database
                saveExpenseToDatabase(description, amount, limit, category ?: "Unknown", imageUri)
                Toast.makeText(this, "Expense added!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun saveExpenseToDatabase(
        description: String,
        amount: String,
        limit: String,
        category: String,
        imageUri: Uri?
    ) {
        val photoPath = imageUri?.toString() ?: "No photo attached"
        val amountDouble = amount.toDoubleOrNull() ?: 0.0
        val limitDouble = limit.toDoubleOrNull() ?: 0.0

        // Insert the expense into the database
        userDatabaseHelper.insertExpense(description, amountDouble, limitDouble, category, photoPath)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data?.data != null) {
            imageUri = data.data
            photoImageView.setImageURI(imageUri)
            photoImageView.visibility = View.VISIBLE
        }
    }
}
