package org.example.ada.Post

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_post_setting_not_me_dialog.*
import org.example.ada.R

class PostSettingNotMeDialog(context: Context) {
    val db = FirebaseFirestore.getInstance()
    lateinit var auth: FirebaseAuth

    private val dialog = Dialog(context)
    private lateinit var onclickListener: PostSettingNotMeDialog.onDialogClickListenserPostNotMe

    fun setOnclickListener(listener: onDialogClickListenserPostNotMe){
        onclickListener = listener
    }

    interface onDialogClickListenserPostNotMe{
        fun onclicked(data: String)
    }

    fun showDialog(data: String, diamond: Context){
        auth = FirebaseAuth.getInstance()
        val curUser = auth.currentUser?.uid.toString()

        dialog.setContentView(R.layout.activity_post_setting_not_me_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        dialog.report.setOnClickListener {
                val ReasonDialog = ReportReason(diamond)
                ReasonDialog.showDialog(data ,diamond)
                dialog.dismiss()
        }
    }
}
