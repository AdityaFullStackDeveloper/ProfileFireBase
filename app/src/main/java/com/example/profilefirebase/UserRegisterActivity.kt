package com.example.profilefirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatEditText

class UserRegisterActivity : AppCompatActivity() {
    private lateinit var userRegisterName : AppCompatEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registeration)
    }
}