package com.dicoding.picodiploma.carewithus.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.carewithus.R
import com.dicoding.picodiploma.carewithus.databinding.ActivityDetailMaterialBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailMaterialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMaterialBinding
    private var bookId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookId = intent.getStringExtra("materialId").toString()

        loadMaterialDetails()
    }

    private fun loadMaterialDetails() {
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val categoryId = "${snapshot.child("categoryId").value}"
                    val description = "${snapshot.child("description").value}"
                    val timestamp = "${snapshot.child("timestamp").value}"
                    val title = "${snapshot.child("title").value}"
                    val uid = "${snapshot.child("uid").value}"

                    val date = Helper.formatTimeStamp(timestamp.toLong())
                    val date2 = date.toString()
                    Helper.loadCategory(categoryId, binding.categoryTv)

                    binding.descriptionMaterial.text = description
                    binding.materialTitle.text = title
                    binding.dateTv.text = date2

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}