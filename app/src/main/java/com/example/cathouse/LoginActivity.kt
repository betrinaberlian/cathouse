package com.example.cathouse

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class LoginActivity() : AppCompatActivity(), Parcelable {

    private lateinit var editEmail: EditText
    private lateinit var editPsswd: EditText
    private lateinit var btnlgin: Button
    private lateinit var btnRegister : Button
    private var urlSignIn: String = "https://nazara-nano.my.id/berlin/api/auth/signin.php"
    constructor(parcel: Parcel) : this() {
        urlSignIn = parcel.readString().toString()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        editEmail = findViewById(R.id.editTextEmailLogin)
        editPsswd = findViewById(R.id.editTextPasswordLogin)
        btnlgin = findViewById(R.id.btnlogin)
        btnRegister = findViewById(R.id.btnRegister)
        btnRegister.setOnClickListener {
            startActivity(Intent(this, Register1Activity::class.java))
        }
        btnlgin.setOnClickListener {
            val password: String = editPsswd.text.toString().trim()
            val email: String = editEmail.text.toString().trim()

            if (!(password.isEmpty() || email.isEmpty())) {
                val stringRequest: StringRequest = object : StringRequest(
                    Request.Method.POST,
                    urlSignIn,
                    object : com.android.volley.Response.Listener<String?> {
                        override fun onResponse(response: String?) {
                                if (response != null) {
                                    val jsonObject = JSONObject(response)
                                    val success = jsonObject.getBoolean("success")
                                    val message = jsonObject.getString("message")
                                    if (success) {
                                        val sharedPref: SharedPreferences = this@LoginActivity.getSharedPreferences(global.Pref_Name, MODE_PRIVATE)
                                        val editor = sharedPref.edit()
                                        editor.putString("email", email)
                                        editor.putString("password", password)
                                        editor.putBoolean("isLoggedIn", true)
                                        editor.apply()
                                        Toast.makeText(applicationContext, "Selamat datang di CatHouse", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(applicationContext, PilihanActivity::class.java))
                                        finish()
                                    } else {
                                        // Jika ada pesan kesalahan dari server
                                        Log.e("SignInResponse", "Login gagal. Pesan dari server: $message")
                                        Toast.makeText(applicationContext, "Gagal login: $message", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(applicationContext, "Terjadi kesalahan saat login. Respon null.", Toast.LENGTH_SHORT).show()
                                }
                        }
                    },
                    object : com.android.volley.Response.ErrorListener {
                        override fun onErrorResponse(error: VolleyError) {
                            try {
                                // Check if the error has network response
                                if (error.networkResponse != null) {
                                    val errorObj = JSONObject(String(error.networkResponse.data))
                                    val errorMessage = errorObj.getString("message")
                                    Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(applicationContext, "Terjadi kesalahan saat login.", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                Toast.makeText(applicationContext, "Terjadi kesalahan saat login.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }) {
                    override fun getBodyContentType(): String {
                        return "application/json"
                    }


                    @Throws(AuthFailureError::class)
                    override fun getBody(): ByteArray {
                        val params = HashMap<String, String>()
                        params["email"] = email
                        params["password"] = password
                        return JSONObject(params as Map<*, *>?).toString().toByteArray(Charsets.UTF_8)
                    }
                    override fun getHeaders(): Map<String, String> {
                        val headers = HashMap<String, String>()
                        return headers
                    }
                }

                Volley.newRequestQueue(applicationContext).add(stringRequest)
            } else {
                Toast.makeText(applicationContext, "Kolom belum terisi", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(urlSignIn)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoginActivity> {
        override fun createFromParcel(parcel: Parcel): LoginActivity {
            return LoginActivity(parcel)
        }

        override fun newArray(size: Int): Array<LoginActivity?> {
            return arrayOfNulls(size)
        }
    }
}