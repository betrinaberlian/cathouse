package com.example.cathouse

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast

class TransaksiActivity<SharedPreferences> : AppCompatActivity() {
        private lateinit var btnPilihan : ImageButton
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)
        supportActionBar?.hide()
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        btnPilihan = findViewById(R.id.headerhomeTransaksi)
        btnPilihan.setOnClickListener {
            val sharedPref: android.content.SharedPreferences = this.getSharedPreferences(global.Pref_Name, MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            Toast.makeText(applicationContext, "Sukses Logout", Toast.LENGTH_SHORT).show()
        }
    }
}