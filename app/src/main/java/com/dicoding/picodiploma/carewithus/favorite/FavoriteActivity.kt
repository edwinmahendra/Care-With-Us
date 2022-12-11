package com.dicoding.picodiploma.carewithus.favorite

import android.content.Intent
import android.graphics.ColorSpace
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.carewithus.R
import com.dicoding.picodiploma.carewithus.admin.ModelMaterial
import com.dicoding.picodiploma.carewithus.databinding.ActivityFavoriteBinding
import com.dicoding.picodiploma.carewithus.user.UserActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var materialArrayList: ArrayList<ModelMaterial>
    private lateinit var adapterMaterialFav: AdapterMaterialFav

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        loadFavoriteBooks()

    }

    override fun onBackPressed() {
        startActivity(Intent(this@FavoriteActivity, UserActivity::class.java))
        finish()
    }

    private fun loadFavoriteBooks() {
        materialArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(auth.uid!!).child("Favorites")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    materialArrayList.clear()
                    for (ds in snapshot.children) {
                        val materialId = "${ds.child("bookId").value}"
                        val model = ModelMaterial()
                        model.id = materialId
                        materialArrayList.add(model)
                    }
                    adapterMaterialFav = AdapterMaterialFav(this@FavoriteActivity, materialArrayList)
                    binding.rvMaterials.adapter = adapterMaterialFav

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}