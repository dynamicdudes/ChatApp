package com.dynamicdudes.happyapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*

class SignupActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var database : DatabaseReference
    private val PICK_UP_IMAGE_CODE = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        create_account.setOnClickListener {
            firebaseAuthentication()
        }

        image_profile.setOnClickListener {
            pickUpImageFromGallery()
        }



        login_activity_link.setOnClickListener {
            goToIntent()
        }

    }

    private fun pickUpImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, PICK_UP_IMAGE_CODE)
    }

    private var resultUri : Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_UP_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            //uri is from picking image from gallery
            val uri = data?.data
            CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(480, 480)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(this)
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //This uri is the new image after cropping.
            val result = CropImage.getActivityResult(data)
            resultUri = result.uri
            circle_image_view.setImageURI(resultUri)
            image_profile.alpha = 0f
        } else {
            Log.d(TAG, "Failed to load image...")
        }
    }

    private fun uploadDetailsToFirebase() {
        if (resultUri == null) {
            Log.d(TAG, "NULLLLLL")
            return
        }
        val filename = UUID.randomUUID().toString()
        val storageRef = FirebaseStorage.getInstance().getReference("images/$filename")
        storageRef.putFile(resultUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    Log.v(TAG, "Image Saved..")
                    writeDataInRealTimeDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.v(TAG, "Failed To Save Image..")
            }
    }

    private fun writeDataInRealTimeDatabase(profileImageUrl : String){
        val uuid = FirebaseAuth.getInstance().uid ?: ""
        val user = User(uuid,user_name_ed.text.toString(),profileImageUrl)
        database = Firebase.database.reference
        database.child("users").child(uuid).setValue(user)
            .addOnSuccessListener {
                toast("Stored..")
            }
            .addOnFailureListener {
                toast("Not Stored..")
            }
    }

    private fun goToIntent() {
        finish()
    }

    private fun firebaseAuthentication() {
        val username = user_name_ed.text.toString()
        val email = email_ed.text.toString()
        val password = password_ed.text.toString()
        val checkPassword = confirm_pass_ed.text.toString()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || checkPassword.isEmpty()) toast(
            "Fill all the above fields"
        )
        else if (password != checkPassword) toast("Passwords didn't match.")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    uploadDetailsToFirebase()
                    goToIntent()
                }
            }
            .addOnFailureListener {
                toast("${it.message}")
            }


    }


    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
