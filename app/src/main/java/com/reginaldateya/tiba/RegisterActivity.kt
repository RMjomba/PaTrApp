package com.reginaldateya.tiba

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var radioGroup: RadioGroup
    private lateinit var male: RadioButton
    private lateinit var female: RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        radioGroup = findViewById(R.id.radioGroup)
        male = findViewById(R.id.male)
        female = findViewById(R.id.female)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.male -> {
                    // Do something when option 1 is selected
                    male = findViewById(R.id.male)
                    val selectedOption = male.text
                }
                R.id.female -> {
                    // Do something when option 2 is selected
                    female = findViewById(R.id.female)
                    val selectedOption = female.text
                }
            }
        }




        tvLogin.setOnClickListener {
            onBackPressed()
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Register"

        btnNext.setOnClickListener {
            createAccount()
        }
    }

    private fun createAccount() {

            val fullName = etFullName.text.toString()
            val clinicName = etClinicName.text.toString()
            val district = etDistrict.text.toString()
            val title = etTitle.text.toString()
            val phoneNumber = etMobileNumber.text.toString()
            val email = etRegisterEmailAddress.text.toString()
            val password = etPassword.text.toString()
        val selectedOption = when (radioGroup.checkedRadioButtonId) {
            R.id.male -> male.text
            R.id.female -> female.text
            else -> null
        }



            when {

                TextUtils.isEmpty(fullName) -> Toast.makeText(this, "Full name is required!", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(clinicName) -> Toast.makeText(this, "Clinic name is required!", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(district) -> Toast.makeText(this, "District is required!", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(title) -> Toast.makeText(this, "Title is required!", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(phoneNumber) -> Toast.makeText(this, "Phone number is required!", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(email) -> Toast.makeText(this, "Email address is required!", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(password) -> Toast.makeText(this, "Password is required!", Toast.LENGTH_SHORT).show()


                else -> {
                    val progressDialog = ProgressDialog(this@RegisterActivity)
                    progressDialog.setTitle("SignUp")
                    progressDialog.setMessage("Please wait, this may take a while...")
                    progressDialog.setCanceledOnTouchOutside(false)
                    progressDialog.show()

                    val auth: FirebaseAuth = FirebaseAuth.getInstance()

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {task ->
                            if (task.isSuccessful)
                            {

                                val user = auth.currentUser
                                user?.sendEmailVerification()

                                saveUserInfo(fullName, clinicName, district, title, phoneNumber, email, selectedOption, progressDialog)

                            }
                            else {

                                // If sign in fails, display a message to the user.
                                val message = task.exception!!.toString()
                                Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                                auth.signOut()
                                progressDialog.dismiss()
                            }
                        }
                }

            }


        }


    private fun saveUserInfo(
        fullName: String,
        clinicName: String,
        district: String,
        title: String,
        phoneNumber: String,
        email: String,
        selectedOption: CharSequence?,
        progressDialog: ProgressDialog
    ) {

        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserId
        userMap["fullName"] = fullName
        userMap["clinicName"] = clinicName
        userMap["district"] = district
        userMap["title"] = title
        userMap["gender"] = selectedOption.toString()
        userMap["phoneNumber"] = phoneNumber
        userMap["email"] = email
        userMap["image"] = "https://firebasestorage.googleapis.com/v0/b/tiba-ee3b4.appspot.com/o/Default%20Images%2Fprofile.png?alt=media&token=aba6e11d-b6ac-4a34-bd36-6eed24efa7c3"

        usersRef.child(currentUserId).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        baseContext, "Account has been created successfully.",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()


                } else {
                    // If sign in fails, display a message to the user.
                    val message = task.exception!!.toString()
                    Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG)
                   FirebaseAuth.getInstance().signOut()
                    progressDialog.dismiss()
                }
            }
    }
}