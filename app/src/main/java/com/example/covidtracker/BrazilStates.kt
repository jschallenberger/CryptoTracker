package com.example.covidtracker

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_brazil_states.*

class BrazilStates : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brazil_states)

        //statesRecView.setBackgroundColor(Color.BLACK)

        statesRecView.layoutManager = LinearLayoutManager(this)
        statesRecView.adapter = StatesAdapter()
    }
}
