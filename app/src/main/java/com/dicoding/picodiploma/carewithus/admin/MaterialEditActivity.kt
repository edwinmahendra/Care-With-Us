package com.dicoding.picodiploma.carewithus.admin

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.dicoding.picodiploma.carewithus.R
import com.dicoding.picodiploma.carewithus.databinding.ActivityAddMaterialBinding
import com.dicoding.picodiploma.carewithus.databinding.ActivityMaterialEditBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MaterialEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMaterialEditBinding
    private var bookId = ""
    private lateinit var categoryTitleArrayList:ArrayList<String>
    private lateinit var categoryIdArrayList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaterialEditBinding.inflate(layoutInflater)

        setContentView(binding.root)

        bookId = intent.getStringExtra("materialId").toString()

        loadCategories()

        binding.categoryMaterial.setOnClickListener {
            categoryDialog()
        }

        binding.addCategory.setOnClickListener{
            validateData()
        }

        loadMaterialInfo()
    }

    private var title = ""
    private var description = ""

    private fun validateData() {
        title = binding.titleMaterial.text.toString().trim()
        description = binding.descriptionMaterial.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show()
        }
        else if (description.isEmpty()) {
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show()
        }
        else if (selectedCategoryId.isEmpty()) {
            Toast.makeText(this, "Pick Category", Toast.LENGTH_SHORT).show()
        }
        else {
            updateMaterial()
        }
    }

    private fun loadMaterialInfo() {
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    selectedCategoryId = snapshot.child("categoryId").value.toString()
                    val description = snapshot.child("description").value.toString()
                    val title = snapshot.child("title").value.toString()

                    binding.titleMaterial.setText(title)
                    binding.descriptionMaterial.setText(description)

                    val refMaterialCategory = FirebaseDatabase.getInstance().getReference("Categories")
                    refMaterialCategory.child(selectedCategoryId)
                        .addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val category = snapshot.child("category").value
                                binding.categoryMaterial.text = category.toString()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }
    private fun updateMaterial() {
        val hashMap = HashMap<String, Any>()
        hashMap["title"] = "$title"
        hashMap["description"] = "$description"
        hashMap["categoryId"] = "$selectedCategoryId"

        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(bookId)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "update pdf success")
                Toast.makeText(this, "update pdf success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e ->
                Log.d(TAG, "update pdf: error due to ${e.message}")
                Toast.makeText(this, "Faied to update", Toast.LENGTH_SHORT).show()
            }

    }

    private var selectedCategoryId = ""
    private var selectedCategoryTitle = ""

    private fun categoryDialog() {
        val categoriesArray = arrayOfNulls<String>(categoryTitleArrayList.size)
        for (i in categoryTitleArrayList.indices) {
            categoriesArray[i] = categoryTitleArrayList[i]

        }

        val builder =  AlertDialog.Builder(this)
        builder.setTitle("Choose Category")
            .setItems(categoriesArray) {dialog, position ->
                selectedCategoryId = categoryIdArrayList[position]
                selectedCategoryTitle = categoryTitleArrayList[position]

                binding.categoryMaterial.text = selectedCategoryTitle

            }
            .show()
    }

    private fun loadCategories() {
        Log.d(TAG, "loading categories")
        categoryTitleArrayList = ArrayList()
        categoryIdArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryIdArrayList.clear()
                categoryTitleArrayList.clear()

                for (ds in snapshot.children) {
                    val id = "${ds.child("id").value}"
                    val category = "${ds.child("category").value}"

                    categoryIdArrayList.add(id)
                    categoryTitleArrayList.add(category)


                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}