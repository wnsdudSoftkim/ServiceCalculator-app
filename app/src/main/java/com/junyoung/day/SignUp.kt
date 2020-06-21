package com.junyoung.day

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_home_fragment_tab.*
import kotlinx.android.synthetic.main.signup.*

class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val viewModel: DayViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        // FirebaseAuth 객체의 공유 인스턴스를 가져옴
        auth = FirebaseAuth.getInstance()
        //신규 사용자의 이메일 주소와 비밀번호를 createUserWithEmailAndPassword에 전달

        signup_btn.setOnClickListener {
            if (signup_email.text.toString().length <= 0 && signup_password.text.toString().length <= 0) {
                Toast.makeText(this, "이메일과 비밀번호를 기입해주세요", Toast.LENGTH_LONG).show()
            }else {
                onClick()

            }
        }
        gologin.setOnClickListener {
            startActivity(Intent(this,Login2::class.java))
        }
    }

    private fun onClick() {
        signup()
    }

    fun signup() {
        val signupEmail = findViewById<EditText>(R.id.signup_email).text.toString()
        val signupPassword = findViewById<EditText>(R.id.signup_password).text.toString()


        auth.createUserWithEmailAndPassword(signupEmail, signupPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    viewModel.addDataCalenderInit()

                    val user = auth.currentUser
                    updateUI(user)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Sign", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "이메일과 비밀번호 양식을 정확히 기입해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }

    }

    fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            Toast.makeText(this, "홈 화면으로 이동합니다.", Toast.LENGTH_LONG).show()
            viewModel.addDataCalenderInit()
            startActivity(Intent(this, Home::class.java))
        } else {
            Toast.makeText(this, "?????", Toast.LENGTH_SHORT).show()
        }
    }


}