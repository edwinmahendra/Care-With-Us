package com.dicoding.picodiploma.carewithus.admin

import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dicoding.picodiploma.carewithus.R
import com.dicoding.picodiploma.carewithus.databinding.ActivityDetailMaterialBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.oAuthCredential
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailMaterialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMaterialBinding
    private var bookId = ""
    private var inMyFav = false
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookId = intent.getStringExtra("bookId").toString()

        loadMaterialDetails()

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser!=null) {
            checkIsFav()
        }
        binding.fabFav.setOnClickListener{
            if (auth.currentUser == null) {
                Toast.makeText(this, "You're not logged in", Toast.LENGTH_SHORT).show()

            } else {
                if (inMyFav) {
                    removeFromFavorite()
                } else {
                    addToFavorite()
                }
            }
        }
    }

    private fun checkIsFav() {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(auth.uid!!).child("Favorites").child(bookId)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    inMyFav = snapshot.exists()
                    if (inMyFav) {
                        binding.fabFav.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@DetailMaterialActivity, R.drawable.ic_baseline_favorite_24
                            )
                        )
                    } else {
                        binding.fabFav.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@DetailMaterialActivity, R.drawable.ic_baseline_favorite_border_24
                            )
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun addToFavorite() {
        val timestamp = System.currentTimeMillis()
        val hashMap = HashMap<String, Any>()

        hashMap["bookId"] = bookId
        hashMap["timestamp"] = timestamp

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(auth.uid!!).child("Favorites").child(bookId)
            .setValue(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "addToFavorite")

            }
            .addOnFailureListener{e ->
                Log.d(TAG, "failed to add to favorite due to ${e.message}")
                Toast.makeText(this, "Failed to add to fav", Toast.LENGTH_SHORT).show()

            }
    }

    private fun removeFromFavorite() {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(auth.uid!!).child("Favorites").child(bookId)
            .removeValue()
            .addOnSuccessListener {
                Log.d(TAG, "remove From Favorite")
                Toast.makeText(this, "Success to Remove from fav", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e ->
                Log.d(TAG, "failed to remove from favorite due to ${e.message}")
                Toast.makeText(this, "Failed to remove from fav", Toast.LENGTH_SHORT).show()
            }
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