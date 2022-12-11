package com.dicoding.picodiploma.carewithus.splash

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dicoding.picodiploma.carewithus.R
import com.dicoding.picodiploma.carewithus.admin.AdminActivity
import com.dicoding.picodiploma.carewithus.databinding.ActivitySplashScreenActivityBinding
import com.dicoding.picodiploma.carewithus.loginactivity.LoginActivity
import com.dicoding.picodiploma.carewithus.user.UserActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenActivityBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        showProgress()


    }

    private fun showProgress() {
        Handler(Looper.getMainLooper()).postDelayed({
            val progress = binding.loadingSplash.progress + 20
            binding.loadingSplash.progress = progress
            val options = ActivityOptions.makeCustomAnimation(
                this,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            this.startActivity(intent, options.toBundle())
            when {
                progress >= 100 -> {
                    if (auth.currentUser != null) {
                        val firebaseUser = auth.currentUser!!
                        val ref = FirebaseDatabase.getInstance().getReference("Users")
                        ref.child(firebaseUser.uid)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot){
                                    val userType = snapshot.child("userType").value
                                    if (userType == "user"){
                                        startActivity(Intent(this@SplashScreenActivity, UserActivity::class.java))
                                        finish()
                                    }else if (userType == "admin"){
                                        startActivity(Intent(this@SplashScreenActivity, AdminActivity::class.java))
                                        finish()
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                }
                            })
                    } else {
                        startActivity(Intent(this@SplashScreenActivity, AdminActivity::class.java))
                        finish()
                    }
                }
                else -> {
                    showProgress()
                }
            }
        }, delayMILLS.toLong())
    }

    companion object {
        private const val delayMILLS = 200
    }
}