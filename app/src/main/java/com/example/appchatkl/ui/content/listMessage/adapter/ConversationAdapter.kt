package com.example.appchatkl.ui.content.listMessage.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appchatkl.data.Conversation
import com.example.appchatkl.databinding.ConversationLayoutAdapterBinding


class ConversationAdapter(val open :onClick)  : RecyclerView.Adapter<ConversationAdapter.MyViewHolder>(),
    Filterable {
   //  lateinit var list : ArrayList<Conversation>
    val TAG="ConversationAdapter"
    inner class MyViewHolder(val binding: ConversationLayoutAdapterBinding) :
        RecyclerView.ViewHolder(binding.root){

        }
    var countryFilterList : List<Conversation>
    get() = differ.currentList
    set(value) {
        differ.submitList(value)
    }

    fun get(){
        countryFilterList= listConversation
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
        val currentTvShow:Conversation = listConversation[position]
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
                    Log.d(TAG, "performFiltering12: "+listConversation.size)
                } else {
                    var resultList = ArrayList<Conversation>()
                    Log.d(TAG, "performFiltering123: "+countryFilterList.size)
                    for (row in countryFilterList) {
                        var count=0
                        row.listMessage.forEach{
                            if (it.lowercase().contains(constraint.toString().lowercase())){
                                count+=1
                                Log.d(TAG, "performFiltering: "+count)
                            }
                            Log.d(TAG, "performFiltering: "+it)
                        }
                        if (count>0) {
                            row.notification=count.toString()+"tin nhắn phù hợp"
                            row.isNotificat=true
                            row.isFind=true

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
                countryFilterList = results?.values as List<Conversation>
                notifyDataSetChanged()
            }
        }
    }

}
