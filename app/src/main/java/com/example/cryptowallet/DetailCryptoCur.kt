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
            val dbCryptoList = db.getCryptoBuys(stringCryptoCode)
            recComprados.adapter =
                OrderAdapter(dbCryptoList.toArrayList(), db, this@DetailCryptoCur)
            totalG = retornamult(db.getCryptoBuys(stringCryptoCode))
            totalQ = retornasoma(db.getCryptoBuys(stringCryptoCode))
            if((retornamult(dbCryptoList)/ retornasoma(dbCryptoList)).isNaN()){
                txtPrecoMedio.text = " - "
            }else{
                val total = (retornasoma(dbCryptoList) * stringCryptoPrice) - retornamult(dbCryptoList)
                txtPrecoMedio.text = "%.2f".format(retornamult(dbCryptoList)/ retornasoma(dbCryptoList)) + " USD"
                txtTotalCarteira.text = "Total em Carteira: %.2f".format(retornamult(dbCryptoList)) + " USD ($ %.2f".format(total) + " )"
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

                           updateTotal(listabuys.toArrayList())
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
            txtTotalCarteira.text = "Total em Carteira: %.2f".format(retornamult(t)) + " USD"
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




