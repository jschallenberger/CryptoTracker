package com.example.cryptowallet

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.CoroutineScope


class Cryptos(val asset_id: String, val name: String, val price_usd: Double)


fun <T> List<T>.toArrayList(): ArrayList<T>{
    return ArrayList(this)
}