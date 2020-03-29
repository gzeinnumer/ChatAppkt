package com.gzeinnumer.chatappkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gzeinnumer.chatappkt.databinding.ActivityLoginBinding
import com.gzeinnumer.chatappkt.databinding.ActivityStartBinding

//Connected to FirebaseMVVM
class StartActivity : AppCompatActivity() {

    //todo 23
    lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //todo 24
        binding = ActivityStartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLogin.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    LoginActivity::class.java
                ).apply {
//                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
        }

        binding.btnRegister.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    RegisterActivity::class.java
                ).apply {
//                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
        }
    }
}
