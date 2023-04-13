package org.example.ada.setting

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_connect_push_dialog.*
import org.example.ada.R

class ConnectPushDialog(context: Context){

    val db = FirebaseFirestore.getInstance()
    lateinit var auth: FirebaseAuth
    var opponentUid = ""
    var opponentId = ""

    private val dialog = Dialog(context)
    private lateinit var onclickListener: OnDialogClickListener

    fun setOnclickListener(listener: OnDialogClickListener) {
        onclickListener = listener
    }

    interface OnDialogClickListener{
        fun onclicked(data: String, OpponentUserUid: String)
    }

    fun showDialog() {

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!.uid
        var ConnectMent = dialog.findViewById<TextView>(R.id.ConnectMent)

        val MySettingData = db.collection("UserId").document(currentUser)

        var wantConnect = ""

        MySettingData.get().addOnSuccessListener { result ->
            wantConnect = result.data?.get("wantconnect").toString()
            dialog.ConnectMent.setText("${wantConnect} 님이 연결하고 싶어합니다.")
        }

        db.collection("UserId").get().addOnSuccessListener { result ->
            for(document in result) {
                val searchUserId = document.data?.get("UserName").toString()
                val searchUid = document.data?.get("UserUid").toString()
                if(searchUserId == wantConnect) {
                    this.opponentId = searchUserId
                    this.opponentUid = searchUid
                }
            }
        }

        dialog.setContentView(R.layout.activity_connect_push_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)
        dialog.show()

        dialog.Yes.setOnClickListener{
            db.collection("UserId").get().addOnSuccessListener { result ->
                for(document in result) {
                    val searchUserId = document.data?.get("UserName").toString()
                    if(searchUserId == this.opponentId) {
                        onclickListener.onclicked(wantConnect,opponentUid)
                    }
                }
            }
            dialog.dismiss()
        }

        dialog.No.setOnClickListener{
            db.collection("UserId").get().addOnSuccessListener { result ->
                for(document in result) {
                    val searchUserId = document.data?.get("UserName").toString()
                    if(searchUserId == this.opponentId) {
                        onclickListener.onclicked("=", opponentUid)
                    }
                }
            }
            dialog.dismiss()
        }
    }
}