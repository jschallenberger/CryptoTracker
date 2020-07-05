package com.example.cryptowallet

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.room.*

//-------------------DataBase Stuff-----------------------
//-------------------DataBase Entities-----------------------
@Entity(indices = [Index("name"), Index("cryptocode")])
data class CryptoBuy(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val cryptocode: String,
    val name: String,
    val price: Double,
    val quantity: Double)

//-------------------DataBase DAO-----------------------
@Dao
interface CryptoDAO{

    @Insert
    suspend fun insertCryptoBuy(cryptoBuy: CryptoBuy)

    @Update
    suspend fun updateCryptoBuy(cryptoBuy: CryptoBuy)

    @Query ("DELETE FROM CryptoBuy WHERE cryptocode == :cryptocode")
    suspend fun deleteCryptoBuy(cryptocode: String)

    @Query("SELECT * FROM  CryptoBuy WHERE cryptocode == :cryptocode")
    suspend fun getCryptoBuys(cryptocode: String): List<CryptoBuy>

}

//-------------------DataBase Creation-----------------------
@Database(entities = [CryptoBuy::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase(){
    abstract fun cryptoDAO() : CryptoDAO

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        //private val LOCK = Any()

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(context).also{ instance = it}
            }
        }

        private fun buildDatabase(context: Context): AppDatabase{
            return Room.databaseBuilder(context, AppDatabase::class.java, "CryptoDB").build()
        }
    }
}

