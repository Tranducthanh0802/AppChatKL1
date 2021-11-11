package com.example.appchatkl.ui.content.Find.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appchatkl.data.Conversation
import com.example.appchatkl.databinding.ConversationLayoutAdapterBinding
import com.example.appchatkl.ui.content.listMessage.adapter.onClick


class FindMessageAdapter (val open : onClick)  : RecyclerView.Adapter<FindMessageAdapter.MyViewHolder>(),
    Filterable {
    var countryFilterList = ArrayList<Conversation>()
    inner class MyViewHolder(val binding: ConversationLayoutAdapterBinding) :
        RecyclerView.ViewHolder(binding.root){

    }

    private val diffCallback = object : DiffUtil.ItemCallback<Conversation>() {
        override fun areItemsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
            return oldItem.name.equals(newItem.name)
        }

        override fun areContentsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
            return newItem.equals(oldItem)
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var listConversation: List<Conversation>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ConversationLayoutAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTvShow: Conversation = listConversation[position]
        holder.binding.apply {
            conversation=currentTvShow
        }
        holder.binding.Linear.setOnClickListener{
            open.openChat(currentTvShow.message.id)
        }
    }

    override fun getItemCount() = listConversation.size
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    countryFilterList = listConversation as ArrayList<Conversation>
                } else {
                    val resultList = ArrayList<Conversation>()
                    for (row in countryFilterList) {
                        var count=0
                        row.listMessage.forEach{
                            if (it.lowercase().contains(constraint.toString().lowercase())){
                               count+=1
                            }
                        }
                        if (count>0) {
                            row.notification=count.toString()+"tin nhắn phù hợp"
                            resultList.add(row)
                        }
                    }
                    countryFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = countryFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countryFilterList = results?.values as ArrayList<Conversation>
                notifyDataSetChanged()
            }
        }
    }

}
