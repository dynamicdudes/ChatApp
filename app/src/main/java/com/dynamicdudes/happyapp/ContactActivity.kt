package com.dynamicdudes.happyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dynamicdudes.happyapp.Adapter.Adapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_contact.*


class ContactActivity : AppCompatActivity() {

    private val users = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Select contact"
        setContentView(R.layout.activity_contact)
        fetchAllData()

    }



    private fun fetchAllData(){
        val database = FirebaseDatabase.getInstance().getReference("/users")
        Log.d("MainActivity","database referenced")
        database.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.d("FailedActivity","Simply Failed")
            }
            override fun onDataChange(p0: DataSnapshot) {
                Log.d("MainActivity","Inside of onDataChange ... Going to fetch DataSnapshot")
                p0.children.forEach {
                    Log.d("MainActivity","Fetching data and going to Adapter")
                    val data = it.getValue(User::class.java)
                    users.add(User("", data!!.username,data.profileImageUrl))
                   // Log.d("MainActivity",user?.username.toString())
                }
                val adapter = Adapter(users)
                Log.d("MainActivity","Setting Adapter...")
                recycler_view_contact.adapter = adapter
            }
        })
    }

}










