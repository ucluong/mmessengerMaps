package com.example.messengermaps.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.messengermaps.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener{
            val userName = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (TextUtils.isEmpty(userName)){
                Toast.makeText(applicationContext,"Tên người dùng là bắt buộc", Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(email)){
                Toast.makeText(applicationContext,"Email thì cần thiết", Toast.LENGTH_SHORT).show()
            }

            if (TextUtils.isEmpty(password)){
                Toast.makeText(applicationContext,"Mật khẩu là bắt buộc", Toast.LENGTH_SHORT).show()
            }

            if (TextUtils.isEmpty(confirmPassword)){
                Toast.makeText(applicationContext,"Xác nhận mật khẩu ", Toast.LENGTH_SHORT).show()
            }

            if (!password.equals(confirmPassword)){
                Toast.makeText(applicationContext,"Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
            }

            registerUser(userName,email,password)
        }
        btnSignUp.setOnClickListener {
            val intent= Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser (userName:String, email:String,password:String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful){
                    val user:FirebaseUser? = auth.currentUser
                    val userId:String= user!!.uid

                    databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    val hashMap:HashMap<String,String> = HashMap()
                    hashMap.put("userId",userId)
                    hashMap.put("userName", userName)
                    hashMap.put("profileImage","")

                    databaseReference.setValue(hashMap).addOnCompleteListener(this) {
                        if (it.isSuccessful){
                            //open home activity
                            edtName.setText("")
                            edtEmail.setText("")
                            edtPassword.setText("")
                            etConfirmPassword.setText("")
                            val intent= Intent(this@SignUpActivity, UsersActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
    }
}