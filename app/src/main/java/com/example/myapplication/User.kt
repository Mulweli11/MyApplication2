// : This is the user model for SQLite
package com.example.myapplication

data class User(
    val id: Int = 0,              // Auto-incremented ID (optional for insert)
    val username: String,         // User's username
    val password: String          // User's password
)
