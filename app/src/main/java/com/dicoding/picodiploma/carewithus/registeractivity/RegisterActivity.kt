package com.dicoding.picodiploma.carewithus.registeractivity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.carewithus.customview.ButtonCustomView
import com.dicoding.picodiploma.carewithus.customview.EmailCustomView
import com.dicoding.picodiploma.carewithus.customview.PasswordCustomView
import com.dicoding.picodiploma.carewithus.databinding.ActivityRegisterBinding
import com.dicoding.picodiploma.carewithus.loginactivity.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var emailCustomView: EmailCustomView
    private lateinit var passwordCustomView: PasswordCustomView
    private lateinit var buttonCustomView: ButtonCustomView

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        haveAccount()

        supportActionBar?.hide()

        emailCustomView = binding.inputEmail
        passwordCustomView = binding.passwordLogin
        buttonCustomView = binding.buttonRegister

        emailCustomView.addTextChangedListener(textChangedListener)
        passwordCustomView.addTextChangedListener(textChangedListener)
    }

    private fun haveAccount() {
        binding.buttonLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            this.startActivity(intent, options.toBundle())
        }
    }

    private val textChangedListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val resultEmail = emailCustomView.text.toString().trim()
            val resultPass = passwordCustomView.text.toString().trim()
            buttonCustomView.isEnabled = resultEmail.isNotEmpty() && resultPass.isNotEmpty()
        }

        override fun afterTextChanged(s: Editable) {
        }
    }
}