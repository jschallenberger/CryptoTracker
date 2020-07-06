package com.example.cryptowallet

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.detailcrypto.view.*
import kotlinx.android.synthetic.main.ordercard.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IndexOutOfBoundsException
import java.time.LocalDate

class OrderAdapter(var listCryptos: ArrayList<CryptoBuy>, val cryptoDAO: CryptoDAO, val context: Context): RecyclerView.Adapter<CustomViewHolderOrder>() {


    override fun getItemCount(): Int {
        return listCryptos.count()
    }

    public interface OnItemChangeListener{

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderOrder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.ordercard, parent, false)
        return CustomViewHolderOrder(cellForRow)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: CustomViewHolderOrder, position: Int) {
        holder?.view?.txtQtd?.text= "%.8f".format(listCryptos[position].quantity)
        holder?.view?.txtValor?.text = "%.2f".format(listCryptos[position].price) + " USD"
        val total ="%.2f".format(listCryptos[position].quantity * listCryptos[position].price) + " USD"
        val id = listCryptos[position].id
        holder?.view?.txtTotal.text = total.toString()
            holder?.view?.txtData.text = listCryptos[position].data
        holder?.view?.floatingActionButton.setOnClickListener {
            CoroutineScope(IO).launch {
                cryptoDAO.deleteCryptoBuy(id)
                withContext(Main){
                    listCryptos.removeIf { x -> x.id == id }
                    notifyDataSetChanged()
                    (context as DetailCryptoCur).updateTotal(listCryptos)
                    }
                }
            }
    }
}

class CustomViewHolderOrder(val view: View): RecyclerView.ViewHolder(view) {

}