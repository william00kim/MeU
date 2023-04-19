package org.example.ada.Post

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.StateSet.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_report_reason.*
import org.example.ada.R

class ReportReason(context: Context) {
    val db = FirebaseFirestore.getInstance()
    lateinit var auth: FirebaseAuth

    private val dialog = Dialog(context)
    private lateinit var onclickListener: ReportReason.onDialogClickListenserReportReason

    fun setOnclickListener(listener: onDialogClickListenserReportReason){
        onclickListener = listener
    }

    interface onDialogClickListenserReportReason{
        fun onclicked(data: String)
    }

    fun showDialog(data: String, diamond: Context){
        auth = FirebaseAuth.getInstance()
        val CurUser = auth.currentUser?.uid.toString()

        dialog.setContentView(R.layout.activity_report_reason)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()

        var Reason = ""

        dialog.ReasonGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.ItsMyPhoto -> Reason = "사진 무단 도용"
                R.id.Its19Post -> Reason = "선정적인 이유"
                R.id.ItsBadAd -> Reason = "부적절한 광고 포함"
                R.id.ETC -> Reason = dialog.ETCReason.text.toString()
            }
        }

        dialog.ReasonSort.setOnClickListener {
            var count = 0
            if(Reason == "") {
                Toast.makeText(diamond,"사유를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                db.collection("UserId").document(CurUser).get().addOnSuccessListener { res ->
                    val MyId = res.data?.get("UserName").toString()
                    Log.e(TAG, "${data}")
                    Log.e(TAG, "${Reason}")
                    db.collection("UserPost").document(data).collection(Reason).get().addOnSuccessListener { res ->
                        for(doc in res){
                            count++
                        }
                    }
                    Log.e(TAG, "${count}")
                    db.collection("UserPost").document(data).collection(Reason).document(MyId).set(
                        hashMapOf("Me" to count)
                    )

                }
                dialog.dismiss()
            }
        }

        dialog.ReasonCancle.setOnClickListener {
            dialog.dismiss()
        }
    }
}