package org.example.ada

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null) {
            val MainIntent = Intent(this, MainActivity::class.java)
            startActivity(MainIntent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        Login.setOnClickListener {
            var email = StartEmail.text.toString()
            var pass = StartPassword.text.toString()

            if((email == "") || (pass == "")) {
                Toast.makeText(this, "아이디 혹은 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                login(email, pass)
            }
        }

        SignUp.setOnClickListener {
            val SignUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(SignUpIntent)
        }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password) // 로그인
            .addOnCompleteListener(this) { result->
                if(result.isSuccessful){
                    val MainIntent = Intent(this, MainActivity::class.java)
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT)
                    startActivity(MainIntent)
                    finish()
                } else {
                    Toast.makeText(this, "존재하지 않는 아이디 혹은 올바르지 않은 비밀번호입니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}