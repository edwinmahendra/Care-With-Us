package com.dicoding.picodiploma.carewithus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        supportActionBar?.title = "Home"

        auth = FirebaseAuth.getInstance()

        binding.btnLogout.setOnClickListener{
            auth.signOut()
            Intent(this@MainActivity, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

    }
}