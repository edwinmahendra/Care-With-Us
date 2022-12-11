package com.dicoding.picodiploma.carewithus.user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.dicoding.picodiploma.carewithus.R
import com.dicoding.picodiploma.carewithus.admin.ModelMaterial
import com.dicoding.picodiploma.carewithus.databinding.FragmentMaterialUserBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MaterialUserFragment : Fragment {
    private lateinit var binding: FragmentMaterialUserBinding

    public companion object{
        private const val  TAG = "MATERIAL_USER_TAG"

        public fun newInstance(categoryId: String, category: String, uid: String): MaterialUserFragment {
            val fragment = MaterialUserFragment()
            val args = Bundle()
            args.putString("categoryId", categoryId)
            args.putString("category", category)
            args.putString("uid",uid)
            fragment.arguments = args
            return fragment
        }
    }

    private var categoryId = ""
    private var category = ""
    private var uid = ""

    private lateinit var materialArrayList: ArrayList<ModelMaterial>
    private lateinit var adapterMaterialUser: AdapterMaterialUser

    constructor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if(args != null){
            categoryId = args.getString("categoryId")!!
            category = args.getString("category")!!
            uid = args.getString("uid")!!
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentMaterialUserBinding.inflate(LayoutInflater.from(context), container, false)

        if(category ==  "All" ){
            loadAllMaterials()
        }
        else{
            loadCategorizedMaterials()
        }

        binding.searchBar.addTextChangedListener {object :TextWatcher  {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try{
                    adapterMaterialUser.filter!!.filter(s)
                }
                catch (e: Exception){

                }
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

        } }
        return binding.root
    }

    private fun loadAllMaterials() {
        materialArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                materialArrayList.clear()
                for (ds in snapshot.children){
                    val model = ds.getValue(ModelMaterial::class.java)
                    materialArrayList.add(model!!)
                }
                adapterMaterialUser = AdapterMaterialUser(context!!, materialArrayList)
                binding.rvBooks.adapter = adapterMaterialUser
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun loadCategorizedMaterials() {
        materialArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.orderByChild("categoryId").equalTo(categoryId)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot){
                    materialArrayList.clear()
                    for (ds in snapshot.children){
                        val model = ds.getValue(ModelMaterial::class.java)
                        materialArrayList.add(model!!)
                    }
                    adapterMaterialUser = AdapterMaterialUser(context!!, materialArrayList)
                    binding.rvBooks.adapter = adapterMaterialUser
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

}