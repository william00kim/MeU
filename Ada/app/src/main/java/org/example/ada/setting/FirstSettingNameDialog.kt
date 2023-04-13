package org.example.ada.setting

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_first_setting_name_dialog.*
import org.example.ada.R

class firstSettingNameDialog(context: Context){

    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener) {
        onClickListener = listener
    }

    fun showDialog(){
        dialog.setContentView(R.layout.activity_first_setting_name_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        val sortName = dialog.findViewById<EditText>(R.id.FirstNickname)

        dialog.Yes.setOnClickListener {
            onClickListener.onClicked(sortName.text.toString())
            dialog.dismiss()
        }

        dialog.No.setOnClickListener {
            dialog.dismiss()
        }
    }

    interface OnDialogClickListener{
        fun onClicked(name: String)
    }
}