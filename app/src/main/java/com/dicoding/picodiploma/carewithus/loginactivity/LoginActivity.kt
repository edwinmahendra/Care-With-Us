package com.dicoding.picodiploma.carewithus.loginactivity

import android.app.ActivityOptions
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.carewithus.MainActivity
import com.dicoding.picodiploma.carewithus.customview.ButtonCustomView
import com.dicoding.picodiploma.carewithus.customview.EmailCustomView
import com.dicoding.picodiploma.carewithus.customview.PasswordCustomView
import com.dicoding.picodiploma.carewithus.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.carewithus.registeractivity.RegisterActivity
import com.dicoding.picodiploma.carewithus.utils.animateVisibility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

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

        auth = FirebaseAuth.getInstance()

        emailCustomView = binding.emailLogin
        passwordCustomView = binding.passwordLogin
        buttonCustomView = binding.buttonLogin

        emailCustomView.addTextChangedListener(textChangedListener)
        passwordCustomView.addTextChangedListener(textChangedListener)

        dhaveAccount()
        logginAccount()

        if (auth.currentUser != null) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun logginAccount() {
        binding.buttonLogin.setOnClickListener {
            progressBar(true)
            val email = binding.emailLogin.text.toString()
            val password = binding.passwordLogin.text.toString()
            if (email != "" && password != "") {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "${it.exception?.message}")
                            progressBar(false)
                        }
                    }
            } else {
                Toast.makeText(
                    baseContext, "Login failed.",
                    Toast.LENGTH_SHORT
                ).show()
                progressBar(false)
            }
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

    private fun progressBar(isLoading: Boolean) {
        binding.apply {
            emailLogin.isEnabled = !isLoading
            passwordLogin.isEnabled = !isLoading
            buttonRegister.isEnabled = !isLoading

            if (isLoading) {
                viewLoading.animateVisibility(true)
            } else {
                viewLoading.animateVisibility(false)
            }
        }
    }
}