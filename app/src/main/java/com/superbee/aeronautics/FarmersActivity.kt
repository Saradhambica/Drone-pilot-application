package com.superbee.aeronautics

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.superbee.aeronautics.databinding.ActivityFarmersBinding
import com.superbee.aeronautics.sopform.SopFormActivity
import android.view.Menu
import android.view.MenuItem
import com.superbee.aeronautics.sopform.MyProfileActivity

class FarmersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFarmersBinding
    private lateinit var adapter: FarmersAdapter
    private lateinit var db: FirebaseFirestore
    private var listener: ListenerRegistration? = null

    private val farmerViewModel: FarmerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFarmersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Projects"

        db = FirebaseFirestore.getInstance()
        setupRecycler()
        listenToFarmers()

        // Add Farmer button (existing)
        binding.addFarmerBtn.setOnClickListener {
            startActivity(Intent(this, AddFarmerActivity::class.java))
        }
    }

    private fun setupRecycler() {
        adapter = FarmersAdapter(
            emptyList(),
            onEdit = { farmer ->
                EditFarmerDialogFragment.newInstance(farmer)
                    .show(supportFragmentManager, "EditFarmer")
            },
            onDelete = { farmer -> deleteFarmer(farmer) },
            onSelect = { farmer ->
                farmerViewModel.selectFarmer(farmer)

                val intent = Intent(this, SopFormActivity::class.java)
                intent.putExtra("farmerId", farmer.id)
                intent.putExtra("farmerName", farmer.Name)
                startActivity(intent)

            }
        )

        binding.farmersRecycler.layoutManager = LinearLayoutManager(this)
        binding.farmersRecycler.setHasFixedSize(true)
        binding.farmersRecycler.adapter = adapter
    }

    private fun listenToFarmers() {
        listener = db.collection("farmers")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                val farmers = snapshot?.documents?.mapNotNull { doc ->
                    Farmer(
                        id = doc.id,
                        Name = doc.getString("Name") ?: "",
                        Phone = doc.getString("Phone") ?: "",
                        State = doc.getString("State") ?: "",
                        District = doc.getString("District") ?: "",
                        Village = doc.getString("Village") ?: "",
                        Pincode = doc.getString("Pincode") ?: ""
                    )
                } ?: emptyList()

                adapter.updateList(farmers)
            }
    }

    private fun deleteFarmer(farmer: Farmer) {
        db.collection("farmers").document(farmer.id).delete()
    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.remove()
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_profile -> {
                startActivity(Intent(this, MyProfileActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
