package com.dicoding.picodiploma.carewithus.user

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dicoding.picodiploma.carewithus.R
import com.dicoding.picodiploma.carewithus.admin.CategoryModel
import com.dicoding.picodiploma.carewithus.admin.ModelMaterial
import com.dicoding.picodiploma.carewithus.databinding.ActivityUserBinding
import com.dicoding.picodiploma.carewithus.favorite.FavoriteActivity
import com.dicoding.picodiploma.carewithus.loginactivity.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var categoryArrayList: ArrayList<CategoryModel>
    private lateinit var viewPagerAdapter: ViewPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        checkUser()
        setupWithViewPagerAdapter(binding.viewPager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        supportActionBar?.hide()

        binding.btnFav.setOnClickListener {
            startActivity(Intent(this@UserActivity,FavoriteActivity::class.java))
            finish()
        }

        binding.btnLogout.setOnClickListener {
            val exitDialog = AlertDialog.Builder(this)
            exitDialog.setTitle("Log Out")
            exitDialog.setMessage("Apakah anda ingin Log Out ?")
            exitDialog.setPositiveButton(android.R.string.yes) { _, _ ->
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                auth.signOut()
                startActivity(intent)
                finish()
            }
            exitDialog.setNegativeButton(android.R.string.no) { _, _ ->
                Toast.makeText(applicationContext,
                    android.R.string.no, Toast.LENGTH_SHORT).show()
            }
            exitDialog.show()
        }

    }

    private fun setupWithViewPagerAdapter(viewPager: ViewPager){
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,this)
        categoryArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()

                val modelAll = CategoryModel("01","All",1,"")
                categoryArrayList.add(modelAll)
                viewPagerAdapter.addFragment(
                    MaterialUserFragment.newInstance(
                        "${modelAll.id}",
                        "${modelAll.category}",
                        "${modelAll.uid}"
                    ), modelAll.category
                )
                viewPagerAdapter.notifyDataSetChanged()

                for(ds in snapshot.children){
                    val model = ds.getValue(CategoryModel::class.java)
                    categoryArrayList.add(model!!)
                    viewPagerAdapter.addFragment(
                        MaterialUserFragment.newInstance(
                            "${model.id}",
                            "${model.category}",
                            "${model.uid}"
                        ), model.category
                    )
                    viewPagerAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        viewPager.adapter = viewPagerAdapter
    }

    class ViewPagerAdapter(fm: FragmentManager, behaviour: Int, context: Context): FragmentPagerAdapter(fm, behaviour) {
        private val fragmentList: ArrayList<MaterialUserFragment> = ArrayList()
        private val fragmentTitleList: ArrayList<String> = ArrayList()
        private val context: Context

        init{
            this.context = context
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentTitleList[position]
        }

        public fun addFragment(fragment: MaterialUserFragment, title: String){
            fragmentList.add(fragment)
            fragmentTitleList.add(title)

        }
    }

    private fun checkUser() {
        val firebaseUser = auth.currentUser
        if(firebaseUser == null){
            binding.tvSubtitle.text = getString(R.string.auth_failed)
        }
        else{
            val email = firebaseUser.email
            binding.tvSubtitle.text = email
        }
    }

}