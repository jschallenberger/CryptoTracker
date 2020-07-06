package com.example.cryptowallet

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.detailcrypto.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.lang.Exception
import java.lang.IndexOutOfBoundsException
import java.time.LocalDate

class DetailCryptoCur : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailcrypto)
        var db = AppDatabase.getDatabase(applicationContext).cryptoDAO()
        recComprados.layoutManager = LinearLayoutManager(recComprados.context)
        val stringCryptoName = intent.getStringExtra("crypto")
        val stringCryptoCode = intent.getStringExtra("cryptocode")
        val stringCryptoPrice = intent.getStringExtra("price").toDouble()
        var totalG: Double = 0.0
        var totalQ: Double = 0.0
        crypCod.text = stringCryptoCode
        cryptoNome.text = stringCryptoName
        txtPrice.text = "%.2f".format(stringCryptoPrice) + " USD"


        CoroutineScope(Main).launch {
            recComprados.adapter =
                OrderAdapter(db.getCryptoBuys(stringCryptoCode).toArrayList(), db, this@DetailCryptoCur)
            totalG = retornamult(db.getCryptoBuys(stringCryptoCode))
            totalQ = retornasoma(db.getCryptoBuys(stringCryptoCode))
            txtPrecoMedio.text = if((retornamult(db.getCryptoBuys(stringCryptoCode))/ retornasoma(db.getCryptoBuys(stringCryptoCode))).isNaN()){
                " - "
            }else{
               "%.2f".format(retornamult(db.getCryptoBuys(stringCryptoCode))/ retornasoma(db.getCryptoBuys(stringCryptoCode))) + " USD"
            }
            if ((totalG/totalQ) < stringCryptoPrice){
                txtPrecoMedio.setTextColor(Color.parseColor("#00ff00"))
            }
        }


        btnBuy.setOnClickListener {
            try{
                val cryptoBuy = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CryptoBuy(
                        cryptocode = stringCryptoCode,
                        name = stringCryptoName,
                        price = stringCryptoPrice,
                        quantity = edtQuantity.text.toString().toDouble(),
                        data = LocalDate.now().toString()
                    )
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
                CoroutineScope(IO).launch {
                        db.insertCryptoBuy(cryptoBuy)
                        val listabuys = db.getCryptoBuys(stringCryptoCode)
                        withContext(Main) {
                            recComprados.adapter = OrderAdapter(listabuys.toArrayList(), db, this@DetailCryptoCur)
                            totalG = retornamult(listabuys)
                            totalQ = retornasoma(listabuys)

                            txtPrecoMedio.text = "%.2f".format(totalG/totalQ) + " USD"
                            if ((totalG/totalQ) < stringCryptoPrice){
                                txtPrecoMedio.setTextColor(Color.parseColor("#00ff00"))
                            }
                        }
                }
        }catch (e: Exception){
                Toast.makeText(this, "PREENCHER QUANTIDADE", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun updateTotal(t: ArrayList<CryptoBuy>){
        if((retornamult(t)/ retornasoma(t)).isNaN()){
            txtPrecoMedio.text = " - "
        }else{
            txtPrecoMedio.text = "%.2f".format(retornamult(t)/ retornasoma(t)) + " USD"
        }

    }

}

fun retornamult(lista: List<CryptoBuy>):Double{
    var total: Double = 0.0
   for (x in lista){
       total+= total + x.quantity * x.price
   }
    return total
}

fun retornasoma(lista: List<CryptoBuy>):Double{
    var total: Double = 0.0
    for (x in lista){
        total+= total + x.quantity
    }
    return total
}




