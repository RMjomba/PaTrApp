package com.reginaldateya.tiba

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_account_settings.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class AccountSettingsActivity : AppCompatActivity() {


    private lateinit var firebaseUser: FirebaseUser
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)
        supportActionBar?.hide()

        tvUpdatePic.setOnClickListener(View.OnClickListener {
            checkPermission()

        })

        pro_image_profile_frag.setOnClickListener(View.OnClickListener {
            checkPermission()

        })


        val radioGroup: RadioGroup = findViewById(R.id.radioGroup)



        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val auth: FirebaseAuth = FirebaseAuth.getInstance()





        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.male -> {
                    // Update the user's record in the database with the value for radio button 1
                    updateUserInfo("male")
                }
                R.id.female -> {
                    // Update the user's record in the database with the value for radio button 2
                    updateUserInfo("female")
                }
            }
        }



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

    private fun saveImageToFirebase() {

        val currentUser = FirebaseAuth.getInstance().currentUser
        val email: String = currentUser?.email.toString()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReferenceFromUrl("gs://tiba-ee3b4.appspot.com/")
        val df = SimpleDateFormat("ddMMyyHHmmss")
        val dataobj = Date()
        val imagePath = splitString(email)+ "." + df.format(dataobj) + ".jpg"
        val ImageRef = storageRef.child("image/"+imagePath)
        pro_image_profile_frag.isDrawingCacheEnabled = true
        pro_image_profile_frag.buildDrawingCache()
        val drawable = pro_image_profile_frag.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = ImageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()

        }.addOnSuccessListener { taskSnapshot ->

            var downLoadUrl = taskSnapshot.task!!.toString()
            myRef.child("User").child(currentUser!!.uid).child("email").setValue(currentUser!!.email)
            myRef.child("User").child(currentUser.uid).child("ProfileImage").setValue(downLoadUrl)

        }
    }

    private fun splitString(email: String): String {
        val split = email.split("@")
        return split[0]
    }

    val READIMAGE: Int = 253
    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    READIMAGE
                )
                return
            }

        }
        loadImage()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READIMAGE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImage()
                } else {
                    Toast.makeText(this, "Cannot access your images", Toast.LENGTH_LONG).show()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        }


    }

    val PICK_IMAGE_CODE = 123
    private fun loadImage() {

        var intent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, PICK_IMAGE_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_CODE && data != null && resultCode == RESULT_OK){
            val selectedImage: Uri? = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor =
                selectedImage?.let { contentResolver.query(it,filePathColumn, null, null, null) }
            cursor?.moveToFirst()
            val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
            val picturePath = columnIndex?.let { cursor?.getString(it) }
            cursor?.close()
            pro_image_profile_frag.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }
    }



    private fun updateUserInfo(s: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef = FirebaseDatabase.getInstance().reference.child("Users").child(currentUserId)

        val userMap = HashMap<String, Any>()
        userMap["gender"] = s

        userRef.updateChildren(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Gender updated successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    // Handle errors
                }
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
        val updatedPhoneNumber = etMobileNumber.text.toString()
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
                    saveImageToFirebase()

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