package org.example.ada.Post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.example.ada.R

class PostAdapter(var context: Context, private val itemList: ArrayList<PostItem>): RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview!!){
        var imageIv: ImageView = itemview.findViewById(R.id.PostImage)
        var Userimage: ImageView = itemview.findViewById(R.id.UserImage)
        var article: TextView = itemview.findViewById(R.id.Posting)
        var UserId_up: TextView = itemview.findViewById(R.id.UserId_1)
        var UserId_down: TextView = itemview.findViewById(R.id.UserId_2)
        var LikeNumber: TextView = itemview.findViewById(R.id.LikeNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.post_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.article.text = itemList.get(position).Article
        holder.UserId_up.text = itemList.get(position).UserId
        holder.UserId_down.text = itemList.get(position).UserId
        holder.LikeNumber.text = itemList.get(position).LikeNumber
        if(itemList.get(position).UserImage != "") {
            Glide.with(context).load(itemList.get(position).UserImage).into(holder.Userimage)
        } else {
            Glide.with(context).load(R.drawable.ic_user_photo).into(holder.Userimage)
        }
        Glide.with(context).load(itemList.get(position).imageUrl).into(holder.imageIv)
    }
}