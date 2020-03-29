package com.gzeinnumer.chatappkt

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.gzeinnumer.chatappkt.databinding.ActivityLoginBinding

//todo 15 buat activity
class LoginActivity : AppCompatActivity() {
    //todo 16
    lateinit var binding: ActivityLoginBinding

    //todo 19
    var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //todo 17
        //todo 17
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //todo 18
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Chat App Java Login")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //todo 20
        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            if (TextUtils.isEmpty(binding.email.text.toString()) || TextUtils.isEmpty(
                    binding.pass.text.toString()
                )
            ) {
                Toast.makeText(
                    this@LoginActivity,
                    "Tidak boleh ada yang kosong",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.pass.text.toString().length < 6) {
                Toast.makeText(
                    this@LoginActivity,
                    "Password minimal 6 karakter",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                login(binding.email.text.toString(), binding.pass.text.toString())
            }
        }
    }

    //todo 21
    private fun login(email: String, pass: String) {
        auth!!.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
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
                Toast.makeText(this@LoginActivity, "Gagal Login", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
