package org.example.ada

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.StateSet.TAG
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_connect_push_dialog.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_setting.*
import org.example.ada.Post.activity_post
import org.example.ada.chatting.ChattingList
import org.example.ada.diary.DairyActivity
import org.example.ada.setting.ConnectPushDialog
import org.example.ada.setting.SettingActivity
import org.example.ada.spacialDay.DdayActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    val db = Firebase.firestore

    var pressedBackButton: Long = 0
    var intentDday: String = ""
    var ConnectMyLove = ""
    var MyName = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loaddata()

        val ddayIntent = Intent(this, DdayActivity::class.java)

        DdayButton.setOnClickListener {
            startActivity(ddayIntent)
            ddayIntent.putExtra("DdaySetting", intentDday)
        }

        val dairyIntent = Intent(this, DairyActivity::class.java)

        DairyBtn.setOnClickListener{
            if(ConnectMyLove == null || ConnectMyLove == "") {
                Toast.makeText(this, "상대방과 연결되지 않았습니다.",Toast.LENGTH_SHORT).show()
            } else {
                startActivity(dairyIntent)
            }
        }

        val settingIntent = Intent(this, SettingActivity::class.java)

        Btn_setting.setOnClickListener {
            startActivity(settingIntent)
        }

        val PostingIntent = Intent(this, activity_post::class.java)

        OurStory.setOnClickListener {
            var options: ActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                this, Pair.create(OurStory, "ButtonTransition"))
            startActivity(PostingIntent, options.toBundle())
        }

        val ChattingActivity = Intent(this, ChattingList::class.java)

        Btn_chatting.setOnClickListener{
            startActivity(ChattingActivity)
        }
    }

    override fun onBackPressed() {
        if(pressedBackButton + 3000 > System.currentTimeMillis()){
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(applicationContext, "뒤로가기 두번 누를 시 종료", Toast.LENGTH_SHORT).show()
        }
        pressedBackButton = System.currentTimeMillis()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loaddata() {
        val rightNowDay: LocalDate = LocalDate.now()
        val auth = Firebase.auth
        val CurrentUser = auth.currentUser?.uid!!
        var wantConnect = ""
        var ConnectStatus = ""

        db.collection("UserId").document(CurrentUser).get()
            .addOnSuccessListener { result ->
                DdayNumber.text = countDate(result.data?.get("UserSettingDate").toString())
                ConnectMyLove = result.data?.get("connect").toString()
                MyName = result.data?.get("UserName").toString()
                wantConnect = result.data?.get("wantconnect").toString()
                ConnectStatus = result.data?.get("connectresult").toString()

                //푸시 알림을 받았을 때
                if(wantConnect != null && ConnectStatus == "No") {
                    val dialog = ConnectPushDialog(this)
                    dialog.showDialog()
                    dialog.setOnclickListener(object : ConnectPushDialog.OnDialogClickListener {
                        override fun onclicked(data: String, opponentUid: String) {
                            if(data != "=") {
                                val YesMyData = hashMapOf(
                                    "connectresult" to "Yes",
                                    "connect" to data,
                                    "chatName" to "$MyName, $data"
                                )
                                val YesOpponentData = hashMapOf(
                                    "connectresult" to "Yes",
                                    "connect" to MyName,
                                    "chatName" to "$MyName, $data"
                                )
                                db.collection("UserId").document(opponentUid).update(YesOpponentData as Map<String, Any>)
                                db.collection("UserId").document(CurrentUser).update(YesMyData as Map<String, Any>)
                            } else if(data == "=") {
                                val NoData = hashMapOf(
                                    "connectresult" to "No",
                                    "connect" to "="
                                )
                                db.collection("UserId").document(opponentUid).update(NoData as Map<String, Any>)
                                db.collection("UserId").document(CurrentUser).update(NoData as Map<String, Any>)
                            }
                        }
                    })
                }

                if(ConnectMyLove == null) {
                    ConnectedPersonThink.text = "상대방 연결 후 사용"
                    Lover.text = "상대방"
                } else {
                    Me.text = MyName
                    Lover.text = ConnectMyLove
                }
            }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener{ task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                val token = task.result
                val take = hashMapOf(
                    "UserToken" to token
                )

                db.collection("UserToken").document(CurrentUser).set(take)
                    .addOnCompleteListener {
                        Log.d(TAG, "토큰: $token")
                        Log.d(TAG,"여부: 성공")
                    }
            }
        )

        db.collection("UserDiary").document(CurrentUser).collection("${rightNowDay}").document("MyDiary").get()
            .addOnSuccessListener { result ->
                if(result.data?.get("MyData") == null) {
                    myThink.text = "오늘은.."
                    Log.d(TAG, "오늘: ${rightNowDay}")
                    Log.d(TAG,"일기장 데이터: ${result.data?.get("MyData")}")
                } else if(result.data?.get("MyData") != null) {
                    myThink.text = result.data?.get("MyData").toString()
                    Log.d(TAG,"일기장 데이터: ${result.data?.get("MyData")}")
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,"실패",Toast.LENGTH_SHORT).show()
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