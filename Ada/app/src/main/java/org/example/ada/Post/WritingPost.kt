package org.example.ada.Post

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_writing_post.*
import org.example.ada.R
import java.time.LocalDateTime

class WritingPost : AppCompatActivity() {

    val REQ_CAMERA_PERMISSION = 1
    val REQ_GALLERY = 2

    lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    lateinit var imageUri: Uri

    val storage = Firebase.storage

    var UserName = ""

    var MyPostNumber = 0

    @RequiresApi(Build.VERSION_CODES.O)
    val settime = LocalDateTime.now()
    @RequiresApi(Build.VERSION_CODES.O)
    var phototitle = "${settime.year}${settime.month}${settime.dayOfMonth}${settime.hour}${settime.minute}"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing_post)

        loaddata()

        ImportImage.setOnClickListener {
            selectGallery()
        }

        Btn_back3.setOnClickListener {
            finish()
        }

        Sort.setOnClickListener {
            sort()
        }

        if(UserCheckImage.drawable == null) {
            UserCheckImage
        }
    }

    private fun selectGallery() {
        val writePermission = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
        val readPermission = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)

        if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
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
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
            return
        } else {
            when (requestCode) {
                REQ_GALLERY -> {
                    data?.data.let { uri ->
                        UserCheckImage.setImageURI(uri)
                        imageUri = uri!!
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sort() {
        auth = Firebase.auth

        val CurrentUser = auth.currentUser?.uid!!

        var Post = posting.editableText.toString()

        MyPostNumber += 1

        val inputdata = hashMapOf(
            "이름" to UserName,
            "이미지" to "${phototitle}.png",
            "게시글" to Post,
            "유저 아이디" to CurrentUser
        )


        db.collection("UserPost").document("$MyPostNumber").set(inputdata)
            .addOnSuccessListener {
                if (imageUri != null) {
                    uploadPhoto(
                        imageUri!!,
                        mSuccessHandler = {
                            Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
                        },
                        mErrorHandeler = {
                            Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadPhoto(
        imageUri: Uri,
        mSuccessHandler: (String) -> Unit,
        mErrorHandeler: () -> Unit
    ) {
        val filename = "${phototitle}.png"
        storage.reference.child("ada/photo").child(filename)
            .putFile(imageUri)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    storage.reference.child("ada/photo").child(filename).downloadUrl
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

    private fun loaddata() {
        auth = Firebase.auth
        val db = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser?.uid!!
        db.collection("UserId").document(currentUser).get()
            .addOnSuccessListener { result ->
                UserName = result.data!!["UserName"].toString()
            }

        db.collection("UserPost").get()
            .addOnSuccessListener { result ->
                if(result != null)
                    for(document in result){
                        MyPostNumber += 1
                } else {
                    MyPostNumber = 0
                }
            }
    }
}