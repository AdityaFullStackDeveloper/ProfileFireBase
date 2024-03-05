package com.example.profilefirebase

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.profilefirebase.UserProfileModel.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity() {

    private lateinit var userListAdapter: ProfileUserAdapter
    private lateinit var userRecyclerView: RecyclerView

    private var id: String? = null
    private var childName: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        id = intent.getStringExtra("id")

        childName = intent.getStringExtra("childName")
        userRecyclerView = findViewById(R.id.userData_RecyclerView)

        getUserProfileData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getUserProfileData() {
        val dataBase = FirebaseFirestore.getInstance()
        val userProfileCollection = dataBase.collection("users")
        .get()
            .addOnSuccessListener { result ->
                 val userList = ArrayList<UserProfile>()
                for (document in result){
                    val userProfile = document.toObject<UserProfile>()
                    userList.add(userProfile)
                }
                userListAdapter = ProfileUserAdapter(this, userList)
                userRecyclerView.layoutManager = LinearLayoutManager(this)
                userRecyclerView.adapter = userListAdapter

                userListAdapter.notifyDataSetChanged()
                Toast.makeText(this, "user details added Success", Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener

            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                return@addOnFailureListener
            }
    }
}
