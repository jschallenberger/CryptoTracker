package com.example.covidtracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.text.NumberFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnEstados.setOnClickListener {
            val intent = Intent(this, BrazilStates::class.java)
            startActivity(intent)
        }

        fetchJson()
    }

    fun fetchJson(){
        val url = "https://covid19-brazil-api.now.sh/api/report/v1/countries"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body!!.string()
                val gson = GsonBuilder().create()
                var total:Int = 0
                val countries = gson.fromJson(body, Countries::class.java)
                //countries.data = countries.data.sortedBy { it.country }
                total = countries.data.sumBy { it.confirmed }

                runOnUiThread {
                    textView.text ="Casos confirmados no mundo: "+ NumberFormat.getIntegerInstance(Locale.GERMAN).format(total)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Errorrrrrr")
            }
        })
    }

}


