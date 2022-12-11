package com.dicoding.picodiploma.carewithus.user

import android.widget.Adapter
import android.widget.Filter
import com.dicoding.picodiploma.carewithus.admin.ModelMaterial

class FilterMaterialUser: Filter {
    var filterList: ArrayList<ModelMaterial>
    var adapterMaterialUser: AdapterMaterialUser

    constructor(filterList: ArrayList<ModelMaterial>, adapterMaterialUser: AdapterMaterialUser): super() {
        this.filterList = filterList
        this.adapterMaterialUser = adapterMaterialUser
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint: CharSequence? = constraint
        val results = FilterResults()
        if(constraint != null && constraint.isNotEmpty()){
            constraint = constraint.toString().uppercase()
            val filteredModels = ArrayList<ModelMaterial>()
            for (i in 0 until filterList.size){
                if (filterList[i].title.uppercase().contains(constraint)){
                    filteredModels.add(filterList[i])
                }
            }
            results.count = filteredModels.size
            results.values = filteredModels
        }
        else{
            results.count = filterList.size
            results.values = filterList
        }
        return  results
    }

    override fun publishResults(constraint: CharSequence, results: FilterResults) {
        adapterMaterialUser.materialArrayList = results.values as ArrayList<ModelMaterial>
        adapterMaterialUser.notifyDataSetChanged()
    }


}