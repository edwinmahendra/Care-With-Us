package com.dicoding.picodiploma.carewithus.favorite

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.carewithus.admin.DetailMaterialActivity
import com.dicoding.picodiploma.carewithus.admin.Helper
import com.dicoding.picodiploma.carewithus.admin.ModelMaterial
import com.dicoding.picodiploma.carewithus.databinding.ActivityDetailMaterialBinding
import com.dicoding.picodiploma.carewithus.databinding.ItemRowMaterialFavBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdapterMaterialFav : RecyclerView.Adapter<AdapterMaterialFav.HolderMaterialFavorite> {
    private val context: Context
    private var materialArrayList: ArrayList<ModelMaterial>
    private lateinit var binding: ItemRowMaterialFavBinding

    constructor(context: Context, materialArrayList: ArrayList<ModelMaterial>) {
        this.context = context
        this.materialArrayList = materialArrayList
    }

    inner class HolderMaterialFavorite(itemView: View)  : RecyclerView.ViewHolder(itemView) {
        var titleTv = binding.tvTitle
        var removeFavBtn = binding.removeFav
        var descriptionTv = binding.tvDescription
        var categoryTv = binding.tvCategory
        var dateTv = binding.tvDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMaterialFavorite {
        binding = ItemRowMaterialFavBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderMaterialFavorite(binding.root)


    }

    override fun onBindViewHolder(holder: HolderMaterialFavorite, position: Int) {
        val model = materialArrayList[position]

        loadBookDetails(model, holder)

        holder.itemView.setOnClickListener{
            val intent = Intent(context, DetailMaterialActivity::class.java)
            intent.putExtra("bookId", model.id)
            context.startActivity(intent)
        }

        holder.removeFavBtn.setOnClickListener{
            Helper.removeFromFavorite(context, model.id)
        }

    }

    private fun loadBookDetails(model: ModelMaterial, holder: AdapterMaterialFav.HolderMaterialFavorite) {
        val bookId = model.id
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val categoryId = "${snapshot.child("categoryId").value}"
                    val description = "${snapshot.child("description").value}"
                    val timestamp = "${snapshot.child("timestamp").value}"
                    val title = "${snapshot.child("title").value}"
                    val uid = "${snapshot.child("uid").value}"

                    model.isFavorite = true
                    model.title = title
                    model.description = description
                    model.categoryId = categoryId
                    model.timestamp = timestamp.toLong()
                    model.uid = uid

                    val date = Helper.formatTimeStamp(timestamp.toLong())
                    Helper.loadCategory("$categoryId", holder.categoryTv)
                    holder.titleTv.text = title
                    holder.descriptionTv.text = description
                    holder.dateTv.text = date

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }


    override fun getItemCount(): Int {
        return materialArrayList.size
    }
}