package com.example.contacts.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.contacts.data.Contact
import com.example.contacts.database.ContactDatabase
import com.example.contacts.database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: Repository
    val allContacts: LiveData<List<Contact>>

    init {
        val contactDao = ContactDatabase.getInstance(application).dao()
        repository = Repository(contactDao)
        allContacts = repository.contacts
    }

    suspend fun getContactById(id: String): Contact {
        return withContext(Dispatchers.IO) {
            repository.getContact(id)
        }
    }

    fun insertContact(contact: List<Contact>) {
        viewModelScope.launch {
            repository.insertContact(contact)
        }
    }

    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            repository.updateContact(contact)
        }
    }

    fun setAllPhones(phone: String) {
        viewModelScope.launch {
            repository.setAllPhones(phone)
        }
    }

    fun deleteById(id: String) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

}