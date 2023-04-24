package org.example.ada.setting

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
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
    var chatName = ""

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

        var wantConnect = ""

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
                chatName = result.data?.get("chatName").toString()
                opponentId = result.data?.get("connect").toString()

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
                        if (searchUserId == wantConnect) {
                            this.opponentId = searchUserId
                            this.opponentUid = searchUid


                            db.collection("UserContent").document(chatName).delete()
                            db.collection("UserChat").document(chatName).delete()
                            db.collection("UserDiary").document(chatName).delete()
                            db.collection("UserId").document(currentUser).update(updates)
                            db.collection("UserId").document(opponentUid).update(updates)
                            dialog.dismiss()
                        }
                    }
                }
            }
        }

        dialog.Cancel.setOnClickListener {
            dialog.dismiss()
        }
    }
}