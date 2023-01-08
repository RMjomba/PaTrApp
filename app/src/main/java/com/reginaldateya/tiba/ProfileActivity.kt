package com.reginaldateya.tiba

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "User Profile"

        val imageView: ImageView = findViewById(R.id.pro_image_profile_frag)
        val user = FirebaseAuth.getInstance().currentUser
        val profileImageRef = FirebaseStorage.getInstance().getReference("profileImages/${user?.uid}")

        profileImageRef.downloadUrl.addOnSuccessListener {
            // Load the image using Glide library
            Glide.with(this).load(it).into(imageView)
        }


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
                val gender = dataSnapshot.child("gender").getValue(String::class.java)
                val phoneNumber = dataSnapshot.child("phoneNumber").getValue(String::class.java)
                val email = dataSnapshot.child("email").getValue(String::class.java)


                // Update the user's profile with the retrieved data
                full_name.text = fullName
                clinic_name.text = clinicName
                district_profile.text = district
                title_profile.text = title
                gender_profile.text = gender
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