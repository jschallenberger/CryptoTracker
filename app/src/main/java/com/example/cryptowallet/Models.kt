package com.example.covidtracker

class ListCryptos(var data: List<Cryptos>)

class Cryptos(val asset_id: String, val name: String, val price_usd: Double)
class Countries(var data: List<Country>)

class Country(val country: String, val confirmed: Int)