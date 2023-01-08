package com.reginaldateya.tiba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_symptoms.*

class SymptomsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptoms)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Symptoms Screening"

        btnAdd.setOnClickListener {
            startActivity(Intent(this@SymptomsActivity, AddSymptomsActivity::class.java))
        }


        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Symptoms")
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val currentUserRef: DatabaseReference = usersRef.child(currentUserId)

        currentUserRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Retrieve the user's data from the database
                val fever = dataSnapshot.child("fever").getValue(String::class.java)
                val sweating = dataSnapshot.child("sweating").getValue(String::class.java)
                val weightLoss = dataSnapshot.child("weightLoss").getValue(String::class.java)
                val coughingWeeks = dataSnapshot.child("coughingWeeks").getValue(String::class.java)
                val coughing = dataSnapshot.child("coughing").getValue(String::class.java)
                val chestPain = dataSnapshot.child("chestPain").getValue(String::class.java)
                val originationDate = dataSnapshot.child("originationDate").getValue(String::class.java)
                val revisionDate = dataSnapshot.child("revisionDate").getValue(String::class.java)

                // Update the user's profile with the retrieved data
                Fever.text = fever
                Sweating.text = sweating
                tvWeightLoss.text = weightLoss
                coughing_weeks.text = coughingWeeks
                fatigue.text = coughing
                coughing_blood.text = chestPain
                origination_date.text = originationDate
                revision__date.text = revisionDate
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })




    }




    override fun onOptionsItemSelected(item : MenuItem) : Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}