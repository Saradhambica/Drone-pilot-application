package com.superbee.aeronautics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.superbee.aeronautics.databinding.ItemFarmerBinding

class FarmersAdapter(
    private var farmers: List<Farmer>,
    private val onEdit: (Farmer) -> Unit,
    private val onDelete: (Farmer) -> Unit,
    private val onSelect: (Farmer) -> Unit
) : RecyclerView.Adapter<FarmersAdapter.FarmerViewHolder>() {

    inner class FarmerViewHolder(val binding: ItemFarmerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FarmerViewHolder {
        val binding = ItemFarmerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FarmerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FarmerViewHolder, position: Int) {
        val farmer = farmers[position]

        holder.binding.tvName.text = farmer.Name
        holder.binding.tvPhone.text = "Phone: ${farmer.Phone}"
        holder.binding.tvVillage.text = "Village: ${farmer.Village}"
        holder.binding.tvDistrict.text = "District: ${farmer.District}"

        holder.binding.btnEdit.setOnClickListener { onEdit(farmer) }
        holder.binding.btnDelete.setOnClickListener { onDelete(farmer) }

        holder.binding.root.setOnClickListener {
            onSelect(farmer)
        }
    }

    override fun getItemCount() = farmers.size

    fun updateList(newList: List<Farmer>) {
        farmers = newList
        notifyDataSetChanged()
    }
}