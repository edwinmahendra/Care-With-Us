package com.dicoding.picodiploma.carewithus.admin

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.sql.Timestamp

import java.util.*

class Helper: Application() {
    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        fun formatTimeStamp(timestamp: Long): String {
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis  = timestamp
            return DateFormat.format("dd/MM/yyyy", cal).toString()
        }
        fun loadCategory(categoryId: String, categoryTv: TextView) {
            val ref = FirebaseDatabase.getInstance().getReference("Categories")
            ref.child(categoryId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val category = "${snapshot.child("category").value}"
                        categoryTv.text = category
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }

        fun deleteBook(context: Context, materialId: String, materialTitle: String) {
            val databaseReference = FirebaseDatabase.getInstance().getReference("Books")
            databaseReference.child(materialId)
                .removeValue()
                .addOnSuccessListener {
                    Log.d(TAG, "delete material success")
                    Toast.makeText(context, "Delete Material Success", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {e->
                    Log.d(TAG, "delete material failed due to ${e.message}")
                    Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
                }
        }
    }
}