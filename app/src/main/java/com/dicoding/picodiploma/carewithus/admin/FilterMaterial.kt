package com.dicoding.picodiploma.carewithus.admin

import android.widget.Filter

class FilterMaterial: Filter {
    var filterList: ArrayList<ModelMaterial>
    var adapterMaterialAdmin: MaterialAdapterAdmin

    constructor(filterList: ArrayList<ModelMaterial>, adapterMaterialAdmin: MaterialAdapterAdmin): super(){
        this.filterList = filterList
        this.adapterMaterialAdmin = adapterMaterialAdmin
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint: CharSequence? = constraint
        val result = FilterResults()
        if(constraint != null && constraint.isNotEmpty()) {
            constraint = constraint.toString().lowercase()
            val filteredModels: ArrayList<ModelMaterial> = ArrayList()
            for (i in 0 until filterList.size){
                if(filterList[i].title.lowercase().contains(constraint)){
                    filteredModels.add(filterList[i])
                }
            }
            result.count = filteredModels.size
            result.values = filteredModels
        } else {
            result.count = filterList.size
            result.values = filterList
        }
        return result
    }

    override fun publishResults(constraint: CharSequence, results : FilterResults) {
        adapterMaterialAdmin.materialArrayList = results.values as ArrayList<ModelMaterial>
        adapterMaterialAdmin.notifyDataSetChanged()
    }

}