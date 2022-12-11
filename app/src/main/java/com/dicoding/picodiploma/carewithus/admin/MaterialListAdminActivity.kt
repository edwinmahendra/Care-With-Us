package com.dicoding.picodiploma.carewithus.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.dicoding.picodiploma.carewithus.R
import com.dicoding.picodiploma.carewithus.databinding.ActivityAdminBinding
import com.dicoding.picodiploma.carewithus.databinding.ActivityMaterialListAdminBinding
import com.dicoding.picodiploma.carewithus.favorite.FavoriteActivity
import com.dicoding.picodiploma.carewithus.loginactivity.LoginActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class MaterialListAdminActivity : AppCompatActivity() {
    private lateinit var materialArrayList: ArrayList<ModelMaterial>
    private lateinit var binding: ActivityMaterialListAdminBinding
    private lateinit var adapterMaterial: MaterialAdapterAdmin
    private var categoryId = ""
    private var category = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaterialListAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        categoryId = intent.getStringExtra("categoryId")!!
        category = intent.getStringExtra("category")!!

        binding.tvSubtitle.text = category
        loadMateriallist()
        supportActionBar?.hide()


        binding.backBtn.setOnClickListener {
            startActivity(Intent(this@MaterialListAdminActivity, AdminActivity::class.java))
            finish()
        }

        binding.searchBar.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try{
                    adapterMaterial.filter!!.filter(s)
                }
                catch (e: Exception){

                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun loadMateriallist() {
        materialArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.orderByChild("categoryId").equalTo(categoryId)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    materialArrayList.clear()
                    for(ds in snapshot.children){
                        val model = ds.getValue(ModelMaterial::class.java)
                        if(model != null) {
                            materialArrayList.add(model)
                        }
                    }
                    adapterMaterial = MaterialAdapterAdmin(this@MaterialListAdminActivity,materialArrayList)
                    binding.rvMaterials.adapter = adapterMaterial
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}