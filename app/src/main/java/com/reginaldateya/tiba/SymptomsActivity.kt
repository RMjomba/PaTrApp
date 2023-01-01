package com.reginaldateya.tiba

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

class SymptomsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptoms)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Symptoms Screening"
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}