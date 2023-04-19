package org.example.ada.Post

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_post_setting_my_dialog.*
import org.example.ada.R

class PostSettingMyDialog(context: Context){
    val db = FirebaseFirestore.getInstance()
    lateinit var auth:FirebaseAuth

    private val dialog = Dialog(context)
    private lateinit var onclickListener: PostSettingMyDialog.onDialogClickListenserPostMe

    fun setOnclickListener(listener: onDialogClickListenserPostMe){
        onclickListener = listener
    }

    interface onDialogClickListenserPostMe{
        fun onclicked(data: String)
    }

    fun showDialog(data: String, diamond: Context){
        auth = FirebaseAuth.getInstance()

        dialog.setContentView(R.layout.activity_post_setting_my_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        dialog.delete.setOnClickListener {
            db.collection("UserPost").document(data).delete().addOnSuccessListener {
                Toast.makeText(diamond,"$data 삭제 완료",Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(diamond,"삭제 실패",Toast.LENGTH_SHORT).show()
                }
            dialog.dismiss()
        }

        dialog.modify.setOnClickListener {
            onclickListener.onclicked("Modify")
            dialog.dismiss()
        }

    }

}