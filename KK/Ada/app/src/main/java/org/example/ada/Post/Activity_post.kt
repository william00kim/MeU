package org.example.ada.Post

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.StateSet.TAG
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_post.*
import org.example.ada.R
import org.example.ada.setting.SettingActivity

class activity_post : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    val storage = Firebase.storage
    val db = FirebaseFirestore.getInstance()

    var article = ""
    var photo_Uri = ""
    var UserId = ""
    var UserImage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        loadpage()

        val WriteIntent = Intent(this, WritingPost::class.java)
        val SettingIntent = Intent(this, SettingActivity::class.java)

        Writing.setOnClickListener{
            startActivity(WriteIntent)
        }

        btn_back2.setOnClickListener{
            finish()
        }

        setting.setOnClickListener{
            startActivity(SettingIntent)
        }
    }

    private fun loadpage() {
        auth = Firebase.auth
        val PostingContent = ArrayList<PostItem>()
        PostList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        PostList.setHasFixedSize(true)

        db.collection("UserPost").get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    for (document in result){
                        photo_Uri = document.data?.get("이미지").toString()
                        storage.reference.child("/ada/photo/${photo_Uri}").downloadUrl
                            .addOnSuccessListener { photo ->
                                Log.d(TAG, "사진 링크: $photo")
                                if (photo != null) {
                                    storage.reference.child("ada/profile/${document.data?.get("유저 아이디").toString()}.png").downloadUrl
                                        .addOnSuccessListener { photo ->
                                            UserImage = photo.toString()
                                        }
                                    UserId = document.data?.get("이름").toString()
                                    Log.d(TAG, "$UserId")
                                    article = document.data?.get("게시글").toString()
                                    PostingContent.add(PostItem(UserId, photo.toString(), article, "0",UserImage))
                                    PostList.adapter = PostAdapter(this, PostingContent)
                                } else {
                                    PostingContent.clear()
                                    val Adapter = PostAdapter(this, PostingContent)
                                    PostList.adapter = Adapter
                                }
                            }
                            .addOnFailureListener {
                                PostingContent.clear()
                                val Adapter = PostAdapter(this, PostingContent)
                                PostList.adapter = Adapter
                            }
                    }
                }
            }
    }
}