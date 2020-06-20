package com.junyoung.day

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login2.*

class Login2 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        auth = FirebaseAuth.getInstance()
        login_login.setOnClickListener {
            if (signin_email.text.toString().length<=0  || signin_password.text.toString().length<=0) {
                Toast.makeText(this,"아이디 비밀번호 입력하세요",Toast.LENGTH_LONG).show()
            }else {
                onClick()
            }
        }
        jumpsign.setOnClickListener {
            startActivity(Intent(this,SignUp::class.java))
        }
    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    private fun onClick() {
        getLogin()
    }

    private fun getLogin() {
        val loginEmail = signin_email.text.toString()
        val loginPassword = signin_password.text.toString()
        auth.signInWithEmailAndPassword(loginEmail ,loginPassword)
            .addOnCompleteListener(this) { task ->
                Toast.makeText(this,"로그인에러:"+task.exception,Toast.LENGTH_LONG).show()
                if (task.isSuccessful==true) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this,"성공",Toast.LENGTH_LONG).show()
                    val user = auth.currentUser
                    updateUI(user)

                } else {
                    // If sign in fails, display a message to the user.
                   Toast.makeText(this,"아이디와 비밀번호 양식을 정확히 기입해주세요",Toast.LENGTH_SHORT)
                    // ...
                }

                // ...
            }
    }
    fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            Toast.makeText(this, "홈 화면으로 이동합니다.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, Home::class.java))
        } else {
            Toast.makeText(this, "?????"+account, Toast.LENGTH_SHORT).show()
        }
    }



}
