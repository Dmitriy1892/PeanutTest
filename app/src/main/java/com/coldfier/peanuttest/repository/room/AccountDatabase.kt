package com.coldfier.peanuttest.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.coldfier.peanuttest.repository.UserData

@Database(entities = [UserData::class], version = 1, exportSchema = false)
abstract class AccountDatabase: RoomDatabase() {

    abstract fun accountDao(): AccountDao

    companion object {

        @Volatile
        private var INSTANCE: AccountDatabase? = null

        fun getInstance(context: Context): AccountDatabase {
            return synchronized(this) {
                INSTANCE ?: kotlin.run {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AccountDatabase::class.java,
                        "account_database"
                    ).build()
                    INSTANCE!!
                }
            }
        }
    }
}