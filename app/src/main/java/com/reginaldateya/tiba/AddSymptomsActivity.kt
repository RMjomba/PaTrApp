package com.reginaldateya.tiba

import android.content.Intent
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_symptoms.*

class AddSymptomsActivity : AppCompatActivity() {
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var userRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_symptoms)

        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        userRef = FirebaseDatabase.getInstance().reference.child("Users").child(currentUserId)


        val radioGroup1: RadioGroup = findViewById(R.id.radioGroup)
        val radioGroup2: RadioGroup = findViewById(R.id.radioGroup1)
        val radioGroup3: RadioGroup = findViewById(R.id.radioGroup2)
        val radioGroup4: RadioGroup = findViewById(R.id.radioGroup3)
        val radioGroup5: RadioGroup = findViewById(R.id.radioGroup4)

        var fever = false
        var sweating = false
        var weightLoss = false
        var coughing = false
        var chestPain = false

        radioGroup1.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.male -> fever = true
                R.id.female -> fever = false
            }
            val message: String = if (fever) "You have selected 'Fever:Yes'" else "You have selected 'Fever:No'"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        radioGroup2.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.male1 -> sweating = true
                R.id.female1 -> sweating = false
            }
            val message: String = if (sweating) "You have selected 'Sweating:Yes'" else "You have selected 'Sweating:No'"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        radioGroup3.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.male2 -> weightLoss = true
                R.id.female2 -> weightLoss = false
            }
            val message: String = if (weightLoss) "You have selected 'Weight loss:Yes'" else "You have selected 'Weight loss:No'"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        radioGroup4.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.male3 -> coughing = true
                R.id.female3 -> coughing = false
            }
            val message: String = if (coughing) "You have selected 'Coughing:Yes'" else "You have selected 'Coughing:No'"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        radioGroup5.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.male4 -> chestPain = true
                R.id.female4 -> chestPain = false

            }

            val message: String = if (chestPain) "You have selected 'Chest pain:Yes'" else "You have selected 'Chest pain:No'"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }


        val symptoms = mutableMapOf<String, Any>()
        btnPost.setOnClickListener {
            symptoms["fever"] = fever
            symptoms["sweating"] = sweating
            symptoms["weightLoss"] = weightLoss
            symptoms["coughing"] = coughing
            symptoms["chestPain"] = chestPain

            userRef.updateChildren(symptoms).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Symptoms saved successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }



        btnPost.setOnClickListener {
            userInfo()
        }



    }

    private fun userInfo() {
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val currentUserRef: DatabaseReference = usersRef.child(currentUserId)
        val updatedCoughingWeeks = etCoughingWeek.text.toString()
        val updatedOriginationDate = etOriginationDate.text.toString()
        val updatedRevisionDate = etLastRevisionDate.text.toString()


        val userMap = HashMap<String, Any>()
        userMap["coughingWeeks"] = updatedCoughingWeeks
        userMap["OriginationDate"] = updatedOriginationDate
        userMap["revisionDate"] = updatedRevisionDate


        currentUserRef.updateChildren(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {


                    Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@AddSymptomsActivity, SymptomsActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    // Handle errors
                }
            }

    }


}