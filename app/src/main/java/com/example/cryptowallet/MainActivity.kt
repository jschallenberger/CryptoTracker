package com.example.cryptowallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException




class MainActivity : AppCompatActivity() {
    lateinit var adapter: CryptosAdapter
    lateinit var recCarteira: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var db = AppDatabase.getDatabase(applicationContext).cryptoDAO()
        recCarteira = findViewById(R.id.recCarteira)
        recCarteira.layoutManager = LinearLayoutManager(recCarteira.context)
        var variablelouca : Int = 0
        //recCarteira.setHasFixedSize(true)

        fetchJson()

        searchCrypto.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        btnCarteira.setOnClickListener {
            if (variablelouca == 0){
                btnCarteira.text = "Mercado"
                CoroutineScope(IO).launch {
                    var cryptoList = ArrayList<Cryptos>()
                    val cryptoOrders = db.getCryptoALL()
                    for (x in cryptoOrders){
                        val crypto = Cryptos(x.cryptocode, x.name, x.price)
                        cryptoList.add(crypto)
                    }
                    withContext(Main){
                        recCarteira.adapter = CryptosAdapter(cryptoList)
                    }
                    variablelouca = 1
                }
            }else{
                btnCarteira.text = "Carteira"
                fetchJson()
                variablelouca = 0
            }
        }

    }

    fun fetchJson(){
        runOnUiThread{progressBar.visibility = View.VISIBLE}
        val url = "https://rest.coinapi.io/v1/assets"
        val request = Request.Builder().url(url).addHeader("X-CoinAPI-Key", "D5D790E1-EC5F-48BC-A638-220E53FC6854").build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body!!.string()
                val gson = GsonBuilder().create()
                val sType = object : TypeToken<ArrayList<Cryptos>>() { }.type
                var cryptos = gson.fromJson<ArrayList<Cryptos>>(body,sType)

                runOnUiThread {
                    adapter = CryptosAdapter(cryptos)
                    recCarteira.adapter = adapter
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Errorrrrrr")
            }
        })
    }

}


