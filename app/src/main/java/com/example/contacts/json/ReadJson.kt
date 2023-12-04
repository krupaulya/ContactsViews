package com.example.contacts.json

import android.content.Context
import android.util.Log
import com.example.contacts.data.Contact
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

fun readJson(context: Context): List<Contact> {
    lateinit var jsonString: String
    try {
        jsonString = context.assets.open("contacts.json")
            .bufferedReader()
            .use { it.readText() }
    } catch (ioException: IOException) {
        Log.d("Exception", ioException.toString())
    }

    val listCountryType = object : TypeToken<List<Contact>>() {}.type
    return Gson().fromJson(jsonString, listCountryType)
}
