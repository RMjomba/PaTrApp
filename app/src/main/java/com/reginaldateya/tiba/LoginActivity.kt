package com.reginaldateya.tiba

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginActivity : AppCompatActivity() {

    private lateinit var btnLogin : Button
    private lateinit var etEmailAddress : EditText
    private lateinit var etPassword : EditText
    private lateinit var btnRegister : Button
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        etPassword = findViewById(R.id.etPassword)
        etEmailAddress = findViewById(R.id.etEmailAddress)


        btnRegister = findViewById(R.id.btnRegister)
        btnRegister.setOnClickListener {
            onBackPressed()
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        btnLogin = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            doLogin()
        }

    }

    private fun doLogin() {
        if (etEmailAddress.text.toString().isEmpty()) {
            etEmailAddress.error = "Please enter email address"
            etEmailAddress.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(etEmailAddress.text.toString()).matches()) {
            etEmailAddress.error = "Please enter a valid email address"
            etEmailAddress.requestFocus()
            return
        }

        if (etPassword.text.toString().isEmpty()) {
            etPassword.error = "Please enter password"
            etPassword.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(etEmailAddress.text.toString(), etPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)

                } else {
                    Toast.makeText(baseContext, "Login failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                    auth.signOut()


                }
            }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)

    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            if (currentUser.isEmailVerified) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(baseContext, "Please verify your email address.",
                    Toast.LENGTH_LONG).show()
                auth.signOut()
            
            }
        }


    }
}

