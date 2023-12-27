package com.example.cathouse

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class PendaftaranActivity : AppCompatActivity() {
    private lateinit var btnDaftar : Button
    private lateinit var namaKucingTxt : EditText
    private lateinit var jenisKucingTxt: EditText
    private lateinit var jenisKelaminTxt : EditText
    private lateinit var jenisPelayananTxt : EditText
    private lateinit var tglPengantaranTxt : EditText
    private lateinit var tglPenjemputanTxt : EditText
    private lateinit var btnPilihan : ImageButton
    private var urlSignUp: String = "https://nazara-nano.my.id/berlin/api/orders/order.php"
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendaftaran)
        supportActionBar?.hide()
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        btnDaftar = findViewById<Button>(R.id.buttonTitipkanKucing)
        namaKucingTxt = findViewById(R.id.edJenisKucing)
        jenisKucingTxt = findViewById(R.id.edJenisKucing)
        jenisKelaminTxt = findViewById(R.id.edJenisKelamin)
        jenisPelayananTxt = findViewById(R.id.edJenisPelayan)
        tglPengantaranTxt = findViewById(R.id.edTanggalPengantar)
        tglPenjemputanTxt = findViewById(R.id.edTanggalPenjemputan)
        btnPilihan = findViewById(R.id.headerhomePendaftaran)
        btnPilihan.setOnClickListener {
            val sharedPref: SharedPreferences = this.getSharedPreferences(global.Pref_Name, MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            Toast.makeText(applicationContext, "Sukses Logout", Toast.LENGTH_SHORT).show()
        }
        btnDaftar.setOnClickListener {
            val name = namaKucingTxt.text.toString().trim()
            val jenisKucing = jenisKucingTxt.text.toString().trim()
            val jenisKelamin = jenisKelaminTxt.text.toString().trim()
            val jenisPelayanan = jenisPelayananTxt.text.toString().trim()
            val tglPengantaran = tglPengantaranTxt.text.toString().trim()
            val tglJemput = tglPenjemputanTxt.text.toString().trim()
            if (!(name.isEmpty() || jenisKucing.isEmpty() || jenisKelamin.isEmpty() || jenisPelayanan.isEmpty() || tglPengantaran.isEmpty() || tglJemput.isEmpty())) {
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
                                    val sharedPref: SharedPreferences = this@PendaftaranActivity.getSharedPreferences(global.Pref_Name, MODE_PRIVATE)
                                    val editor = sharedPref.edit()
                                    editor.putString("name", name)
                                    editor.putString("jenisKucing", jenisKucing)
                                    editor.putString("jenisKelamin", jenisKelamin)
                                    editor.putString("jenisPelayanan", jenisPelayanan)
                                    editor.putString("tglPengantaran", tglPengantaran)
                                    editor.putString("tglJemput", tglJemput)
                                    editor.apply()
                                    Toast.makeText(applicationContext, "Terimakasih sudah Order $name", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(applicationContext, TransaksiActivity::class.java))
                                } else {
                                    Toast.makeText(applicationContext, "Gagal order: $message", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(applicationContext, "Terjadi kesalahan saat order. Respon null.", Toast.LENGTH_SHORT).show()
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
                                    Toast.makeText(applicationContext, "Terjadi kesalahan saat order.", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                Toast.makeText(applicationContext, "Terjadi kesalahan saat order.", Toast.LENGTH_SHORT).show()
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
                        params["cat_breed"] = jenisKucing
                        params["genre"] = jenisKelamin
                        params["service"] = jenisPelayanan
                        params["delivery_date"] = tglPengantaran
                        params["pickup_date"] = tglJemput

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