package org.example.ada.setting

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.StateSet.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_first_setting_name_dialog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.ada.R
import org.example.ada.fcm.NotiModel
import org.example.ada.fcm.PushNotification
import org.example.ada.fcm.RetrofitInstance

class ConnectPersonDialog(context: Context) {

    val db = FirebaseFirestore.getInstance()
    lateinit var auth: FirebaseAuth

    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener2

    fun setOnClickListener(listener: ConnectPersonDialog.OnDialogClickListener2) {
        onClickListener = listener
    }

    fun showDialog(){

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser?.uid
        var MyName = ""

        dialog.setContentView(R.layout.activity_connect_person_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        val sortName = dialog.findViewById<EditText>(R.id.ConnectNickname)
        val notFound = dialog.findViewById<TextView>(R.id.Ment)

        db.collection("UserId").document("$currentUser").get()
            .addOnSuccessListener { data ->
                MyName = data.data?.get("UserName").toString()
            }

        dialog.Yes.setOnClickListener {
            db.collection("UserId").get()
                .addOnSuccessListener { result ->
                    for(document in result) {
                        val searchUserName = document.data?.get("UserName").toString()
                        val searchUserUid = document.data?.get("UserUid").toString()
                        if(searchUserName == sortName.text.toString()) {
                            if(sortName.text.toString() == MyName){
                                notFound.text = "자기 자신을 추가 할 수 없습니다."
                            } else {
                                //이름 바뀌는 것 이거를 알림 보내고 Yes/No 하는 걸로 바꿔야 할 듯
                                db.collection("UserId").document(currentUser.toString())
                                    .update("connect", "=")
                                    .addOnSuccessListener {
                                        onClickListener.onClicked("알림을 보냈습니다.")
                                        //connectedId 부분이 바뀌는 곳
                                        dialog.dismiss()
                                    }
                                db.collection("UserId").document(currentUser.toString()).update("connectresult", "No")

                                val connectdata = hashMapOf(
                                    "connectresult" to "Ing",
                                    "wantconnect" to MyName
                                )

                                db.collection("UserId").document(searchUserUid).update(connectdata as Map<String, Any>)
                                //푸시 알림 보내는 것
                                db.collection("UserToken").document(searchUserUid).get()
                                    .addOnSuccessListener { result ->
                                        val searchToken = result.data?.get("UserToken").toString()
                                        Log.e(TAG, "토큰: $searchToken")
                                        val notiModel = NotiModel("Ada", "${MyName}님 께서 연결하고 싶어합니다!")
                                        val pushModel = PushNotification(notiModel, searchToken)
                                        testPush(pushModel)
                                    }
                            }
                        } else {
                            notFound.text = "존재하지 않는 아이디입니다."
                        }
                    }
                }
        }

        dialog.No.setOnClickListener {
            dialog.dismiss()
        }
    }

    interface OnDialogClickListener2{
        fun onClicked(name: String)
    }

    //Push
    private fun testPush(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        RetrofitInstance.api.postNotification(notification)
    }

}