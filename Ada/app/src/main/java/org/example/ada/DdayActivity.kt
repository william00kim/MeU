package org.example.ada

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.StateSet.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_dday2.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DdayActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    val db = FirebaseFirestore.getInstance()

    var settingDate: String = ""

    var ColorData = ""
    var ContentData = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dday2)

        var datasize = 0

        loadData()

        auth = Firebase.auth
        val CurrentUser = auth.currentUser?.uid!!

        var selecteddate = ""

        val backintent = Intent(this, MainActivity::class.java)

        Btn_back.setOnClickListener{
            startActivity(backintent)
            finish()
        }

        InDdayCalender.setOnDateChangeListener{ InDdayCalender, year, month, dayOfMonth ->
            SelectDate.text = "${year}년 ${month + 1}월 ${dayOfMonth}일"
            selecteddate = "${month + 1}-${dayOfMonth}-${year}"

            val UserContent = mutableListOf<Content>()

            db.collection("UserContent").document(CurrentUser).collection(selecteddate).get()
                .addOnSuccessListener { result ->
                    if(result.isEmpty) {
                        UserContent.add(Content("일정을 추가해주세요.", "gray"))
                        val Adapter = ContentAdapter(this, UserContent)
                        ListView.adapter = Adapter
                    } else {
                        if (result != null) {
                            UserContent.clear()
                            for (document in result) {
                                ColorData = document.data?.get("color").toString()
                                ContentData = document.data?.get("contentTitle").toString()
                                UserContent.add(Content(ContentData, ColorData))
                                val Adapter = ContentAdapter(this, UserContent)
                                ListView.adapter = Adapter
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "데이터 불러오기 실패", Toast.LENGTH_SHORT).show()
                }
        }

        AddContent.setOnClickListener {
            val SpecialIntent = Intent(this, special_day_activity::class.java)
            startActivity(SpecialIntent)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData() {
        val rightNowDay: LocalDate = LocalDate.now()
        val setRightNowDay = rightNowDay.format(DateTimeFormatter.ofPattern("M-dd-yyyy"))
        auth = Firebase.auth
        val CurrentUser = auth.currentUser?.uid!!
        val UserContent = mutableListOf<Content>()

        db.collection("UserId").document(CurrentUser).get()
            .addOnSuccessListener { document ->
                 settingDate = document.data!!["UserSettingDate"].toString()
                txt_Dday.text = countDate(settingDate)
            }

        SelectDate.text = "${rightNowDay.year}년 ${rightNowDay.monthValue}월 ${rightNowDay.dayOfMonth}일"

        db.collection("UserContent").document(CurrentUser).collection("${setRightNowDay}").get()
            .addOnSuccessListener { result ->
                if(result.isEmpty) {
                    UserContent.add(Content("일정을 추가해주세요.", "gray"))
                    Log.d(TAG,"색상:${ColorData}, 콘텐트: ${ContentData}")
                    Log.d(TAG,"날짜:${setRightNowDay}")
                    val Adapter = ContentAdapter(this, UserContent)
                    ListView.adapter = Adapter
                } else {
                    if (result != null) {
                        for (document in result) {
                            ColorData = document.data?.get("color").toString()
                            ContentData = document.data?.get("contentTitle").toString()
                            Log.d(TAG,"날짜:${setRightNowDay}")
                            Log.d(TAG,"색상:${ColorData}, 콘텐트: ${ContentData}")
                            UserContent.add(Content(ContentData, ColorData))
                            val Adapter = ContentAdapter(this, UserContent)
                            ListView.adapter = Adapter
                        }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "데이터 불러오기 실패", Toast.LENGTH_SHORT).show()
            }

    }

    private fun countDate(inputDate: String) : String{
        val dateFormat = SimpleDateFormat("MM-dd-yyyy")

        val startDate = dateFormat.parse(inputDate).time
        val todayTime = Calendar.getInstance().time.time

        val resultday = (todayTime - startDate)/(24 * 60 * 60 * 1000) + 1
        val reallyres = "D+ ${resultday}"
        return reallyres
    }
}