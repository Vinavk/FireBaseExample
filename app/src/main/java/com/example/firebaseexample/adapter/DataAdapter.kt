package com.example.firebaseexample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseexample.databinding.ItemListBinding
import com.example.firebaseexample.interfaces.ClickListener
import com.example.firebaseexample.interfaces.DeletListener
import com.example.firebaseexample.model.EmpData

class DataAdapter(private val list: List<EmpData>, var clickListener: ClickListener,var deletListener: DeletListener) :
    RecyclerView.Adapter<DataAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding: ItemListBinding =
            ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = list[position]
        holder.binding.nametext.text = item.name

        holder.binding.root.setOnClickListener{
            clickListener.onclick(item)
        }
        holder.binding.deleteIcon.setOnClickListener{
            deletListener.delete(item)
        }

    }
}