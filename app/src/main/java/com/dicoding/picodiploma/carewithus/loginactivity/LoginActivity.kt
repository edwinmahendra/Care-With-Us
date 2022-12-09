package com.dicoding.picodiploma.carewithus.loginactivity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.carewithus.customview.ButtonCustomView
import com.dicoding.picodiploma.carewithus.customview.EmailCustomView
import com.dicoding.picodiploma.carewithus.customview.PasswordCustomView
import com.dicoding.picodiploma.carewithus.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.carewithus.registeractivity.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var emailCustomView: EmailCustomView
    private lateinit var passwordCustomView: PasswordCustomView
    private lateinit var buttonCustomView: ButtonCustomView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        emailCustomView = binding.emailLogin
        passwordCustomView = binding.passwordLogin
        buttonCustomView = binding.buttonLogin

        emailCustomView.addTextChangedListener(textChangedListener)
        passwordCustomView.addTextChangedListener(textChangedListener)

        dhaveAccount()

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

    private fun dhaveAccount() {
        binding.buttonRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            this.startActivity(intent, options.toBundle())
        }
    }


}