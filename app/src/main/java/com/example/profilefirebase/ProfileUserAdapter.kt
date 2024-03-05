package com.example.profilefirebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.profilefirebase.UserProfileModel.UserProfile
import com.google.firebase.storage.FirebaseStorage

class ProfileUserAdapter(
    private val context: Context,
    private var userList: ArrayList<UserProfile>,
) :
    RecyclerView.Adapter<ProfileUserAdapter.UserProfileViewHolder>() {


    inner class UserProfileViewHolder(userProfileItem: View) : ViewHolder(userProfileItem) {
        val userName: TextView = userProfileItem.findViewById(R.id.userName_textView)
        val userEmail: TextView = userProfileItem.findViewById(R.id.userEmail_textView)
        val userImage: ImageView = userProfileItem.findViewById(R.id.user_modelImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.user_list_model, parent, false)
        return UserProfileViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserProfileViewHolder, position: Int) {
        holder.userName.text = userList[position].userProfileName
        holder.userEmail.text = userList[position].userProfileEmail

        val imageStorage = FirebaseStorage.getInstance().reference
        imageStorage.child(userList[position].childName!!)
            .downloadUrl
            .addOnSuccessListener {
                Glide.with(context)
                    .load(it)
                    .apply(RequestOptions().placeholder(R.drawable.image))
                    .into(holder.userImage)
                Toast.makeText(context, "image downloadUrl Success ", Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }.addOnFailureListener {
                Toast.makeText(context, "Image downloading failed", Toast.LENGTH_SHORT).show()
                return@addOnFailureListener
            }

//        val userVideo = FirebaseStorage.getInstance().reference
//        userVideo.child("${userList[position].childName}.mp4")
//            .downloadUrl
//            .addOnSuccessListener {videoUri ->
//                holder.userVideo.setVideoURI(videoUri)
//                Toast.makeText(context, "video upload successful", Toast.LENGTH_SHORT).show()
//            }.addOnFailureListener {
//                Toast.makeText(context, "video upload failed ${it.message}", Toast.LENGTH_SHORT).show()
//            }

        holder.userImage.setOnClickListener {
            val intent = Intent(context, UserUpdateActivity::class.java)
                .apply {
                    putExtra("id", userList[position].id)
                    putExtra("ImageChildName", userList[position].childName)
                }
            context.startActivity(intent)
            if (context is Activity){
                context.finish()
            }
        }
    }
}

