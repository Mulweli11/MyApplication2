package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CalculatorActivity : AppCompatActivity() {

    private lateinit var amountText: TextView
    private lateinit var keypad: GridLayout
    private var currentInput = ""  // To store the current input
    private var operator: String? = null  // To store the current operator
    private var operand1: Double = 0.0  // To store the first operand
    private var operand2: Double = 0.0  // To store the second operand
    private var isOperand1 = true  // To determine whether we're working with the first or second operand

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        amountText = findViewById(R.id.amount)  // TextView where the amount is displayed
        keypad = findViewById(R.id.keypad)  // GridLayout that holds the keypad buttons

        // Set up all the buttons
        val buttons = listOf(
            findViewById<Button>(R.id.btn0),
            findViewById<Button>(R.id.btn1),
            findViewById<Button>(R.id.btn2),
            findViewById<Button>(R.id.btn3),
            findViewById<Button>(R.id.btn4),
            findViewById<Button>(R.id.btn5),
            findViewById<Button>(R.id.btn6),
            findViewById<Button>(R.id.btn7),
            findViewById<Button>(R.id.btn8),
            findViewById<Button>(R.id.btn9),
            findViewById<Button>(R.id.btnDot),
            findViewById<Button>(R.id.btnAc),
            findViewById<Button>(R.id.btnDel),
            findViewById<Button>(R.id.btnAdd),
            findViewById<Button>(R.id.btnSub),
            findViewById<Button>(R.id.btnMul),
            findViewById<Button>(R.id.btnDiv),
            findViewById<Button>(R.id.btnEqual)
        )

        // Set up the buttons' click listeners
        buttons.forEach { button ->
            button.setOnClickListener {
                onButtonClick(it)
            }
        }
    }

    private fun onButtonClick(view: View) {
        when (view.id) {
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot -> {
                currentInput += (view as Button).text.toString()
                amountText.text = currentInput
            }

            R.id.btnAc -> {
                currentInput = ""
                operand1 = 0.0
                operand2 = 0.0
                operator = null
                isOperand1 = true
                amountText.text = "0 ZAR"
            }

            R.id.btnDel -> {
                if (currentInput.isNotEmpty()) {
                    currentInput = currentInput.dropLast(1)
                    amountText.text = if (currentInput.isEmpty()) "0 ZAR" else currentInput
                }
            }

            R.id.btnAdd, R.id.btnSub, R.id.btnMul, R.id.btnDiv -> {
                if (currentInput.isNotEmpty()) {
                    operand1 = currentInput.toDouble()
                    operator = (view as Button).text.toString()
                    currentInput = ""
                    isOperand1 = false
                }
            }

            R.id.btnEqual -> {
                if (currentInput.isNotEmpty() && !isOperand1) {
                    operand2 = currentInput.toDouble()
                    val result = when (operator) {
                        "+" -> operand1 + operand2
                        "-" -> operand1 - operand2
                        "*" -> operand1 * operand2
                        "/" -> if (operand2 != 0.0) operand1 / operand2 else "Error"
                        else -> "Error"
                    }
                    amountText.text = if (result is Double) "R${"%,.2f".format(result)}" else result.toString()
                    currentInput = ""
                    isOperand1 = true
                }
            }
        }
    }
}
