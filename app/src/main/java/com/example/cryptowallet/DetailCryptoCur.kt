package com.example.cryptowallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.detailcrypto.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.lang.Exception

class DetailCryptoCur : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailcrypto)
        var db = AppDatabase.getDatabase(applicationContext).cryptoDAO()
        val stringCryptoName = intent.getStringExtra("crypto")
        val stringCryptoCode = intent.getStringExtra("cryptocode")
        val stringCryptoPrice = intent.getStringExtra("price").toDouble()
        var total : Double = 0.0
        crypCod.text = stringCryptoCode
        cryptoNome.text = stringCryptoName
        txtPrice.text = "%.2f".format(stringCryptoPrice) + " USD"
        val cryptoBuy = CryptoBuy(cryptocode = stringCryptoCode, name = stringCryptoName, price = stringCryptoPrice, quantity = 2.0)

        btnBuy.setOnClickListener {
            CoroutineScope(IO).launch {
                db.deleteCryptoBuy(stringCryptoCode)
                db.insertCryptoBuy(cryptoBuy)
                delay(1000)

                val listabuys = db.getCryptoBuys(stringCryptoCode)
                total = 0.0
                for (x in listabuys){
                    total += x.price
                }
                withContext(Main){
                    txtTotal.text = total.toString()
                }
            }
        }
    }

}






