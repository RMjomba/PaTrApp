package com.reginaldateya.tiba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*


class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "User Profile"

        btnEditProfile.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, AccountSettingsActivity::class.java))
        }

        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val currentUserRef: DatabaseReference = usersRef.child(currentUserId)

        currentUserRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Retrieve the user's data from the database
                val fullName = dataSnapshot.child("fullName").getValue(String::class.java)
                val clinicName = dataSnapshot.child("clinicName").getValue(String::class.java)
                val district = dataSnapshot.child("district").getValue(String::class.java)
                val title = dataSnapshot.child("title").getValue(String::class.java)
                val phoneNumber = dataSnapshot.child("phoneNumber").getValue(String::class.java)
                val email = dataSnapshot.child("email").getValue(String::class.java)

                // Update the user's profile with the retrieved data
                full_name.text = fullName
                clinic_name.text = clinicName
                district_profile.text = district
                title_profile.text = title
                Phone_number.text = phoneNumber
                email_profile.text = email
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