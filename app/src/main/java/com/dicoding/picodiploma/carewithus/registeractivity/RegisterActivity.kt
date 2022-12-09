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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var emailCustomView: EmailCustomView
    private lateinit var passwordCustomView: PasswordCustomView
    private lateinit var usernameCustomView: UsernameCustomView
    private lateinit var buttonCustomView: ButtonCustomView
    private lateinit var auth: FirebaseAuth
    private var name = ""
    private var email = ""
    private var password = ""


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
    }

    private fun createAccount() {
        binding.buttonRegister.setOnClickListener {
            name = binding.inputUsername.text.toString().trim()
            email = binding.inputEmail.text.toString().trim()
            password = binding.passwordLogin.text.toString().trim()
            progressBar(true)
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(this) {
                    updateUserInfo()
                    }
                .addOnFailureListener(this){
                    progressBar(false)
                    Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateUserInfo(){
        val timestamp = System.currentTimeMillis()
        val uid = auth.uid
        val hashMap: HashMap<String,Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["email"] = email
        hashMap["name"] = name
        hashMap["profileImage"] = ""
        hashMap["userType"]= "user"
        hashMap["timestamp"] = timestamp
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
            .addOnFailureListener { e->
                Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun berhasil(user: FirebaseUser?) {
        if (user!=null){
            auth.signOut()
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
}