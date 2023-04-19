package org.example.ada.Post

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.StateSet.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_post_modify.*
import org.example.ada.R

class PostModify : AppCompatActivity() {

    lateinit var auth:FirebaseAuth
    val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_modify)

        val Head = intent.getStringExtra("HeadName").toString()

        var Image = ""
        var LikeCount = ""
        var UserId = ""
        var Uid = ""


        loadData()

        db.collection("UserPost").document(Head).get().addOnSuccessListener { res ->
            Image = res.data?.get("이미지").toString()
            LikeCount = res.data?.get("좋아요").toString()
            UserId = res.data?.get("이름").toString()
            Uid = res.data?.get("유저 아이디").toString()

            SortModify.setOnClickListener {

                var Article_head = HeadModify.editableText.toString()
                var Article_body = ArticleModify.editableText.toString()

                if(Article_head == "" || Article_body == "") {
                    Toast.makeText(this,"모든 수정사항을 입력해주세요",Toast.LENGTH_SHORT).show()
                } else {

                    val art = hashMapOf(
                        "게시글" to Article_body,
                        "제목" to Article_head,
                        "이름" to UserId,
                        "좋아요" to LikeCount,
                        "유저 아이디" to Uid,
                        "이미지" to Image
                    )

                    db.collection("UserPost").document(Article_head+"*").set(art)
                    db.collection("UserPost").document(Head).delete()

                    finish()
                }

            }

            CancelModify.setOnClickListener {
                finish()
            }
        }
    }

    private fun loadData(){

        val Head = intent.getStringExtra("HeadName").toString()
        Log.e(TAG, "${Head}")

        db.collection("UserPost").document(Head).get().addOnSuccessListener { res ->
            val head = res.data?.get("제목").toString()
            val body = res.data?.get("게시글").toString()
            HeadModify.setText(head)
            ArticleModify.setText(body)
        }
    }
}