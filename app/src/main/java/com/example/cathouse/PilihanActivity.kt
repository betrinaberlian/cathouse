package com.example.cathouse

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast

class PilihanActivity : AppCompatActivity() {
    private lateinit var btnRegistr: Button
    private lateinit var btnPilihan : ImageButton
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilihan)
        supportActionBar?.hide()
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        btnRegistr = findViewById(R.id.buttonRegisterPelayanan)
        btnPilihan = findViewById(R.id.headerhomePilihan)
        btnPilihan.setOnClickListener {
            val sharedPref: SharedPreferences = this.getSharedPreferences(global.Pref_Name, MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            Toast.makeText(applicationContext, "Sukses Logout", Toast.LENGTH_SHORT).show()
        }




        btnRegistr.setOnClickListener {
            startActivity(Intent(this, PendaftaranActivity::class.java))
        }
    }
}