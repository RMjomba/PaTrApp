package com.reginaldateya.tiba

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


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
        val phoneNumber = etMobileNumber.text.toString()
        val email = etRegisterEmailAddress.text.toString()
        val password = etPassword.text.toString()

        when {

            TextUtils.isEmpty(fullName) -> Toast.makeText(this, "Full name is required!", Toast.LENGTH_SHORT).show()
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

                            saveUserInfo(fullName,phoneNumber,email, progressDialog)

                        }
                        else {

                            // If sign in fails, display a message to the user.
                            val message = task.exception!!.toString()
                            Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG)
                            auth.signOut()
                            progressDialog.dismiss()
                        }
                    }
            }

        }


    }

    private fun saveUserInfo(fullName: String, phoneNumber: String, email: String, progressDialog: ProgressDialog) {

        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserId
        userMap["fullName"] = fullName
        userMap["phoneNumber"] = phoneNumber
        userMap["email"] = email

        usersRef.child(currentUserId).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        baseContext, "Account has been created successfully.",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
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