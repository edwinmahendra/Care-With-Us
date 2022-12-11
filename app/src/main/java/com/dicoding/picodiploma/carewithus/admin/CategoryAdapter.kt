package com.dicoding.picodiploma.carewithus.admin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.carewithus.databinding.ItemRowCategoriesBinding
import com.google.firebase.database.FirebaseDatabase

class CategoryAdapter:RecyclerView.Adapter<CategoryAdapter.HolderCategory>, Filterable {
    private val context: Context
    private lateinit var binding: ItemRowCategoriesBinding
    public var categoryArrayList: ArrayList<CategoryModel>
    private var filterList: ArrayList<CategoryModel>
    private var filter: FilterCategory? = null



    constructor(context: Context, categoryArrayList: ArrayList<CategoryModel>): super(){
        this.context = context
        this.categoryArrayList = categoryArrayList
        this.filterList = categoryArrayList
    }

    inner class HolderCategory(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvCategories: TextView = binding.tvItemCategories
        var btnDelete: ImageButton = binding.btnDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        binding = ItemRowCategoriesBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderCategory(binding.root)

    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {
        val model = categoryArrayList[position]
        val id = model.id
        val category = model.category
        val timestamp = model.timestamp

        holder.tvCategories.text = category
        holder.btnDelete.setOnClickListener{
            deleteCategory(model, holder)
        }
        holder.itemView.setOnClickListener{
            val intent = Intent(context, MaterialListAdminActivity::class.java)
            intent.putExtra("categoryId",id)
            intent.putExtra("category",category)
            context.startActivity(intent)
        }

    }

    private fun deleteCategory(model: CategoryModel, holder: HolderCategory) {
        val id = model.id
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Deleted",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                Toast.makeText(context,"Delete Failed ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int {
        return categoryArrayList.size
    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = FilterCategory(filterList, this)
        }
        return  filter as FilterCategory
    }

}