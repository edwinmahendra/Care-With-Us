package com.dicoding.picodiploma.carewithus.admin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.dicoding.picodiploma.carewithus.R
import com.dicoding.picodiploma.carewithus.databinding.ActivityAddMaterialBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class AddMaterialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMaterialBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var categoryArrayList: ArrayList<CategoryModel>
    private var pdfUri: Uri? = null
    private val TAG = "material_add_tags"
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityAddMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        loadMaterialCategories()

        supportActionBar?.title = "Add Material"

        binding.categoryMaterial.setOnClickListener {
            categoryPickDialog()
        }

        binding.addCategory.setOnClickListener{
            validateData()
        }

    }

    private var title = ""
    private var description = ""
    private var category = ""

    private fun validateData() {
        title = binding.titleMaterial.text.toString().trim()
        description = binding.descriptionMaterial.text.toString().trim()
        category = binding.categoryMaterial.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show()

        } else if (description.isEmpty()) {
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show()
        } else if (category.isEmpty()) {
            Toast.makeText(this, "Pick Category", Toast.LENGTH_SHORT).show()
        } else {
            uploadPdfIntoDb()
        }
    }

    private fun uploadPdfIntoDb() {
        val timestamp = System.currentTimeMillis()
        val uid = auth.uid
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["uid"] = "$uid"
        hashMap["id"] = "$timestamp"
        hashMap["title"] = "$title"
        hashMap["description"] = "$description"
        hashMap["categoryId"] = "$selectedCategoryId"
        hashMap["timestamp"] = timestamp

        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()
                pdfUri = null
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed upload due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadMaterialCategories() {
        Log.d(TAG, "Loadmaterialcategories")
        categoryArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                for (i in snapshot.children) {
                    val model = i.getValue(CategoryModel::class.java)
                    categoryArrayList.add(model!!)
                    Log.d(TAG, "onDataChange: ${model.category}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private var selectedCategoryId = ""
    private var selectedCategoryTitle = ""

    private fun categoryPickDialog() {
        Log.d(TAG, "showing material dialog")
        val categoriesArray = arrayOfNulls<String>(categoryArrayList.size)
        for (i in categoryArrayList.indices) {
            categoriesArray[i] = categoryArrayList[i].category
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Category")
            .setItems(categoriesArray) {dialog, which ->
                selectedCategoryTitle = categoryArrayList[which].category
                selectedCategoryId = categoryArrayList[which].id

                binding.categoryMaterial.text = selectedCategoryTitle

                Log.d(TAG, "Selected Category ID: $selectedCategoryId" )
                Log.d(TAG, "Selected Category Title: $selectedCategoryTitle" )
            }
            .show()
    }

}
