package org.example.ada.setting

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.StateSet.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_first_dday_setting.*
import kotlinx.android.synthetic.main.activity_setting.*
import org.example.ada.MainActivity
import org.example.ada.R
import java.text.SimpleDateFormat
import java.util.*

class FirstDdaySettingActivity : AppCompatActivity() {

    lateinit var Jauth: FirebaseAuth

    val db = Firebase.firestore

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_dday_setting)

        val datePicker: DatePicker = findViewById(R.id.firstDdaySetting)


        Jauth = Firebase.auth

        val MyUid = Jauth.currentUser?.uid!!

        var userSetday = ""
        var userName = ""
        var connectName = ""

        datePicker.setOnDateChangedListener { datePicker, year, month, dayOfMonth ->
            userSetday = "${month + 1}-${dayOfMonth}-${year}"
        }

        settingUserName.setOnClickListener {
            val dialog = firstSettingNameDialog(this)
            dialog.showDialog()
            dialog.setOnClickListener(object : firstSettingNameDialog.OnDialogClickListener {
                override fun onClicked(name: String) {
                    settingUserName.text = name
                    userName = name
                }
            })
        }

        sorting.setOnClickListener {
            if(userName == "" || (userSetday == "" || userSetday == null)){
                Toast.makeText(this,"모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                Log.d(TAG, "이름: ${userName}, 날짜: ${userSetday}, 연결자: ${connectName})")
                firstSetting(MyUid,userName,userSetday)
            }
        }


    }

    fun firstSetting(uid: String, inputData1: String, inputData2: String) {

        var UserDday = countDate(inputData2)

        val UserInfo = hashMapOf(
            "UserName" to inputData1,
            "UserSettingDate" to inputData2,
            "UserDday" to UserDday,
            "UserUid" to uid,
        )


        db.collection("UserId").document(uid)
            .set(UserInfo)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this,"데이터 입력 완료: ${documentReference}",Toast.LENGTH_SHORT).show()
                val MainIntent = Intent(this, MainActivity::class.java)
                startActivity(MainIntent)
                finish()
            }
            .addOnFailureListener{ e ->
                Toast.makeText(this, "데이터 입력 실패: ${e}", Toast.LENGTH_SHORT).show()
            }
    }

    fun countDate(inputDate: String) : String{
        val dateFormat = SimpleDateFormat("MM-dd-yyyy")

        val startDate = dateFormat.parse(inputDate).time
        val todayTime = Calendar.getInstance().time.time

        val resultday = (todayTime - startDate)/(24 * 60 * 60 * 1000) + 1
        val reallyres = "D+ ${resultday}"
        return reallyres
    }
}