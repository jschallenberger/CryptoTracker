package com.example.covidtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_brazil_states.*
import kotlinx.android.synthetic.main.statecard.*
import okhttp3.*
import java.io.IOException
import java.text.NumberFormat
import java.util.*

class DetailState : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statecard)
        val stringUF = intent.getStringExtra("UF")
        println(stringUF)
        fetchJson(stringUF)

    }

    fun fetchJson(endpoint: String){
        val url = "https://covid19-brazil-api.now.sh/api/report/v1/brazil/uf/$endpoint"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body!!.string()
                val gson = GsonBuilder().create()

                //val statesMod: States = gson.fromJson(body, States::class.java)
                runOnUiThread {
                    //UF.text = statesMod.uf
                   // NomeEstado.text = statesMod.state
                   // txtDeaths.text =NumberFormat.getIntegerInstance(Locale.GERMAN).format(statesMod.deaths)
                  //  txtCases.text =NumberFormat.getIntegerInstance(Locale.GERMAN).format(statesMod.cases)
                  //  txtRefuses.text =NumberFormat.getIntegerInstance(Locale.GERMAN).format(statesMod.refuses)
                   // txtSuspects.text =NumberFormat.getIntegerInstance(Locale.GERMAN).format(statesMod.suspects)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Errorrrrrr")
            }
        })
    }
}
