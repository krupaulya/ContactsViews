package com.example.contacts.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.contacts.data.Contact

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: List<Contact>)

    @Query("update contacts set phone=:phone")
    fun setAllPhones(phone: String)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("delete from contacts where id=:id")
    suspend fun deleteById(id: String)

    @Update
    suspend fun updateContact(contact: Contact)

    @Query("SELECT * FROM contacts ORDER BY id ASC")
    fun getAllContacts(): LiveData<List<Contact>>

    @Query("SELECT * from contacts where id = :id")
    fun getContact(id: String): Contact
}