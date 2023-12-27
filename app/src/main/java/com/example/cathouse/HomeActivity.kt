package com.example.cathouse

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var btnhome : Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        try {
            val sharedPref: SharedPreferences = this.getSharedPreferences(global.Pref_Name, MODE_PRIVATE)
            val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
            Log.d("SplashScreen", "isLoggedIn saat aplikasi dibuka: $isLoggedIn")

            val intent = if (isLoggedIn) {
                Log.d("SplashScreen", "isLoggedIn saat aplikasi dibuka: $isLoggedIn")
                Intent(this, PilihanActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }

            startActivity(intent)
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("SplashScreen", "Terjadi kesalahan: ${e.message}")
        }



    }

}