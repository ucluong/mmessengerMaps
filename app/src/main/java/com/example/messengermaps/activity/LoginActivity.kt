package com.example.messengermaps.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.messengermaps.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser : FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        firebaseUser = auth.currentUser!!

        // kiểm tra xem người dùng có đăng nhập không, sau đó điều hướng đến người dùng screer
        if (firebaseUser!= null){
            val intent=  Intent(this@LoginActivity, UsersActivity::class.java)
            startActivity(intent)
            finish()


        }

        btnLogin.setOnClickListener{
            val email= etdEmail.text.toString()
            val password = etdPassword.text.toString()

            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
                Toast.makeText(applicationContext, "email và mật khẩu được yêu cầu",Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){
                        if (it.isSuccessful){
                            etdEmail.setText("")
                            etdPassword.setText("")

                            val intent=  Intent(this@LoginActivity, UsersActivity::class.java)
                            startActivity(intent)
                           finish()
                        }else{

                            Toast.makeText(applicationContext,"Email hoặc mật khẩu không hợp lệ",Toast.LENGTH_SHORT).show()
                        }

                    }
            }

        }
        btnSignUp.setOnClickListener {
            val intent=  Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}