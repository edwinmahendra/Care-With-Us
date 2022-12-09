package com.dicoding.picodiploma.carewithus

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.carewithus.databinding.ActivityMainBinding
import com.dicoding.picodiploma.carewithus.loginactivity.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        supportActionBar?.title = "Home"

        val firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null) {
            binding.textView.setText(firebaseUser.displayName)
        }else {
            binding.textView.setText("Login failed")
        }

        binding.btnLogout.setOnClickListener{
            auth.signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}