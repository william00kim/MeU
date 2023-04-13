package org.example.ada

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.example.ada.setting.FirstDdaySettingActivity

class SignUpActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth

        ConnectButton.setOnClickListener {
            var emailsign = ConnectId.text.toString().trim()
            var passsign = SignUpPassword.text.toString().trim()

            if((emailsign == "") || (passsign == "")) {
                Toast.makeText(this, "아이디 혹은 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(emailsign, passsign)
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show()
                    val FirstSettingIntent = Intent(this, FirstDdaySettingActivity::class.java)
                    startActivity(FirstSettingIntent)
                    finish()
                } else {
                    Toast.makeText(this, "이미 존재하는 계정이거나, 회원가입에 실패했습니다.", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this,"화원가입 실패, 다시 확인해주세요", Toast.LENGTH_SHORT)
            }
    }
}