package com.coldfier.peanuttest.repository.room

import androidx.room.*
import com.coldfier.peanuttest.repository.UserData

@Dao
interface AccountDao {

    @Insert
    suspend fun saveAccount(userData: UserData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAccount(userData: UserData)

    @Query("SELECT * FROM user_data ORDER BY ROWID ASC LIMIT 1")
    suspend fun getAccount(): UserData?

    @Query("DELETE FROM user_data")
    suspend fun deleteAccount()
}