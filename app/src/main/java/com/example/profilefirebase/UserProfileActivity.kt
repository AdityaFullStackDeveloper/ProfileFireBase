package com.example.profilefirebase

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.net.toFile
import com.example.profilefirebase.UserProfileModel.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception
import java.util.UUID

class UserProfileActivity : AppCompatActivity() {

        private lateinit var userProfileImageView: ImageView
//    private lateinit var userProfileVideoView: VideoView
    private lateinit var userImageTakeButton: AppCompatButton

    private lateinit var userNameEditText: AppCompatEditText
    private lateinit var userEmailsEditText: AppCompatEditText
    private lateinit var userUploadDataButton: AppCompatButton

    private var uri: Uri? = null
    private lateinit var fireBaseStorage: FirebaseStorage
    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val imageChildName = "${UUID.randomUUID()}"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        userProfileImageView = findViewById(R.id.user_uploadImageView)
//        userProfileVideoView = findViewById(R.id.user_videoView)
        userImageTakeButton = findViewById(R.id.user_takeButton)

        userNameEditText = findViewById(R.id.enter_userName)
        userEmailsEditText = findViewById(R.id.enter_userDetails)
        userUploadDataButton = findViewById(R.id.upload_yourData)

//        userProfileVideoView.setMediaController(MediaController(this))
//        userProfileVideoView.start()

        fireBaseStorage = FirebaseStorage.getInstance()

        userUploadDataButton.setOnClickListener {
            getUserData()
//            uploadVideo()
        }

        userImageTakeButton.setOnClickListener {
            imageTakeFromStorage()
        }
    }

    private fun imageTakeFromStorage() {
        val intent = Intent()
        intent.type = "image/*"
        startActivityForResult(
            Intent.createChooser(intent, ""), 200
        )
//        val intent = Intent()
//        intent.setType("video/*")
//        intent.setAction(Intent.ACTION_GET_CONTENT)
//        startActivityForResult(intent, 150)
    }

    @SuppressLint("Recycle")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == RESULT_OK) {
            val imageUri = data?.data
                try {
                    uri = imageUri
                    userProfileImageView.setImageURI(imageUri)
                    uploadImage()
                }catch (e:Exception){
                    e.printStackTrace()
                }
        }

//        if (requestCode == 150 && resultCode == RESULT_OK && data != null) {
//            try {
//                val videoUri: Uri? = data.data
//                userProfileVideoView.setVideoURI(videoUri)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
    }

//    private fun getFiletype(videoUri: Uri?): String? {
//        val ref = contentResolver
//        val map = MimeTypeMap.getSingleton()
//        return map.getExtensionFromMimeType(ref.getType(videoUri!!))
//    }

    private fun getUserData() {
        uri?.let { uri ->
            val imageShare = FirebaseStorage.getInstance().reference
                .child("profileImage")
                .child(UUID.randomUUID().toString())

            imageShare.putFile(uri).addOnSuccessListener { taskSnapshot ->

                imageShare.downloadUrl.addOnSuccessListener { downloadUri ->

                    val id = UUID.randomUUID().toString()

                    val userProfile = UserProfile(
                        id = id,
                        userProfileName = userNameEditText.text.toString(),
                        userProfileEmail = userEmailsEditText.text.toString(),
                        childName = imageChildName
                    )

                    fireStore.collection("users")
                        .add(userProfile)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Profile data uploaded successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }

                        .addOnFailureListener { it ->
                            Log.e("FireStore", "Error uploading data to Firestore: $it")
                            Toast.makeText(
                                this,
                                "Error uploading data to Firestore: ${it.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Image upload failed: ${exception.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } ?: run {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImage() {
        if (uri == null) {
            Toast.makeText(this, "No image Selected", Toast.LENGTH_SHORT).show()
            return
        }

        val imageReference = FirebaseStorage.getInstance().reference
            .child(imageChildName)

        val uploadTask = imageReference.putFile(uri!!)
        uploadTask.addOnSuccessListener {
            Toast.makeText(this, "ImageUpload successful", Toast.LENGTH_SHORT).show()
            return@addOnSuccessListener
        }.addOnFailureListener {
            Toast.makeText(this, "ImageUpload failed", Toast.LENGTH_SHORT).show()
            return@addOnFailureListener
        }
    }

//    private fun uploadVideo() {
//        uri?.let { videoUri ->
//            val fileType = getFiletype(videoUri)
//            if (fileType != null) {
//                val reference = FirebaseStorage.getInstance().reference
//                    .child("Videos")
//                    .child("${UUID.randomUUID()}.$fileType")
//
//                val uploadTask = reference.putFile(videoUri)
//                uploadTask.addOnSuccessListener { taskSnapshot ->
//                    reference.downloadUrl.addOnSuccessListener { downloadUri ->
//                        val videoLink = downloadUri.toString()
//                        val videoMap = HashMap<String, String>()
//                        videoMap["videolink"] = videoLink
//                        val videoReference = FirebaseFirestore.getInstance().collection("Video")
//                        videoReference.document("${System.currentTimeMillis()}").set(videoMap)
//                            .addOnSuccessListener {
//                                Toast.makeText(
//                                    this,
//                                    "Video Uploaded Successfully",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                startActivity(Intent(this, MainActivity::class.java))
//                                finish()
//                            }.addOnFailureListener { exception ->
//                                Toast.makeText(
//                                    this,
//                                    "Failed to upload video: ${exception.message}",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                    }
//                }.addOnFailureListener { exception ->
//                    Toast.makeText(
//                        this,
//                        "Failed to upload video: ${exception.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            } else {
//                Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show()
//            }
//        } ?: run {
//            Toast.makeText(this, "No video selected", Toast.LENGTH_SHORT).show()
//        }
//    }
}



