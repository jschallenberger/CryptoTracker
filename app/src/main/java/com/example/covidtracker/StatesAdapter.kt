package com.example.covidtracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.statecard.view.*

class StatesAdapter: RecyclerView.Adapter<CustomViewHolder>() {

    val states = listOf<String>("Rio Grande do Sul", "Paraná", "Santa Catarina", "CrazzyWeed")

    override fun getItemCount(): Int {
        return states.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.statecard, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val state = states.get(position)
        holder?.view?.NomeEstado?.text=state
    }

}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view){

}