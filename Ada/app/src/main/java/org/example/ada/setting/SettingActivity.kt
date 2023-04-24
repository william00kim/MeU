package org.example.ada.setting

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_setting.*
import org.example.ada.LoginActivity
import org.example.ada.MainActivity
import org.example.ada.R
import java.text.SimpleDateFormat
import java.util.*

class SettingActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    val db = FirebaseFirestore.getInstance()

    val storage = Firebase.storage

    val REQ_CAMERA_PERMISSION = 1
    val REQ_GALLERY = 2

    val CHANNEL_ID = "1000"
    val CHANNEL_NAME = "MyChannel"

    lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        auth = Firebase.auth
        val currentUser = auth.currentUser?.uid!!

        lateinit var DdayStart: String
        lateinit var UserDday: String
        var connectName = ""

        val getData = db.collection("UserId").document(currentUser).get()

        val intent = Intent(this, MainActivity::class.java)
        val LoginIntent = Intent(this, LoginActivity::class.java)

        Btn_back3.setOnClickListener {
            ModifyData(currentUser, "settingDay", Dday.text.toString())
            ModifyData(currentUser, "Dday",UserDday)
            finish()
        }

        DdaySetting.setOnClickListener{
            val cal = Calendar.getInstance()
            var iYear = cal.get(Calendar.YEAR)
            var iYMonth= cal.get(Calendar.MONTH)
            var iDay = cal.get(Calendar.DAY_OF_MONTH)

            var listener = object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    DdayStart = "${month + 1}-${dayOfMonth}-${year}"
                    Dday.text = DdayStart
                }
            }

            var picker = DatePickerDialog(this, listener, iYear, iYMonth, iDay)
            picker.show()

        }

        User_Id.setOnClickListener {
            signOut()
            startActivity(LoginIntent)
            finish()
        }

        getData
            .addOnSuccessListener { document ->
                Dday.text = document.data?.get("UserSettingDate").toString()
                NickName.text = document.data?.get("UserName").toString()
                ConnectedId.text = document.data?.get("connect").toString()
                UserDday = countDate(Dday.text.toString())
            }
            .addOnFailureListener {
                Toast.makeText(this, "데이터가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
            }

        UserImageSet.setOnClickListener{
            selectGallery()
        }

        Connect.setOnClickListener {
            db.collection("UserId").document(currentUser).get().addOnSuccessListener { res ->
                var connectResult = res.data?.get("connectresult").toString()
                if(connectResult == null || connectResult == "No"){
                    val dialog = ConnectPersonDialog(this)
                    dialog.showDialog()
                    dialog.setOnClickListener(object : ConnectPersonDialog.OnDialogClickListener2 {
                        override fun onClicked(name: String) {
                            ConnectedId.text = name
                            connectName = name
                        }
                    })
                } else {
                    Toast.makeText(this, "이미 연결되어있습니다!",Toast.LENGTH_SHORT).show()
                }
            }
        }

        CutOpponent.setOnClickListener {
            val dialog = CutOpponentDialog(this)
            dialog.showDialog()
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun signOut() {
        Firebase.auth.signOut()
    }

    fun ModifyData(SelectDoc: String, ModifyDataType: String, ModifyData: String?) {

        auth = Firebase.auth
        val currentUser = auth.currentUser?.uid!!

        if(ModifyDataType == "Name") {

            val updateName = db.collection("UserId").document(currentUser).update("UserName",ModifyData)

            updateName.addOnSuccessListener {
            }

        } else if(ModifyDataType == "settingDay") {
            val updatesettingday = db.collection("UserId").document(currentUser).update("UserSettingDate",ModifyData)
            updatesettingday.addOnSuccessListener {
            }
        } else if(ModifyDataType == "Dday") {
            val updateDday = db.collection("UserId").document(currentUser).update("UserDday", ModifyData)
            updateDday.addOnSuccessListener {
            }
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

    private fun uploadPhoto(
        imageUri: Uri,
        mSuccessHandler: (String) -> Unit,
        mErrorHandeler: () -> Unit
    ) {
        auth = Firebase.auth
        val currentUser = auth.currentUser?.uid!!

        storage.reference.child("ada/profile").child("$currentUser.png")
            .putFile(imageUri)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    storage.reference.child("ada/photo").child("$currentUser.png").downloadUrl
                        .addOnSuccessListener { uri ->
                            mSuccessHandler(uri.toString())
                        }.addOnFailureListener {
                            mErrorHandeler()
                        }
                } else {
                    mErrorHandeler()
                }
            }
    }

    private fun selectGallery() {
        val writePermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                REQ_CAMERA_PERMISSION
            )
        } else {
            var intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, REQ_GALLERY)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        auth = Firebase.auth
        val currentUser = auth.currentUser?.uid!!

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
            return
        } else {
            when (requestCode) {
                REQ_GALLERY -> {
                    data?.data.let { uri ->
                        imageUri = uri!!
                        db.collection("UserId").document(currentUser).update("UserPhoto","$currentUser.png")
                            .addOnSuccessListener {
                                uploadPhoto(imageUri,
                                    mSuccessHandler = {
                                        Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
                                    },
                                    mErrorHandeler = {
                                        Toast.makeText(this, "에러실패", Toast.LENGTH_SHORT).show()
                                    })
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        }
    }

    private fun createNotification(builder: Notification.Builder, notificationId: Int){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val descriptionText = "1번 채널입니다."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("MyChannel", "channel1", importance).apply {
                description = descriptionText
            }

            channel.lightColor = Color.BLUE
            channel.enableVibration(true)

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            notificationManager.notify(notificationId, builder.build())
        } else {
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationId, builder.build())
        }
    }


}