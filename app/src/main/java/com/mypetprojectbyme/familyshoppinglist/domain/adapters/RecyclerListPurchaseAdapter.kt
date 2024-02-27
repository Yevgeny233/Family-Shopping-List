package com.mypetprojectbyme.familyshoppinglist.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.mypetprojectbyme.familyshoppinglist.databinding.PurchaseItemBinding
import com.mypetprojectbyme.familyshoppinglist.domain.model.PurchaseModel

class RecyclerListPurchaseAdapter :
    RecyclerView.Adapter<RecyclerListPurchaseAdapter.ListPurchaseViewHolder>() {

    private var purchaseArray = ArrayList<PurchaseModel>()

    inner class ListPurchaseViewHolder(private val binding: PurchaseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(purchaseModel: PurchaseModel) {
            binding.purchaseEdit.setText(purchaseModel.namePurchase)
            binding.purchaseEditLayout.setEndIconOnClickListener {
                removePurchaseItem(purchaseModel)
            }
            binding.purchaseEdit.doOnTextChanged { text, start, before, count ->
                purchaseArray[adapterPosition].namePurchase = text.toString()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPurchaseViewHolder {
        val binding =
            PurchaseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListPurchaseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return purchaseArray.size
    }

    override fun onBindViewHolder(holder: ListPurchaseViewHolder, position: Int) {
        val purchaseItem = purchaseArray[position]
        holder.bind(purchaseItem)

    }

    fun updatePurchaseList(newPurchaseList: ArrayList<PurchaseModel>) {
        purchaseArray = newPurchaseList
        notifyDataSetChanged()
    }

    fun addPurchaseItem(purchaseItem: PurchaseModel) {
        purchaseArray.add(purchaseItem)
        notifyDataSetChanged()
    }

    private fun removePurchaseItem(purchaseItem: PurchaseModel) {
        purchaseArray.remove(purchaseItem)
        notifyDataSetChanged()
    }

    fun getArrayList(): ArrayList<PurchaseModel> {
        return purchaseArray
    }
}