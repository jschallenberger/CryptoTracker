package com.example.cryptowallet

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cryptocard.view.*
import java.util.*
import kotlin.collections.ArrayList


class CryptosAdapter(val listCryptos: ArrayList<Cryptos>): RecyclerView.Adapter<CustomViewHolder>(), Filterable {

    var cryptoFilterList = ArrayList<Cryptos>()

    init {
        cryptoFilterList = listCryptos
    }

    override fun getItemCount(): Int {
        return cryptoFilterList.count()
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    cryptoFilterList = listCryptos
                } else {
                    var resultList = ArrayList<Cryptos>()
                    for (row in listCryptos) {
                        if (row?.asset_id?.toLowerCase(Locale.ROOT)?.contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    cryptoFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = cryptoFilterList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                cryptoFilterList = (results?.values as ArrayList<Cryptos>)!!
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.cryptocard, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        //val state = dataCovid.data[position]
        holder?.view?.cryptoName?.text=cryptoFilterList[position].name
        holder?.view?.cryptoCode?.text = cryptoFilterList[position].asset_id
        holder?.view?.price.text = cryptoFilterList[position].price_usd.toString()
    }

}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view){
    init {
        view.setOnClickListener {
            val intent = Intent(view.context, DetailCryptoCur::class.java)
            intent.putExtra("cryptocode",it.cryptoCode.text)
            intent.putExtra("crypto",it.cryptoName.text)
            intent.putExtra("price",it.price.text)
            view.context.startActivity(intent)
        }
    }
}