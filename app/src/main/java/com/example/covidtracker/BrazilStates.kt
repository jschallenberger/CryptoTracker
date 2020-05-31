package com.example.covidtracker

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_brazil_states.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class BrazilStates : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brazil_states)

        //statesRecView.setBackgroundColor(Color.BLACK)

        statesRecView.layoutManager = LinearLayoutManager(this)
        //statesRecView.adapter = StatesAdapter()

        fetchJson()
    }

    fun fetchJson(){
        val url = "https://covid19-brazil-api.now.sh/api/report/v1"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback{

            override fun onResponse(call: Call, response: Response) {
               val body = response.body!!.string()
                val gson = GsonBuilder().create()

                val dataCovid = gson.fromJson(body, DataCovid::class.java)
                dataCovid.data = dataCovid.data.sortedBy { it.state }
                runOnUiThread {
                    statesRecView.adapter= StatesAdapter(dataCovid)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Errorrrrrr")
            }
        })
    }

    class DataCovid(var data: List<States>)

    class States(val uid: Int, val uf: String, val state: String,
                 val cases: Int, val deaths: Int, val suspects: Int,
                 val refuses: Int)


}
