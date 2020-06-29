package com.example.cryptowallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.detailcrypto.*
import okhttp3.*
import java.io.IOException
import java.text.NumberFormat
import java.util.*

class DetailState : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailcrypto)
        val stringCryptoName = intent.getStringExtra("crypto")
        val stringCryptoCode = intent.getStringExtra("cryptocode")
        val stringCryptoPrice = intent.getStringExtra("price").toDouble()
        crypCod.text = stringCryptoCode
        cryptoNome.text = stringCryptoName
        txtPrice.text = "%.2f".format(stringCryptoPrice) + " USD"

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
