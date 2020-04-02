package com.gzeinnumer.chatappkt

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.gzeinnumer.chatappkt.adapter.MessageAdapter
import com.gzeinnumer.chatappkt.databinding.ActivityMessageBinding
import com.gzeinnumer.chatappkt.model.Chat
import com.gzeinnumer.chatappkt.model.User

//todo 44
class MessageActivity : AppCompatActivity() {

    //todo 45
    lateinit var binding: ActivityMessageBinding
    lateinit var firebaseUser: FirebaseUser
    lateinit var reference: DatabaseReference

    //todo 58
    lateinit var myAdapter: MessageAdapter
    var listChat: ArrayList<Chat> = ArrayList<Chat>()

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

                //todo 60
                user?.imageURL?.let { readMessage(firebaseUser.uid, userId, it) }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        binding.btnSent.setOnClickListener {
            val msg = binding.textSend.text.toString()
            if (msg != "") {
                sendMessage(firebaseUser.uid, userId, msg)
            } else {
                Toast.makeText(this@MessageActivity, "pesan jangan kosong", Toast.LENGTH_SHORT)
                    .show()
            }
            binding.textSend.setText("")
        }
    }


    //todo 50
    private fun sendMessage(
        sender: String,
        receiver: String,
        message: String
    ) {
        val reference = FirebaseDatabase.getInstance().reference
        val hashMap = mapOf("sender" to sender, "receiver" to receiver, "message" to message)
        reference.child("Chats_app").push().setValue(hashMap)
    }

    //todo 59
    private fun readMessage(
        myId: String,
        userId: String,
        imageUrl: String
    ) {
        binding.rvData.layoutManager = LinearLayoutManager(this)
        binding.rvData.setHasFixedSize(true)
        reference = FirebaseDatabase.getInstance().getReference("Chats_app")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listChat.clear()
                for (snapshot in dataSnapshot.children) {
                    val chat: Chat = snapshot.getValue(Chat::class.java)!!
                    if (chat.receiver.equals(myId) && chat.sender.equals(userId) ||
                        chat.receiver.equals(userId) && chat.sender.equals(myId)
                    ) {
                        listChat.add(chat)
                    }
                    myAdapter = MessageAdapter(listChat, imageUrl)
                    binding.rvData.adapter = myAdapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}
