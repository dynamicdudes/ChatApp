package com.dynamicdudes.happyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        button.setOnClickListener {
            firebaseLoginOperation()
        }

        create_now_tv.setOnClickListener {
            val intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }

    }

    private fun firebaseLoginOperation(){
        val email = ed_email.text.toString()
        val password = ed_password.text.toString()

        if(email.isEmpty() or password.isEmpty())  toast("Fill are the above field.")

        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    //Perform Intent to Message Activity
                    val intent = Intent(this,LatestMessage::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    toast("Logged In")
                }
            }
            .addOnFailureListener {
                toast("${it.message}")
            }

    }

    private fun toast(msg : String){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
    }
}
