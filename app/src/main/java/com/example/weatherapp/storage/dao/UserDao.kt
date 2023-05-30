package com.example.weatherapp.storage.dao

import androidx.room.*
import com.example.weatherapp.storage.tables.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM tbluser")
    fun getData(): List<UserEntity>

    @Query("SELECT * FROM tbluser WHERE id = :id")
    fun getDataById(id: Long): UserEntity

    @Query("SELECT * FROM tbluser WHERE email = :email")
    fun getDataByEmail(email: String): UserEntity?

    @Query("SELECT * FROM tbluser WHERE email = :email AND password =:password")
    fun getDataByEmailAndPassword(email: String,password : String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(lstData: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingleData(item: UserEntity)

    @Delete
    fun delete(item: UserEntity)

    @Query("DELETE FROM tbluser")
    fun deleteAllData()

    @Query("DELETE FROM tbluser WHERE id = :id")
    fun deleteById(id: Long)
}