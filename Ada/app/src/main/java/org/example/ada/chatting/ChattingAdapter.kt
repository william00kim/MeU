package org.example.ada.chatting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.example.ada.R

class ChattingAdapter(var context: Context, var messageList:ArrayList<Message>): RecyclerView.Adapter<ChattingAdapter.ViewHolder>() {

    class ViewHolder(messageview: View): RecyclerView.ViewHolder(messageview!!){
        var Photo: ImageView = messageview.findViewById(R.id.Photo)
        var Text: TextView = messageview.findViewById(R.id.Text)
        var date: TextView = messageview.findViewById(R.id.date)
        var Photo2: ImageView = messageview.findViewById(R.id.Photo2)
        var Text2: TextView = messageview.findViewById(R.id.Text2)
        var date2: TextView = messageview.findViewById(R.id.date2)
        var MChat: View = messageview.findViewById(R.id.ChatLayout)
        var OChat: View = messageview.findViewById(R.id.OLayout)

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.chatting_recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(messageList.get(position).State == "Me"){
            holder.Text.text = messageList.get(position).Text
            holder.date.text = messageList.get(position).Date
            if(messageList.get(position).Image != "") {
                Glide.with(context).load(messageList.get(position).Image).into(holder.Photo)
            } else {
                Glide.with(context).load(R.drawable.ic_user_photo).into(holder.Photo)
            }
            holder.OChat.isVisible = false
        } else if(messageList.get(position).State == "Opponent"){
            holder.Text2.text = messageList.get(position).Text
            holder.date2.text = messageList.get(position).Date
            if(messageList.get(position).Image != "") {
                Glide.with(context).load(messageList.get(position).Image).into(holder.Photo2)
            } else {
                Glide.with(context).load(R.drawable.ic_user_photo).into(holder.Photo2)
            }
            holder.MChat.isVisible = false
        }

    }
}