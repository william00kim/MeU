package org.example.ada.chatting

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.StateSet.TAG
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chatting_list.*
import kotlinx.android.synthetic.main.activity_post.*
import org.example.ada.R
import java.text.SimpleDateFormat

class ChattingList : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    val db = FirebaseFirestore.getInstance()
    var opponent = ""

    var MessageNumber = 0

    val CurrentTime: Long = System.currentTimeMillis()
    val time = SimpleDateFormat("HH:mm:ss")
    val date = SimpleDateFormat("yyyy-MM-dd")
    val CurrentTimeSet = time.format(CurrentTime)
    val today = date.format(CurrentTime)

    var MyId = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting_list)
        val ChattingContent = ArrayList<Message>()

        ChattingList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        ChattingList.setHasFixedSize(true)
        auth = Firebase.auth

        val currentUser = auth.currentUser!!.uid

        db.collection("UserId").document(currentUser).get().addOnSuccessListener { result ->
            opponent = result.get("connect").toString()
            MyId = result.get("UserName").toString()
            val ChatName = result.get("chatName").toString()

            db.collection("UserChat").document(ChatName).collection("Chat")
                .addSnapshotListener { result, e ->
                    if (result != null) {
                        for (doc in result.documentChanges) {
                            Log.d(TAG, "변화")
                            if (doc.document["ChattingLog"].toString() == MyId) {
                                ChattingContent.add(
                                    Message(
                                        doc.document["turn"].toString().toInt(),
                                        "Me",
                                        doc.document["ChattingLog"].toString(),
                                        "",
                                        doc.document["Mychat"].toString(),
                                        doc.document["time"].toString()
                                    )
                                )
                            } else {
                                ChattingContent.add(
                                    Message(
                                        doc.document["turn"].toString().toInt(),
                                        "Opponent",
                                        doc.document["ChattingLog"].toString(),
                                        "",
                                        doc.document["Mychat"].toString(),
                                        doc.document["time"].toString()
                                    )
                                )
                            }
                            MessageNumber++
                            ChattingContent.sortBy(Message::turn)
                            for (i in 1..MessageNumber) {
                                ChattingList.adapter = ChattingAdapter(this, ChattingContent)
                            }
                            ChattingList.scrollToPosition(result.size())
                        }
                    }
                }

            Btn_send.setOnClickListener {
                Log.e(TAG, "$MessageNumber")
                db.collection("UserId").document(currentUser).get().addOnSuccessListener { res ->
                    val chatName = res.data?.get("chatName").toString()
                    if (ChattingText.text.toString() == "" || ChattingText.text == null) {
                        Toast.makeText(this, "메세지를 입력하세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        db.collection("UserChat").document(chatName).collection("Chat").document(MessageNumber.toString()).set(
                            hashMapOf(
                                "OpponentId" to opponent,
                                "ChattingLog" to MyId,
                                "Image" to "",
                                "Mychat" to ChattingText.text.toString(),
                                "time" to today + " " + CurrentTimeSet,
                                "turn" to MessageNumber
                            )
                        )
                        ChattingText.setText("")
                    }
                }
            }
        }

        Btn_back.setOnClickListener {
            finish()
        }

    }
}