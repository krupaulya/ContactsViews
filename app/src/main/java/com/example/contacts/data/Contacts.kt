package com.example.contacts.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize

@Entity(tableName = "contacts")
data class Contact(
    val age: Int,
    val company: String,
    val phone: String? = "+375291180549",
    val email: String,
    val gender: String,
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val photo: String
) : Parcelable