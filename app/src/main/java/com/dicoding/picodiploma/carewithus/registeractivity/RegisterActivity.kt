package com.dicoding.picodiploma.carewithus.registeractivity

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.carewithus.R
import com.dicoding.picodiploma.carewithus.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.carewithus.databinding.ActivityRegisterBinding
import com.dicoding.picodiploma.carewithus.loginactivity.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        haveAccount()

        supportActionBar?.hide()
    }

    private fun haveAccount() {
        binding.buttonLogin.setOnClickListener{
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            this.startActivity(intent, options.toBundle())
        }
    }
}