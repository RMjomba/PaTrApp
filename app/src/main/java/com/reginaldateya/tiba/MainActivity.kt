package com.reginaldateya.tiba

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


            // retrieve the user's email from the intent
            val intent = intent
            val email = intent.getStringExtra("email")

            // display the user's email on the screen
            val textView = findViewById<TextView>(R.id.tvEmail)
            textView.text = "Hi: $email"


        supportActionBar?.hide()


        cv_profile.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))

        }

        cvContact.setOnClickListener {
            startActivity(Intent(this@MainActivity, ContactActivity::class.java))

        }

        cvSymptoms.setOnClickListener {
            startActivity(Intent(this@MainActivity, SymptomsActivity::class.java))

        }

        cvLab.setOnClickListener {
            startActivity(Intent(this@MainActivity, LabActivity::class.java))

        }


    }

}

