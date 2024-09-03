package com.example.firebaseexample.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseexample.R
import com.example.firebaseexample.adapter.DataAdapter
import com.example.firebaseexample.databinding.FragmentRetriveBinding
import com.example.firebaseexample.interfaces.ClickListener
import com.example.firebaseexample.interfaces.DeletListener
import com.example.firebaseexample.model.EmpData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class RetriveFragment : Fragment(), ClickListener, DeletListener {

    private lateinit var binding: FragmentRetriveBinding
    private lateinit var recyclerView: RecyclerView
    @Inject
    lateinit var dbref: DatabaseReference
    private var emplist: ArrayList<EmpData> = ArrayList()
    private lateinit var adapter: DataAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRetriveBinding.inflate(inflater, container, false)
        recyclerView = binding.recylerview
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = DataAdapter(emplist, this, this)
        recyclerView.adapter = adapter


        retrivedatas()

        return binding.root
    }

    private fun retrivedatas() {
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                emplist.clear()
                if (snapshot.exists()) {
                    for (empvalue in snapshot.children) {
                        val emp = empvalue.getValue(EmpData::class.java)
                        emp?.let {
                            emplist.add(it)
                        }
                    }
                }

                adapter.notifyDataSetChanged()

                if (emplist.isEmpty()) {
                    binding.recylerview.visibility = View.GONE
                    binding.loadingText.visibility = View.GONE
                } else {
                    binding.recylerview.visibility = View.VISIBLE
                    binding.loadingText.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("RetriveFragment", "Database error: ${error.message}")
                binding.recylerview.visibility = View.GONE
                binding.loadingText.visibility = View.VISIBLE
            }
        })
    }

    override fun onclick(item: EmpData) {
        findNavController().navigate(
            R.id.action_retriveFragment_to_dataFragment,
            bundleOf("items" to item)
        )
    }

    override fun delete(item: EmpData) {
        emplist.removeAll { it.id == item.id }
        adapter.notifyDataSetChanged()

        item.id?.let { id ->
            dbref.child(id).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("RetriveFragment", "Item removed from database successfully.")
                } else {
                    Log.e("RetriveFragment", "Failed to remove item from database: ${task.exception?.message}")
                }
            }
        }
    }
}
