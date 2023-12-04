package com.example.contacts.database

import androidx.lifecycle.LiveData
import com.example.contacts.data.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class Repository(private val dao: Dao) {

    val contacts: LiveData<List<Contact>> = dao.getAllContacts()

    suspend fun insertContact(contact: List<Contact>) {
        CoroutineScope(IO).launch {
            dao.insertContact(contact)
        }
    }

    suspend fun updateContact(contact: Contact) {
        CoroutineScope(IO).launch {
            dao.updateContact(contact)
        }
    }

    suspend fun deleteById(id: String) {
        CoroutineScope(IO).launch {
            dao.deleteById(id)
        }
    }

    fun getContact(id: String): Contact {
        return dao.getContact(id)
    }

    fun setAllPhones(phone: String) {
        CoroutineScope(IO).launch {
            dao.setAllPhones(phone)
        }
    }
}