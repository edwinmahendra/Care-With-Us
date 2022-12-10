package com.dicoding.picodiploma.carewithus.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.dicoding.picodiploma.carewithus.R
import com.dicoding.picodiploma.carewithus.databinding.ActivityAdminBinding
import com.dicoding.picodiploma.carewithus.loginactivity.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Dashboard Admin"

        auth = FirebaseAuth.getInstance()
        checkUser()

        binding.buttonAddCategory.setOnClickListener{
            startActivity(Intent(this@AdminActivity, AddCategoryActivity::class.java))
        }
    }

    private fun checkUser() {
        val firebaseUser = auth.currentUser
        if(firebaseUser == null) {
            startActivity(Intent(this@AdminActivity, LoginActivity::class.java))
            finish()
        } else {
            val email = firebaseUser.email
            binding.emailAdmin.text = email

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                auth.signOut()
                checkUser()

                true
            }
            else -> {
                super.onOptionsItemSelected(item)
                true
            }
        }
    }
}