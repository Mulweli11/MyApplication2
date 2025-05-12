package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "user_data.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT,
                password TEXT
            );
        """.trimIndent())

        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS income (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                amount DOUBLE
            );
        """.trimIndent())

        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS goals (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                description TEXT,
                targetAmount DOUBLE
            );
        """.trimIndent())

        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS cash_setup (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                amount DOUBLE
            );
        """.trimIndent())

        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS expenses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                description TEXT,
                amount DOUBLE,
                limitAmount DOUBLE,
                category TEXT,
                photoUri TEXT
            );
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS income")
        db?.execSQL("DROP TABLE IF EXISTS goals")
        db?.execSQL("DROP TABLE IF EXISTS cash_setup")
        db?.execSQL("DROP TABLE IF EXISTS expenses")
        onCreate(db)
    }

    fun resetData() {
        val db = writableDatabase
        db.execSQL("DROP TABLE IF EXISTS income")
        db.execSQL("DROP TABLE IF EXISTS goals")
        db.execSQL("DROP TABLE IF EXISTS cash_setup")
        db.execSQL("DROP TABLE IF EXISTS expenses")
        onCreate(db)
        db.close()
    }

    fun insertUser(username: String, password: String) {
        val db = writableDatabase
        val stmt = db.compileStatement("INSERT INTO users (username, password) VALUES (?, ?)")
        stmt.bindString(1, username)
        stmt.bindString(2, password)
        stmt.executeInsert()
        db.close()
    }

    fun isUserValid(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", arrayOf(username, password))
        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return exists
    }

    fun insertGoal(description: String, targetAmount: Double) {
        val db = writableDatabase
        db.execSQL("DELETE FROM goals")
        val stmt = db.compileStatement("INSERT INTO goals (description, targetAmount) VALUES (?, ?)")
        stmt.bindString(1, description)
        stmt.bindDouble(2, targetAmount)
        stmt.executeInsert()
        db.close()
    }

    fun insertIncome(amount: Double) {
        val db = writableDatabase
        db.execSQL("DELETE FROM income")
        val stmt = db.compileStatement("INSERT INTO income (amount) VALUES (?)")
        stmt.bindDouble(1, amount)
        stmt.executeInsert()
        db.close()
    }

    fun insertCashSetup(amount: Double) {
        val db = writableDatabase
        db.execSQL("DELETE FROM cash_setup")
        val stmt = db.compileStatement("INSERT INTO cash_setup (amount) VALUES (?)")
        stmt.bindDouble(1, amount)
        stmt.executeInsert()
        db.close()
    }

    fun insertExpense(description: String, amount: Double, limit: Double, category: String, photoUri: String) {
        val db = writableDatabase
        val stmt = db.compileStatement("""
            INSERT INTO expenses (description, amount, limitAmount, category, photoUri) 
            VALUES (?, ?, ?, ?, ?)
        """.trimIndent())
        stmt.bindString(1, description)
        stmt.bindDouble(2, amount)
        stmt.bindDouble(3, limit)
        stmt.bindString(4, category)
        stmt.bindString(5, photoUri)
        stmt.executeInsert()
        db.close()
    }

    @SuppressLint("Range")
    fun getIncome(): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT amount FROM income ORDER BY id DESC LIMIT 1", null)
        var income = 0.0
        if (cursor.moveToFirst()) {
            val index = cursor.getColumnIndex("amount")
            if (index >= 0) {
                income = cursor.getDouble(index)
            }
        }
        cursor.close()
        db.close()
        return income
    }

    @SuppressLint("Range")
    fun getCashSetup(): Double {
        val db = readableDatabase
        var amount = 0.0
        val cursor = db.rawQuery("SELECT amount FROM cash_setup ORDER BY id DESC LIMIT 1", null)
        if (cursor.moveToFirst()) {
            val index = cursor.getColumnIndex("amount")
            if (index >= 0) {
                amount = cursor.getDouble(index)
            }
        }
        cursor.close()
        db.close()
        return amount
    }

    @SuppressLint("Range")
    fun getAllGoals(): List<String> {
        val goalsList = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT description, targetAmount FROM goals", null)
        if (cursor.moveToFirst()) {
            do {
                val description = cursor.getString(cursor.getColumnIndex("description"))
                val target = cursor.getDouble(cursor.getColumnIndex("targetAmount"))
                val goal = "Goal: $description\nTarget: R${"%,.2f".format(target)}"
                goalsList.add(goal)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return goalsList
    }

    @SuppressLint("Range")
    fun getTotalExpenses(): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM(amount) as total FROM expenses", null)
        var total = 0.0
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(cursor.getColumnIndex("total"))
        }
        cursor.close()
        db.close()
        return total
    }

    @SuppressLint("Range")
    fun getAllExpenses(): List<String> {
        val list = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM expenses", null)
        if (cursor.moveToFirst()) {
            do {
                val desc = cursor.getString(cursor.getColumnIndex("description"))
                val amount = cursor.getDouble(cursor.getColumnIndex("amount"))
                val limit = cursor.getDouble(cursor.getColumnIndex("limitAmount"))
                val category = cursor.getString(cursor.getColumnIndex("category"))
                list.add("Category: $category\nDescription: $desc\nAmount: R${"%,.2f".format(amount)}\nLimit: R${"%,.2f".format(limit)}")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    @SuppressLint("Range")
    fun getGoalProgress(): Double {
        val target = getGoalTargetAmount()
        val expenses = getTotalExpenses()
        return if (target > 0) {
            ((target - expenses) / target) * 100
        } else 0.0
    }

    @SuppressLint("Range")
    private fun getGoalTargetAmount(): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT targetAmount FROM goals ORDER BY id DESC LIMIT 1", null)
        var amount = 0.0
        if (cursor.moveToFirst()) {
            amount = cursor.getDouble(cursor.getColumnIndex("targetAmount"))
        }
        cursor.close()
        db.close()
        return amount
    }
}
