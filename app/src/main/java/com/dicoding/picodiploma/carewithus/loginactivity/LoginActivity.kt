package com.dicoding.picodiploma.carewithus.loginactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.carewithus.R
import com.dicoding.picodiploma.carewithus.customview.EmailCustomView
import com.dicoding.picodiploma.carewithus.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var emailCustomView: EmailCustomView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        emailCustomView = binding.emailLogin
    }


}