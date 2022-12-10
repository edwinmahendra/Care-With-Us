package com.dicoding.picodiploma.carewithus.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dicoding.picodiploma.carewithus.R
import com.dicoding.picodiploma.carewithus.databinding.ActivityAddCategoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCategoryBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.addCategory.setOnClickListener{
            validateData()
        }

    }

    private var category = ""
    private fun validateData() {
        category = binding.inputCategory.text.toString().trim()
        if(category.isBlank()) {
            Toast.makeText(this, "Enter Category", Toast.LENGTH_SHORT).show()
        }
        else {
            addCategoryFirebase()
        }
    }

    private fun addCategoryFirebase() {
        val timestamp = System.currentTimeMillis()
        val hashMap = HashMap<String, Any>()

        hashMap["id"] = "$timestamp"
        hashMap["category"] = category
        hashMap["timestamp"] = timestamp

        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {

            }
            .addOnFailureListener{
                e-> Toast.makeText(this, "Failed to add the category due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}