package com.gzeinnumer.chatappkt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.gzeinnumer.chatappkt.databinding.ActivityMessageBinding
import com.gzeinnumer.chatappkt.model.User

//todo 44
class MessageActivity : AppCompatActivity() {

    //todo 45
    lateinit var binding: ActivityMessageBinding
    lateinit var firebaseUser: FirebaseUser
    lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //todo 46
        binding = ActivityMessageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(binding.root)

        //todo 48
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val userId = intent.getStringExtra("id")
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users_chat_app").child(userId!!)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User? = dataSnapshot.getValue<User>(User::class.java)
                binding.username.text = user?.username
                if (user?.imageURL.equals("default")) {
                    binding.profileImage.setImageResource(R.mipmap.ic_launcher)
                } else {
                    Glide.with(applicationContext).load(user?.imageURL)
                        .into(binding.profileImage)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}
