package com.dicoding.picodiploma.carewithus.user

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.carewithus.admin.DetailMaterialActivity
import com.dicoding.picodiploma.carewithus.admin.Helper
import com.dicoding.picodiploma.carewithus.admin.ModelMaterial
import com.dicoding.picodiploma.carewithus.databinding.ItemMaterialUserBinding

class AdapterMaterialUser: RecyclerView.Adapter<AdapterMaterialUser.HolderMaterialUser> {
    private var context: Context
    public var materialArrayList: ArrayList<ModelMaterial>
    private lateinit var binding:ItemMaterialUserBinding
    private val filterList: ArrayList<ModelMaterial>

    constructor(context: Context, materialArrayList: ArrayList<ModelMaterial>): super(){
        this.context = context
        this.materialArrayList = materialArrayList
        this.filterList = materialArrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMaterialUser {
         binding = ItemMaterialUserBinding.inflate(LayoutInflater.from(context))
        return HolderMaterialUser(binding.root)
    }

    override fun onBindViewHolder(holder: HolderMaterialUser, position: Int) {
        val model = materialArrayList[position]
        val bookId = model.id
        val categoryId = model.categoryId
        val title = model.title
        val description = model.description
        val uid = model.uid
        val timestamp = model.timestamp
        val date = Helper.formatTimeStamp(timestamp)

        holder.tvTitle.text = title
        holder.tvDate.text = date
        Helper.loadCategory(categoryId,holder.tvCategory)

        holder.itemView.setOnClickListener{
            val intent = Intent(context, DetailMaterialActivity::class.java)
            intent.putExtra("bookId", bookId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return materialArrayList.size
    }



    inner class HolderMaterialUser(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvTitle = binding.tvTitle
        var tvCategory = binding.tvCategory
        var tvDate = binding.tvDate
    }


}