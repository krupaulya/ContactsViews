package com.example.contacts.ui

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.contacts.data.Contact
import com.example.contacts.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsFragment : Fragment() {

    private val contactViewModel: ContactViewModel by viewModels()
    private lateinit var contactId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contactId = it.getString("contact").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name: EditText = view.findViewById(R.id.detail_name)
        val photo: ImageView = view.findViewById(R.id.detail_photo)
        val age: EditText = view.findViewById(R.id.detail_age)
        val phone: EditText = view.findViewById(R.id.detail_phone)
        val company: EditText = view.findViewById(R.id.detail_company)
        val email: EditText = view.findViewById(R.id.detail_email)
        val button = view.findViewById<Button>(R.id.save_changes)
        CoroutineScope(Dispatchers.IO).launch {
            val contact = contactViewModel.getContactById(contactId)

            withContext(Dispatchers.Main) {
                name.text = contact.name.toEditable()
                age.text = contact.age.toString().toEditable()
                phone.text = contact.phone!!.toEditable()
                company.text = contact.company.toEditable()
                email.text = contact.email.toEditable()
                context?.let {
                    Glide.with(it).asBitmap().load(contact.photo).into(BitmapImageViewTarget(photo))
                }
            }
            withContext(Dispatchers.Main) {
                button.setOnClickListener {
                    val changedAge = age.text.toString().toInt()
                    val changedName = name.text.toString()
                    val changedEmail = email.text.toString()
                    val changedPhone = phone.text.toString()
                    val changedCompany = company.text.toString()

                    val changedContact = Contact(
                        changedAge,
                        changedCompany,
                        changedPhone,
                        changedEmail,
                        contact.gender,
                        contactId,
                        changedName,
                        contact.photo
                    )
                    contactViewModel.updateContact(changedContact)
                    findNavController(view).popBackStack()
                }
            }
        }
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController(view).popBackStack()
        }
        callback.isEnabled = true
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    companion object {
        @JvmStatic
        fun newInstance() =
            DetailsFragment().apply {
            }
    }
}