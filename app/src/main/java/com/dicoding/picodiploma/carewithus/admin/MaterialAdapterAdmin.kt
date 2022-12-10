package com.dicoding.picodiploma.carewithus.admin

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.carewithus.databinding.ItemMaterialAdminBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class MaterialAdapterAdmin : RecyclerView.Adapter<MaterialAdapterAdmin.HolderMaterialAdmin>, Filterable {

    private var context: Context
    public var materialArrayList: ArrayList<ModelMaterial>
    private lateinit var  binding: ItemMaterialAdminBinding
    private val filterList:ArrayList<ModelMaterial>
    var filter: FilterMaterial? = null

    constructor(context: Context, materialArrayList: ArrayList<ModelMaterial>): super(){
        this.context = context
        this.materialArrayList = materialArrayList
        this.filterList = materialArrayList
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMaterialAdmin {
        binding = ItemMaterialAdminBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderMaterialAdmin(binding.root)
    }

    override fun onBindViewHolder(holder: HolderMaterialAdmin, position: Int) {
        val model = materialArrayList[position]
        val materialId = model.id
        val categoryId = model.categoryId
        val title = model.title
        val description = model.description
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = model.timestamp
        val timestamp2 = DateFormat.format("dd/MM/yyyy", cal).toString()
        val timestamp = timestamp2
        val imgMaterial = binding.imgMaterial
        holder.tvTitle.text = title
        holder.tvDescription.text = description
        holder.tvDate.text = timestamp

        Helper.loadCategory(categoryId = categoryId, holder.tvCategory)

    }

    override fun getItemCount(): Int {
        return materialArrayList.size
    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = FilterMaterial(filterList,this)
        }
        return filter as FilterMaterial
    }
    inner class  HolderMaterialAdmin(itemView: View): RecyclerView.ViewHolder(itemView){
        val imgMaterial = binding.imgMaterial
        val progressBar = binding.progressBar
        val tvTitle = binding.tvTitle
        val tvDescription = binding.tvDescription
        val tvCategory = binding.tvCategory
        val tvDate = binding.tvDate
        val btnMore = binding.btnMore
    }
}