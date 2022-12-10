package com.dicoding.picodiploma.carewithus.admin

import android.graphics.ColorSpace
import android.widget.Filter


class FilterCategory: Filter {
    private var filterList: ArrayList<CategoryModel>
    private var adapterCategory: CategoryAdapter

    constructor(filterList:ArrayList<CategoryModel>, adapterCategory: CategoryAdapter): super(){
        this.filterList = filterList
        this.adapterCategory = adapterCategory
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val result = FilterResults()

        if (constraint != null && constraint.isNotEmpty()) {
            constraint = constraint.toString().uppercase()
            val filteredModels:ArrayList<CategoryModel> = ArrayList()
            for(i in 0 until filterList.size){
                if (filterList[i].category.uppercase().contains(constraint)){
                    filteredModels.add(filterList[i])
                }
            }
            result.count = filteredModels.size
            result.values = filteredModels
        }else{
            result.count = filterList.size
            result.values = filterList
        }
        return result
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapterCategory.categoryArrayList = results.values as ArrayList<CategoryModel>
        adapterCategory.notifyDataSetChanged()
    }


}