package com.gzeinnumer.chatappkt

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.gzeinnumer.chatappkt.databinding.ActivityRegisterBinding

//todo 5 buat activity
class RegisterActivity : AppCompatActivity() {

    //todo 8 view binding
    private lateinit var binding: ActivityRegisterBinding

    //todo 10 set firebase
    var auth: FirebaseAuth? = null
    var reference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //todo 9 set view binding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //todo 14 set toolbar
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Chat App Kotlin Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //todo 11
        auth = FirebaseAuth.getInstance()

        //todo 13
        binding.btnRegister.setOnClickListener {

            if (TextUtils.isEmpty(binding.username.text.toString()) || TextUtils.isEmpty(
                    binding.email.text.toString()
                ) || TextUtils.isEmpty(binding.pass.text.toString())
            ) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Tidak boleh ada yang kosong",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.pass.text.toString().length < 6) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Password minimal 6 karakter",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                register(
                    binding.username.text.toString(),
                    binding.email.text.toString(),
                    binding.pass.text.toString()
                )
            }
        }
    }


    //todo 12 buat function register
    private fun register(
        username: String,
        email: String,
        password: String
    ) {
        auth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener { task ->
            run {
                if (task.isSuccessful) {
                    val firebaseUser = auth?.currentUser
                    val userId = firebaseUser?.uid

                    reference = userId?.let {
                        FirebaseDatabase.getInstance().getReference("Users_chat_app").child(it)
                    }
//                    val hashMap = mapOf("id" to userId, "username" to username, "imageURL" to "default")
                    //todo 93 komentarkan yang diatas
//                    val hashMap = mapOf("id" to userId, "username" to username, "imageURL" to "default", "status" to "offline")
                    //todo 93
                    //todo 99 komentarkan yang diatas
                    val hashMap = mapOf("id" to userId, "username" to username, "imageURL" to "default", "status" to "offline", "search" to username.toLowerCase())
                    //todo 99
                    //todo 100 perbaiki struktur di firebase dan tambahkan search
                    reference?.setValue(hashMap)?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            startActivity(
                                Intent(
                                    applicationContext,
                                    MainActivity::class.java
                                ).apply {
                                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                })
                            finish()
                        } else {
                            Toast.makeText(applicationContext, "Gagal Register", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }
}
