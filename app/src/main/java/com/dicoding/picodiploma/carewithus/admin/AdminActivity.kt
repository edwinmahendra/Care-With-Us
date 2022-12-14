package com.dicoding.picodiploma.carewithus.admin

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.CalendarView
import android.widget.Toast
import com.dicoding.picodiploma.carewithus.R
import com.dicoding.picodiploma.carewithus.databinding.ActivityAdminBinding
import com.dicoding.picodiploma.carewithus.favorite.FavoriteActivity
import com.dicoding.picodiploma.carewithus.loginactivity.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var categoryArrayList: ArrayList<CategoryModel>
    private lateinit var adapterCategory: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadCategories()

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        checkUser()

        binding.btnLogout.setOnClickListener {
            val exitDialog = AlertDialog.Builder(this)
            exitDialog.setTitle("Log Out")
            exitDialog.setMessage("Apakah anda ingin Log Out ?")
            exitDialog.setPositiveButton(android.R.string.yes) { _, _ ->
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                auth.signOut()
                startActivity(intent)
                finish()
            }
            exitDialog.setNegativeButton(android.R.string.no) { _, _ ->
                Toast.makeText(applicationContext,
                    android.R.string.no, Toast.LENGTH_SHORT).show()
            }
            exitDialog.show()
        }



        binding.searchBar.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try{
                    adapterCategory.filter.filter(s)
                }
                catch (e: Exception){

                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.buttonAddCategory.setOnClickListener{
            startActivity(Intent(this@AdminActivity, AddCategoryActivity::class.java))
        }

        binding.buttonAddMaterial.setOnClickListener{
            startActivity(Intent(this@AdminActivity, AddMaterialActivity::class.java))
        }

    }

    private fun loadCategories() {
        categoryArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                categoryArrayList.clear()
                for (ds in snapshot.children){
                    val model = ds.getValue(CategoryModel::class.java)
                    categoryArrayList.add(model!!)
                }
                adapterCategory = CategoryAdapter(this@AdminActivity, categoryArrayList)
                binding.rvCategories.adapter = adapterCategory
            }
            override fun onCancelled(error: DatabaseError){
            }
        })

    }

    private fun checkUser() {
        val firebaseUser = auth.currentUser
        if(firebaseUser == null) {
            startActivity(Intent(this@AdminActivity, LoginActivity::class.java))
            finish()
        } else {
            val email = firebaseUser.email
//            binding.emailAdmin.text = email
        }
    }
}