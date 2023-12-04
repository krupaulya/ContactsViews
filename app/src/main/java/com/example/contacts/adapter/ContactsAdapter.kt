package com.example.contacts.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.contacts.data.Contact
import com.example.contacts.R
import com.example.contacts.diffutil.ContactDiffUtilCallback
import java.util.Locale


class ContactsAdapter(
    private val context: Context,
    private val onClick: (Contact) -> Unit,
    private val onLongClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>(), Filterable {

    private var contacts: MutableList<Contact> = ArrayList()
    private var filteredContacts: MutableList<Contact> = ArrayList()

    class ViewHolder(
        view: View,
        onClick: (Int) -> Unit,
        onLongClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        val textView: TextView
        val imageView: ImageView

        init {
            textView = view.findViewById(R.id.contact_name)
            imageView = view.findViewById(R.id.contact_photo)
            itemView.setOnClickListener {
                val pos: Int = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onClick(pos)
                }
            }
            itemView.setOnLongClickListener {
                val pos: Int = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onLongClick(pos)
                    true
                } else {
                    false
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_item, parent, false)
        return ViewHolder(view, { onClick }, { onLongClick })
    }

    override fun getItemCount() = filteredContacts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = filteredContacts[position]
        holder.textView.text = data.name
        Glide
            .with(context)
            .load(data.photo)
            .into(holder.imageView)
        holder.itemView.setOnClickListener {
            onClick(data)
        }
        holder.itemView.setOnLongClickListener {
            onLongClick(data)
            true
        }
    }

    fun refreshUsers(updatedContacts: List<Contact>) {
        val diffResult =
            DiffUtil.calculateDiff(ContactDiffUtilCallback(filteredContacts, updatedContacts))
        contacts.clear()
        contacts.addAll(updatedContacts)
        filteredContacts.clear()
        filteredContacts.addAll(updatedContacts)
        diffResult.dispatchUpdatesTo(this)

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charSequenceString = constraint.toString()
                val filteredList: MutableList<Contact> = ArrayList()

                if (charSequenceString.isEmpty()) {
                    filteredList.addAll(contacts)
                } else {
                    for (contact in contacts) {
                        if (contact.name.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) {
                            filteredList.add(contact)
                        }
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                filteredContacts = results.values as MutableList<Contact>
                notifyDataSetChanged()
            }
        }
    }


}