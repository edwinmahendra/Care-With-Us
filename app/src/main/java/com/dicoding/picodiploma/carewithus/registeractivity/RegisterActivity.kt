package com.dicoding.picodiploma.carewithus.registeractivity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.carewithus.MainActivity
import com.dicoding.picodiploma.carewithus.customview.ButtonCustomView
import com.dicoding.picodiploma.carewithus.customview.EmailCustomView
import com.dicoding.picodiploma.carewithus.customview.PasswordCustomView
import com.dicoding.picodiploma.carewithus.customview.UsernameCustomView
import com.dicoding.picodiploma.carewithus.databinding.ActivityRegisterBinding
import com.dicoding.picodiploma.carewithus.loginactivity.LoginActivity
import com.dicoding.picodiploma.carewithus.utils.animateVisibility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var emailCustomView: EmailCustomView
    private lateinit var passwordCustomView: PasswordCustomView
    private lateinit var usernameCustomView: UsernameCustomView
    private lateinit var buttonCustomView: ButtonCustomView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        haveAccount()

        auth = FirebaseAuth.getInstance()

        supportActionBar?.hide()

        emailCustomView = binding.inputEmail
        passwordCustomView = binding.passwordLogin
        buttonCustomView = binding.buttonRegister
        usernameCustomView = binding.inputUsername

        usernameCustomView.addTextChangedListener(textChangedListener)
        emailCustomView.addTextChangedListener(textChangedListener)
        passwordCustomView.addTextChangedListener(textChangedListener)

        createAccount()

        if (auth.currentUser != null) {
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun createAccount() {
        binding.buttonRegister.setOnClickListener {
            val username = binding.inputUsername.text.toString()
            val email = binding.inputEmail.text.toString()
            val password = binding.passwordLogin.text.toString()
            progressBar(true)
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        val profileUpdates =
                            UserProfileChangeRequest.Builder().setDisplayName(username).build()
                        user?.updateProfile(profileUpdates)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        progressBar(false)
                    }
                }
        }
    }

    private fun progressBar(isLoading: Boolean) {
        binding.apply {
            inputEmail.isEnabled = !isLoading
            inputUsername.isEnabled = !isLoading
            passwordLogin.isEnabled = !isLoading
            buttonRegister.isEnabled = !isLoading

            if (isLoading) {
                viewLoading.animateVisibility(true)
            } else {
                viewLoading.animateVisibility(false)
            }
        }
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
            val resultUsername = usernameCustomView.text.toString().trim()

            val resultEmail = emailCustomView.text.toString().trim()
            val resultPass = passwordCustomView.text.toString().trim()
            buttonCustomView.isEnabled =
                resultEmail.isNotEmpty() && resultPass.isNotEmpty() && resultUsername.isNotEmpty()
        }

        override fun afterTextChanged(s: Editable) {
        }
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}