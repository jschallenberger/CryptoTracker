package com.example.cryptowallet

import android.content.Context
import androidx.room.*


class Cryptos(val asset_id: String, val name: String, val price_usd: Double)


//-------------------DataBase Stuff-----------------------
//-------------------DataBase Entities-----------------------
@Entity(indices = [Index("name"),Index("cryptocode")])
data class CryptoBuy(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val crypocode: String,
    val name: String,
    val price: Double,
    val quantity: Double)

//-------------------DataBase DAO-----------------------
@Dao
interface CryptoDAO{

    @Insert
    fun insertCryptoBuy(cryptoBuy: CryptoBuy)

    @Update
    fun updateCryptoBuy(cryptoBuy: CryptoBuy)

    @Delete
    fun deleteCryptoBuy(cryptoBuy: CryptoBuy)

    @Query("SELECT * FROM  CryptoBuy WHERE crypocode == :cryptocode")
    fun getCryptoBuys(cryptocode: String): List<CryptoBuy>

}

//-------------------DataBase Creation-----------------------
@Database(entities = [CryptoBuy::class], version = 1)
abstract class AppDatabase: RoomDatabase(){
    abstract fun cryptoDAO() : CryptoDAO

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "crypt.db")
            .build()
    }
}