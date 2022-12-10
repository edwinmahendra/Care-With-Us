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
import com.dicoding.picodiploma.carewithus.AdminActivity
import com.dicoding.picodiploma.carewithus.MainActivity
import com.dicoding.picodiploma.carewithus.customview.ButtonCustomView
import com.dicoding.picodiploma.carewithus.customview.EmailCustomView
import com.dicoding.picodiploma.carewithus.customview.PasswordCustomView
import com.dicoding.picodiploma.carewithus.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.carewithus.registeractivity.RegisterActivity
import com.dicoding.picodiploma.carewithus.utils.animateVisibility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
            val firebaseUser = auth.currentUser!!
            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot){
                        val userType = snapshot.child("userType").value
                        if (userType == "user"){
                            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                            finish()
                        }else if (userType =="admin"){
                            startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
                            finish()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }

    private fun logginAccount() {
        binding.buttonLogin.setOnClickListener {
            progressBar(true)
            val email = binding.emailLogin.text.toString()
            val password = binding.passwordLogin.text.toString()
            if (email != "" && password != "") {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(this) {
                        checkUser()
                    }
                    .addOnFailureListener{e->
                        Toast.makeText(this,"Login Failed", Toast.LENGTH_SHORT).show()
                        progressBar(false)
                    }
            }
        }
    }


    private fun checkUser(){
        val firebaseUser = auth.currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot){
                    val userType = snapshot.child("userType").value
                    if (userType == "user"){
                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        finish()
                    }else if (userType =="admin"){
                        startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
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
            buttonLogin.isEnabled = !isLoading

            if (isLoading) {
                viewLoading.animateVisibility(true)
            } else {
                viewLoading.animateVisibility(false)
            }
        }
    }
}