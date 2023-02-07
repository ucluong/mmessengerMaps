package com.example.messengermaps.activity


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.messengermaps.R
import com.example.messengermaps.adapter.UserAdapter
import com.example.messengermaps.firebase.FirebaseSevice
import com.example.messengermaps.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.item_user.*

class UsersActivity : AppCompatActivity() {
    var userlist = ArrayList<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        FirebaseSevice.sharedPref = getSharedPreferences("sharedPref",Context.MODE_PRIVATE)





        userRecyclerView.layoutManager= LinearLayoutManager (this, LinearLayout.VERTICAL,false)

        imgBack.setOnClickListener{
            onBackPressed()
        }
        imgProfile.setOnClickListener{
            val intent = Intent(this@UsersActivity,
                ProfileActivity::class.java)
            startActivity(intent)
        }
        getUserslist()
    }
    fun getUserslist(){
        val firebase :FirebaseUser= FirebaseAuth.getInstance().currentUser!!

        var userid = firebase.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userid")
        val databaseReference:DatabaseReference= FirebaseDatabase.getInstance().getReference("Users")



        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userlist.clear()
                val currentUser = snapshot.getValue(User::class.java)
                if (currentUser!!.profileImage==("")){
                    imgProfile.setImageResource(R.drawable.profile_image)
                }else{
                    Glide.with(this@UsersActivity).load(currentUser.profileImage).into(userImage)
                }
                for (dataSnapShot:DataSnapshot in snapshot.children){
                    val user = dataSnapShot.getValue(User::class.java)

                    if (!user!!.userId.equals(firebase.uid)){
                        userlist.add(user)
                    }

                }
                val userAdapter = UserAdapter(this@UsersActivity,userlist)

                userRecyclerView.adapter= userAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message,Toast.LENGTH_SHORT).show()

            }

        })
    }
}