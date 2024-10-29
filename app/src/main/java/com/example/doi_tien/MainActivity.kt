package com.example.doi_tien

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    private lateinit var sourceCurrencyAmount: EditText
    private lateinit var targetCurrencyAmount: EditText
    private lateinit var sourceCurrencySpinner: Spinner
    private lateinit var targetCurrencySpinner: Spinner

    // Simulated exchange rates
    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.9241,
        "JPY" to 110.0,
        "VND" to 23000.0,
        "GBP" to 0.79,
        "AUD" to 1.39,
        "CAD" to 1.28,
        "CHF" to 0.96,
        "CNY" to 6.66
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the views
        sourceCurrencyAmount = findViewById(R.id.sourceCurrencyAmount)
        targetCurrencyAmount = findViewById(R.id.targetCurrencyAmount)
        sourceCurrencySpinner = findViewById(R.id.sourceCurrencySpinner)
        targetCurrencySpinner = findViewById(R.id.targetCurrencySpinner)

        setupSpinners()
        setupListeners()
    }

    private fun setupSpinners() {
        val currencyOptions = exchangeRates.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sourceCurrencySpinner.adapter = adapter
        targetCurrencySpinner.adapter = adapter

        // Set default selections to avoid null selections
        sourceCurrencySpinner.setSelection(0)
        targetCurrencySpinner.setSelection(1)
    }

    private fun setupListeners() {
        // Listen for changes in the source amount to update conversion
        sourceCurrencyAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateConversion()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Spinner listeners to update conversion when currency selections change
        sourceCurrencySpinner.onItemSelectedListener = createSpinnerListener()
        targetCurrencySpinner.onItemSelectedListener = createSpinnerListener()
    }

    private fun createSpinnerListener() = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            updateConversion()
        }

        override fun onNothingSelected(parent: AdapterView<*>) {}
    }

    private fun updateConversion() {
        // Retrieve selected currencies
        val sourceCurrency = sourceCurrencySpinner.selectedItem?.toString() ?: return
        val targetCurrency = targetCurrencySpinner.selectedItem?.toString() ?: return

        // Parse and validate the source amount
        val sourceAmountText = sourceCurrencyAmount.text.toString()
        val sourceAmount = sourceAmountText.toDoubleOrNull()
        if (sourceAmount == null || sourceAmount <= 0) {
            targetCurrencyAmount.setText("") // Clear target amount if input is invalid
            return
        }

        // Calculate the converted amount
        val sourceRate = exchangeRates[sourceCurrency] ?: return
        val targetRate = exchangeRates[targetCurrency] ?: return
        val convertedAmount = sourceAmount * (targetRate / sourceRate)

        // Display the converted amount with two decimal precision
        targetCurrencyAmount.setText("%.2f".format(convertedAmount))
    }
}
