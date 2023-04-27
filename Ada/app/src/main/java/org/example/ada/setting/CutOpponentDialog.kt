package org.example.ada.setting

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.WindowManager
import androidx.constraintlayout.widget.StateSet.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_cut_opponent_dialog.*
import org.example.ada.R

class CutOpponentDialog(context: Context) {

    val db = FirebaseFirestore.getInstance()
    lateinit var auth: FirebaseAuth
    var opponentUid = ""
    var opponentId = ""
    var MyId = ""

    private val dialog = Dialog(context)
    private lateinit var onclickListener: OnDialogClickListenerCutOpponent

    fun setOnclickListener(listener: OnDialogClickListenerCutOpponent) {
        onclickListener = listener
    }

    interface OnDialogClickListenerCutOpponent {
        fun onclicked(data: String, OpponentUserUid: String)
    }

    fun showDialog() {

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!.uid

        dialog.setContentView(R.layout.activity_cut_opponent_dialog)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)
        dialog.show()

        dialog.CutYes.setOnClickListener {
            db.collection("UserId").document(currentUser).get().addOnSuccessListener { result ->
                opponentId = result.data?.get("connect").toString()
                MyId = result.data?.get("UserName").toString()

                val updates = hashMapOf(
                    "connect" to FieldValue.delete(),
                    "chatName" to FieldValue.delete(),
                    "connectresult" to "No",
                    "wantconnect" to FieldValue.delete()
                )

                db.collection("UserId").get().addOnSuccessListener { result ->
                    for (document in result) {
                        val searchUserId = document.data?.get("UserName").toString()
                        val searchUid = document.data?.get("UserUid").toString()
                        val chatName = document.data?.get("chatName").toString()
                        if (searchUserId == opponentId) {
                            this.opponentUid = searchUid
                            Log.e(TAG, "우리 이름: $chatName")

                            //체팅기록 삭제
                            db.collection("UserChat").document(chatName).collection("Chat").get().addOnSuccessListener { res ->
                                for (doc in res){
                                    val number = doc.data?.get("turn").toString()
                                    db.collection("UserChat").document(chatName).collection("Chat").document(number).delete()
                                }
                            }
                            db.collection("UserDiary").document(chatName).collection(MyId).get().addOnSuccessListener { res ->
                                for(doc in res){
                                    val date = doc.data?.get("Date").toString()
                                    db.collection("UserDiary").document(chatName).collection(MyId).document(date).delete()
                                }
                            }
                            db.collection("UserDiary").document(chatName).collection(opponentId).get().addOnSuccessListener { res ->
                                for(doc in res){
                                    val date = doc.data?.get("Date").toString()
                                    db.collection("UserDiary").document(chatName).collection(opponentId).document(date).delete()
                                }
                            }
                            db.collection("User")
                            db.collection("UserId").document(currentUser).update(updates)
                            db.collection("UserId").document(opponentUid).update(updates)
                        }
                    }
                }
            }
            dialog.dismiss()
        }

        dialog.Cancel.setOnClickListener {
            dialog.dismiss()
        }
    }
}