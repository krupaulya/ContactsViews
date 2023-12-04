package com.example.contacts

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.contacts.data.Contact
import com.example.contacts.json.readJson
import com.example.contacts.ui.ContactViewModel

class MainActivity : AppCompatActivity() {

    private val contactViewModel: ContactViewModel by viewModels()
    private lateinit var contacts: List<Contact>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contacts = readJson(baseContext)
        contactViewModel.insertContact(contacts)
        contactViewModel.setAllPhones("+375291180549")
    }
}