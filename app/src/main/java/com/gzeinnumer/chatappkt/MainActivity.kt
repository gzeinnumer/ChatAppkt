package com.gzeinnumer.chatappkt

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.gzeinnumer.chatappkt.databinding.ActivityMainBinding
import com.gzeinnumer.chatappkt.model.User

class MainActivity : AppCompatActivity() {
    //todo 28
    private lateinit var binding: ActivityMainBinding
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //todo 29
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users_chat_app").child(firebaseUser!!.uid)

        //todo 31
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User? = dataSnapshot.getValue<User>(User::class.java)
                user?.let {
                    binding.username.text = it.username
                    if (it.imageURL == "default") {
                        binding.profileImage.setImageResource(R.mipmap.ic_launcher)
                    } else {
                        Glide.with(this@MainActivity).load(it.imageURL)
                            .into(binding.profileImage)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


    //todo 33
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    //todo 34
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@MainActivity, StartActivity::class.java))
            finish()
        }
        return false
    }
}
