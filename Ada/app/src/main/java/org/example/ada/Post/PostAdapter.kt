package org.example.ada.Post

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import org.example.ada.R

class PostAdapter(var context: Context, private val itemList: ArrayList<PostItem>): RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    lateinit var auth: FirebaseAuth

    class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview!!){
        var imageIv: ImageView = itemview.findViewById(R.id.PostImage)
        var Userimage: ImageView = itemview.findViewById(R.id.UserImage)
        var article: TextView = itemview.findViewById(R.id.Posting)
        var UserId_up: TextView = itemview.findViewById(R.id.UserId_1)
        var Head: TextView = itemview.findViewById(R.id.Head)
        var LikeNumber: TextView = itemview.findViewById(R.id.LikeNumber)
        var LikeButton: ImageView = itemview.findViewById(R.id.LikeButton)
        var PostSetting: ImageView = itemview.findViewById(R.id.PostSettingBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.post_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val db = FirebaseFirestore.getInstance()
        auth = Firebase.auth
        val curUser = auth.currentUser?.uid.toString()

        holder.article.text = itemList.get(position).Article
        holder.UserId_up.text = itemList.get(position).UserId
        holder.Head.text = itemList.get(position).Article_head
        holder.LikeNumber.text = itemList.get(position).LikeNumber

        holder.LikeButton.setOnClickListener {
            db.collection("UserPost").get().addOnSuccessListener { result ->
                for(document in result) {
                    val Post = holder.article.text
                    if(Post == document.data?.get("게시글")){
                        db.collection("UserPost").document(Post.toString()).get().addOnSuccessListener { res ->
                            var Like = document.data?.get("좋아요").toString()
                            var plusLike : Int = Like.toInt()
                            plusLike++
                            holder.LikeButton.setBackgroundResource(R.drawable.like)
                            db.collection("UserPost").document(Post.toString()).update("좋아요", plusLike.toString())
                        }
                    }
                }
            }
        }

        Glide.with(context).load(itemList.get(position).imageUrl).into(holder.imageIv)

        if(itemList.get(position).UserImage != "") {
            Glide.with(context).load(itemList.get(position).UserImage).into(holder.Userimage)
        } else {
            Glide.with(context).load(R.drawable.ic_user_photo).into(holder.Userimage)
        }

        holder.PostSetting.setOnClickListener {
            db.collection("UserId").document(curUser).get().addOnSuccessListener { res ->
                val UserName = res.data?.get("UserName").toString()
                val PostName = holder.Head.text.toString()
                if(holder.UserId_up.text == UserName){
                    val dialog = PostSettingMyDialog(context)
                    dialog.showDialog(PostName,context)
                    dialog.setOnclickListener(object:PostSettingMyDialog.onDialogClickListenserPostMe{
                        override fun onclicked(data: String) {
                            if(data == "Modify"){
                                val PostModifyk = Intent(context, PostModify::class.java)
                                PostModifyk.putExtra("HeadName",PostName)
                                ContextCompat.startActivity(context,PostModifyk,null)
                            }
                        }
                    })
                } else {
                    val dialog = PostSettingNotMeDialog(context)
                    dialog.showDialog(PostName, context)
                }
            }
        }
    }
}