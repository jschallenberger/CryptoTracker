package com.example.covidtracker

class DataCovid(var data: List<States>)

class States(val uid: Int, val uf: String, val state: String,
             val cases: Int, val deaths: Int, val suspects: Int,
             val refuses: Int)
class Countries(var data: List<Country>)

class Country(val country: String, val confirmed: Int)