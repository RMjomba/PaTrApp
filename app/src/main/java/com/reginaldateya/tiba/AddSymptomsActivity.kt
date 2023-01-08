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
    private var fever: String = ""
    private var sweating: String = ""
    private var weightLoss: String = ""
    private var coughing: String = ""
    private var chestPain: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_symptoms)

        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        userRef = FirebaseDatabase.getInstance().reference.child("Symptoms").child(currentUserId)


        val radioGroup1: RadioGroup = findViewById(R.id.radioGroup)
        val radioGroup2: RadioGroup = findViewById(R.id.radioGroup1)
        val radioGroup3: RadioGroup = findViewById(R.id.radioGroup2)
        val radioGroup4: RadioGroup = findViewById(R.id.radioGroup3)
        val radioGroup5: RadioGroup = findViewById(R.id.radioGroup4)



        radioGroup1.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.male -> {
                    fever = "Fever: Yes"
                    updateUserInfo(fever, sweating, weightLoss, coughing, chestPain)
                }
                R.id.female -> {
                    fever = "Fever: No"
                    updateUserInfo(fever, sweating, weightLoss, coughing, chestPain)
                }
            }
        }

        radioGroup2.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.male1 -> {
                    sweating = "Sweating: Yes"
                    updateUserInfo(fever, sweating, weightLoss, coughing, chestPain)
                }
                R.id.female1 -> {
                    sweating = "Sweating: No"
                    updateUserInfo(fever, sweating, weightLoss, coughing, chestPain)
                }
            }
        }

        radioGroup3.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.male2 -> {
                    weightLoss = "Weight loss: Yes"
                    updateUserInfo(fever, sweating, weightLoss, coughing, chestPain)
                }
                R.id.female2 -> {
                    weightLoss = "Weight loss: No"
                    updateUserInfo(fever, sweating, weightLoss, coughing, chestPain)
                }
            }
        }

        radioGroup4.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.male3 -> {
                    coughing = "Coughing: Yes"
                    updateUserInfo(fever, sweating, weightLoss, coughing, chestPain)
                }
                R.id.female3 -> {
                    coughing = "Coughing: No"
                    updateUserInfo(fever, sweating, weightLoss, coughing, chestPain)
                }
            }
        }
        radioGroup5.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.male4 -> {
                    chestPain = "Chest pain: Yes"
                    updateUserInfo(fever, sweating, weightLoss, coughing, chestPain)
                }
                R.id.female4 -> {
                    chestPain = "Chest pain: No"
                    updateUserInfo(fever, sweating, weightLoss, coughing, chestPain)
                }

            }

        }








        btnPost.setOnClickListener {
            userInfo()
        }



    }

    private fun updateUserInfo(fever: String, sweating: String, weightLoss: String, coughing: String, chestPain: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef = FirebaseDatabase.getInstance().reference.child("Symptoms").child(currentUserId)

        val userMap = HashMap<String, Any>()
        userMap["fever"] = fever
        userMap["sweating"] = sweating
        userMap["weightLoss"] = weightLoss
        userMap["coughing"] = coughing
        userMap["chestPain"] = chestPain


        userRef.updateChildren(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Symptoms updated successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    // Handle errors
                }
            }


    }

    private fun userInfo() {
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Symptoms")
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