package com.mypetprojectbyme.familyshoppinglist.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mypetprojectbyme.familyshoppinglist.databinding.InputEmailItemBinding

class AlertAdapter : RecyclerView.Adapter<AlertAdapter.UserEmailItemViewHolder>() {

    private val setUserEmail: MutableSet<String> = mutableSetOf()

    inner class UserEmailItemViewHolder(private val binding: InputEmailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userEmailItem: String) {
            binding.userEmail.setText(userEmailItem)
            binding.userEmailInputLayout.setEndIconOnClickListener {
                setUserEmail.remove(userEmailItem)
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserEmailItemViewHolder {
        val binding =
            InputEmailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserEmailItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return setUserEmail.size
    }

    override fun onBindViewHolder(holder: UserEmailItemViewHolder, position: Int) {
        holder.bind(setUserEmail.elementAt(position))
    }

    fun addUserEmail(newUserEmail: String) {
        setUserEmail.add(newUserEmail)
        notifyDataSetChanged()
    }

    fun getFamilyMembers() = setUserEmail
}
