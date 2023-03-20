package com.kharin.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kharin.chat.databinding.UserMessageItemBinding


class MessageAdapter: ListAdapter<UserMessage, MessageAdapter.ItemHolder>(ItemComparator()) {
    class ItemHolder (private val binding: UserMessageItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind (userMessage: UserMessage) = with(binding){
            tvMessage.text = userMessage.message
            tvUsername.text = userMessage.name
        }
        companion object {
            fun create (parent: ViewGroup): ItemHolder{
                return ItemHolder(UserMessageItemBinding
                    .inflate(LayoutInflater.from(parent.context),parent,false))
            }
        }
    }
    class ItemComparator: DiffUtil.ItemCallback<UserMessage>(){
        override fun areItemsTheSame(oldItem: UserMessage, newItem: UserMessage): Boolean {
          return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UserMessage, newItem: UserMessage): Boolean {
                return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }
}