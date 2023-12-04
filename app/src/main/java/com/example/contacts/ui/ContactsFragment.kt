package com.example.contacts.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.R
import com.example.contacts.adapter.ContactsAdapter


class ContactsFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var adapter: ContactsAdapter
    private val contactViewModel: ContactViewModel by viewModels()
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView = view.findViewById(R.id.search_bar)
        searchView.setOnQueryTextListener(this)
        recyclerView = view.findViewById(R.id.recycler)
        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        ResourcesCompat.getDrawable(resources, R.drawable.divider, null).let {
            if (it != null) {
                dividerItemDecoration.setDrawable(it)
            }
        }
        recyclerView.addItemDecoration(dividerItemDecoration)
        contactViewModel.allContacts.observe(viewLifecycleOwner) {
            adapter.refreshUsers(it)
        }
        adapter = ContactsAdapter(requireContext(), onClick = { model -> onClick(model.id) },
            onLongClick = { model -> onLongClick(model.id) })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun onClick(position: String) {
        val bundle: Bundle = bundleOf("contact" to position)
        Navigation.findNavController(requireView())
            .navigate(R.id.action_contactsFragment_to_detailsFragment, bundle)
    }

    private fun onLongClick(position: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Deleting the contact")
        builder.setMessage("Do you want to delete this contact")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes") { _, _ ->
            contactViewModel.deleteById(position)
        }
        builder.setNegativeButton("No") { _, _ -> }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ContactsFragment().apply {
            }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter.filter(newText)
        return false
    }
}
