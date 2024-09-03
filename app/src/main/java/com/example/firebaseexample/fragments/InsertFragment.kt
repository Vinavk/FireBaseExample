package com.example.firebaseexample.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firebaseexample.R
import com.example.firebaseexample.databinding.FragmentInsertBinding
import com.example.firebaseexample.model.EmpData
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint

class InsertFragment : Fragment() {

    private lateinit var binding : FragmentInsertBinding
    private lateinit var nameTex : EditText
    private lateinit var ageTex : EditText
    private lateinit var salaryTex : EditText
    private lateinit var saveBtn : Button


    @Inject
    lateinit var dbref : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentInsertBinding.inflate(layoutInflater,container,false)
        nameTex = binding.nameEditText
        ageTex = binding. ageEditText
        salaryTex = binding.salaryEditText
        saveBtn = binding.saveBTn
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveBtn.setOnClickListener {
            saveData()
            if( !nameTex.text.toString().isEmpty() &&
                !ageTex.text.toString().isEmpty() &&
                !salaryTex.text.toString().isEmpty()){
                nameTex.text.clear()
                ageTex.text.clear()
                salaryTex.text.clear()
                findNavController().navigate(R.id.action_insertFragment_to_mainFragment)
            }
        }
    }

    private fun saveData() {
        var name = nameTex.text.toString()
        var age = ageTex.text.toString()
        var salry = salaryTex.text.toString()

        if(name.isEmpty()){
            nameTex.error = "Please enter name"
        }
        if(age.isEmpty()){
            ageTex.error = "Please enter age"
        }
        if(salry.isEmpty()){
            salaryTex.error = "Please enter age"
        }
        val empid = dbref.push().key!!
        val empdata = EmpData(empid,name,age,salry)

        dbref.child(empid).setValue(empdata).addOnCompleteListener{
            Log.d("success123","emdata stored")

        }.addOnFailureListener{
            Log.d("failure123","emdata DIDn't stored")
        }
    }
}