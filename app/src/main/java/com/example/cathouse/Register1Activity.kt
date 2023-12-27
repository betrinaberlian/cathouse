package com.example.cathouse

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Register1Activity : AppCompatActivity() {
    private lateinit var btnRegist : Button
    private lateinit var emailTxt : EditText
    private lateinit var nameTxt : EditText
    private lateinit var addressTxt : EditText
    private lateinit var numberTxt : EditText
    private lateinit var jenisTxt : EditText
    private lateinit var passwordTxt : EditText
    private var urlSignUp: String = "https://nazara-nano.my.id/berlin/api/auth/signup.php"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register1)
        supportActionBar?.hide()
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        btnRegist = findViewById(R.id.buttonRegister)
        emailTxt = findViewById(R.id.editTextEmail)
        nameTxt = findViewById(R.id.editTextNama)
        addressTxt = findViewById(R.id.editTextAddress)
        numberTxt = findViewById(R.id.editTextNumber)
        jenisTxt = findViewById(R.id.editTextJenis)
        passwordTxt = findViewById(R.id.editTextPassword)

        btnRegist.setOnClickListener {
            val name: String = nameTxt.text.toString().trim()
            val email: String = emailTxt.text.toString().trim()
            val password: String = passwordTxt.text.toString().trim()
            val address: String = addressTxt.text.toString().trim()
            val phone: String = numberTxt.text.toString().trim()
            val jenis: String = jenisTxt.text.toString().trim()
            if (!(password.isEmpty() || email.isEmpty() || name.isEmpty() || address.isEmpty() || phone.isEmpty() || jenis.isEmpty())) {
                val stringRequest: StringRequest = object : StringRequest(
                    Request.Method.POST,
                    urlSignUp,
                    object : com.android.volley.Response.Listener<String?> {
                        override fun onResponse(response: String?) {
                            if (response != null) {
                                val jsonObject = JSONObject(response)
                                val success = jsonObject.getBoolean("success")
                                val message = jsonObject.getString("message")
                                if (success) {
                                    val sharedPref: SharedPreferences = this@Register1Activity.getSharedPreferences(global.Pref_Name, MODE_PRIVATE)
                                    val editor = sharedPref.edit()
                                    editor.putString("name", name)
                                    editor.putString("email", email)
                                    editor.putString("password", password)
                                    editor.putString("address", address)
                                    editor.putString("phone", phone)
                                    editor.putString("sex", jenis)
                                    editor.apply()
                                    Toast.makeText(applicationContext, "Terimakasih sudah mendaftar $name", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                                } else {
                                    Toast.makeText(applicationContext, "Gagal Daftar: $message", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(applicationContext, "Terjadi kesalahan saat daftar. Respon null.", Toast.LENGTH_SHORT).show()
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
                        return "application/x-www-form-urlencoded; charset=UTF-8"
                    }



                    @Throws(AuthFailureError::class)
                    override fun getBody(): ByteArray {
                        val params = HashMap<String, String>()
                        params["name"] = name
                        params["email"] = email
                        params["password"] = password
                        params["address"] = address
                        params["phone"] = phone
                        params["sex"] = jenis

                        val jsonParams = JSONObject(params as Map<*, *>?).toString()
                        Log.d("RequestParams", jsonParams)
                        return jsonParams.toByteArray(Charsets.UTF_8)
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
}