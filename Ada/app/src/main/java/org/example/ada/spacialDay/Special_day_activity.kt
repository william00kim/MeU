package org.example.ada.spacialDay

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.StateSet.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_dairy.*
import kotlinx.android.synthetic.main.activity_dairy.Btn_back
import kotlinx.android.synthetic.main.activity_special_day.*
import org.example.ada.R

class special_day_activity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    val db = Firebase.firestore
    var OurName = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_special_day)

        auth = Firebase.auth

        val currentUser = auth.currentUser?.uid!!
        var selectdate = ""
        var importantColor = ""
        var datanumber = 0
        var gettingdata = ""

        Btn_back.setOnClickListener {
            finish()
        }

        special_content_date.setOnDateChangedListener { special_content_date, year, month, dayOfMonth ->
            selectdate = "${month + 1}-${dayOfMonth}-${year}"
        }

        ColorCheckRadio.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.pinkRadio -> importantColor = "pink" //매우 중요
                R.id.violetRadio -> importantColor = "violent" // 익숙하지만 까먹지 않기
                null -> importantColor = "gray" //중요하진 않지만 기록의무
            }

            Log.d(TAG,"선택된 날짜: ${selectdate}")

            if(selectdate == "") {
              Toast.makeText(this, "날짜를 입력해주세요",Toast.LENGTH_SHORT).show()
            } else {
                db.collection("UserId").document(currentUser).get().addOnSuccessListener { result ->
                    OurName = result.get("chatName").toString()

                    db.collection("UserContent").document(OurName).collection(selectdate).get().addOnSuccessListener{ result ->
                        if(result != null){
                            for(document in result){
                                datanumber = result.size()
                                Log.d(TAG, "데이터 갯수: ${datanumber}")
                            }
                        }
                    }
                }
            }
        }

        sort_specialContent.setOnClickListener {

            val contentTitle = add_specialday.text.toString()

            val setdatanumber = (datanumber + 1).toString()

            val inputdata = hashMapOf(
                "contentTitle" to contentTitle,
                "color" to importantColor
             )

            if(selectdate == "") {
                Toast.makeText(this, "날짜를 입력해주세요",Toast.LENGTH_SHORT).show()
            } else if(contentTitle == "") {
                Toast.makeText(this, "일정 제목을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                db.collection("UserId").document(currentUser).get().addOnSuccessListener { result ->
                    OurName = result.get("chatName").toString()
                    val dataset = db.collection("UserContent").document(OurName).collection(selectdate)

                    dataset.document(setdatanumber).set(inputdata)
                        .addOnSuccessListener {
                            Toast.makeText(this, "데이터 저장 완료", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "데이터 저장 실패", Toast.LENGTH_SHORT).show()
                        }
                    finish()
                }
            }
        }

        no_sort.setOnClickListener {
            finish()
        }
    }
}