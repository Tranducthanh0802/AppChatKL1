package com.example.appchatkl.ui.content.listMessage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appchatkl.data.Conversation
import com.example.appchatkl.databinding.ConversationLayoutAdapterBinding


class ConversationAdapter  : RecyclerView.Adapter<ConversationAdapter.MyViewHolder>() {

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
        val currentTvShow:Conversation = listConversation[position]
        holder.binding.apply {
            conversation=currentTvShow
        }
    }

    override fun getItemCount() = listConversation.size

}
