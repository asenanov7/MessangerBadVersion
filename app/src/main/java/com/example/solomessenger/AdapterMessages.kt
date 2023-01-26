package com.example.solomessenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth

class AdapterMessages(private val currentUserId:String=""):Adapter<AdapterMessages.MessagesViewHolder>() {
    var adapterMessagesList = ArrayList<Message>()
    fun setDataOnAdapterMessageList(data:List<Message>){
        data as ArrayList<Message>
        adapterMessagesList = data
        notifyDataSetChanged()
    }


  class MessagesViewHolder(itemView: View):ViewHolder(itemView){
      val message:TextView = itemView.findViewById(R.id.textView_message)
  }

    override fun getItemViewType(position: Int): Int {
        val message = adapterMessagesList[position]
        return if (currentUserId==message.senderUserID) {
            R.layout.item_mymessage
        }else{
            R.layout.item_othermessage
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(viewType,parent,false)
        return MessagesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        val message = adapterMessagesList[position]
        holder.message.text = message.text
    }

    override fun getItemCount(): Int {
        return adapterMessagesList.size
    }


}