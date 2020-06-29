package com.example.covidtracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recCarteira.layoutManager = LinearLayoutManager(this)
        btnAdicionar.setOnClickListener {
            val intent = Intent(this, BrazilStates::class.java)
            startActivity(intent)
        }

        fetchJson()
    }

    fun fetchJson(){
        val url = "https://rest.coinapi.io/v1/assets"
        val request = Request.Builder().url(url).addHeader("X-CoinAPI-Key", "D5D790E1-EC5F-48BC-A638-220E53FC6854").build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body!!.string()
                val gson = GsonBuilder().create()
                val sType = object : TypeToken<List<Cryptos>>() { }.type
                var cryptos = gson.fromJson<List<Cryptos>>(body,sType)
                cryptos = cryptos.sortedBy { it.asset_id }
                cryptos = cryptos.filter { it.asset_id.contains("BTC") }

                runOnUiThread {
                    recCarteira.adapter = StatesAdapter(cryptos)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Errorrrrrr")
            }
        })
    }

}


