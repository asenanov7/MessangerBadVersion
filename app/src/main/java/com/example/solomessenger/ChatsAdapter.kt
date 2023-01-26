package com.example.solomessenger

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class ChatsAdapter(context:Context): Adapter<ChatsAdapter.ChatsViewHolder>() {
    private var chatsAdapterList = ArrayList<User>()

    private val onlineDrawable = ContextCompat.getDrawable(context, android.R.drawable.presence_online )
    private val offlineDrawable = ContextCompat.getDrawable(context, android.R.drawable.presence_offline )

    fun setDataOnChatsAdapter(data:List<User>){
        data as ArrayList<User>
        chatsAdapterList = data
        notifyDataSetChanged()
    }

    class ChatsViewHolder(itemView: View): ViewHolder(itemView){
        val username:TextView = itemView.findViewById(R.id.textView_username)
        val status:ImageView = itemView.findViewById(R.id.imageView_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item,parent,false)
        return ChatsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        val user = chatsAdapterList[position]
        val username = "${user.name} ${user.surname}, ${user.age}"
        holder.username.text = username


        val background = when(user.status){
            true-> onlineDrawable
            false-> offlineDrawable
        }
        holder.status.setImageDrawable(background)


        holder.itemView.setOnClickListener { bridge.click(user) }

    }

    override fun getItemCount(): Int {
       return chatsAdapterList.size
    }

    interface OnClickListener{
        fun click(user:User)
    }
    lateinit var bridge:OnClickListener
}