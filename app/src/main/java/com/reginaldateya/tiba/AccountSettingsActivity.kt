package com.reginaldateya.tiba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.activity_account_settings.*

class AccountSettingsActivity : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)
        supportActionBar?.hide()

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val auth:FirebaseAuth = FirebaseAuth.getInstance()






        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this@AccountSettingsActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
            }
        close.setOnClickListener {
            val intent = Intent(this@AccountSettingsActivity, ProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        save_edited_info.setOnClickListener {
            userInfo()
        }
    }

    private fun userInfo() {
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val currentUserRef: DatabaseReference = usersRef.child(currentUserId)
        val updatedFullName = etFullName.text.toString()
        val updatedClinicName = etClinicName.text.toString()
        val updatedDistrict = etDistrict.text.toString()
        val updatedTitle = etTitle.text.toString()
        val updatedPhoneNumber = etPhoneNumber.text.toString()
        val updatedEmail = etEmailAddress.text.toString()

        val userMap = HashMap<String, Any>()
        userMap["fullName"] = updatedFullName
        userMap["clinicName"] = updatedClinicName
        userMap["district"] = updatedDistrict
        userMap["title"] = updatedTitle
        userMap["phoneNumber"] = updatedPhoneNumber
        userMap["email"] = updatedEmail

        currentUserRef.updateChildren(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@AccountSettingsActivity, ProfileActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    // Handle errors
                }
            }
    }
}