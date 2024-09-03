package com.example.firebaseexample.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.firebaseexample.databinding.CustomdialogBinding
import com.example.firebaseexample.databinding.FragmentDataBinding
import com.example.firebaseexample.model.EmpData
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class DataFragment : Fragment() {

    private lateinit var binding: FragmentDataBinding
    private lateinit var empData: EmpData
    @Inject lateinit var dbref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDataBinding.inflate(inflater, container, false)
        val data = requireArguments().getParcelable<EmpData>("items")
        if (data != null) {
            empData = data
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateViews()
        binding.Update.setOnClickListener {
            Toast.makeText(requireContext(), "Change Data Here", Toast.LENGTH_LONG).show()
            openDialog()
        }
    }

    private fun updateViews() {
        binding.ageTextView.text = empData.age
        binding.nameTextView.text = empData.name
        binding.salaryTextView.text = empData.salary
    }

    private fun openDialog() {
        val dialogBinding = CustomdialogBinding.inflate(LayoutInflater.from(requireContext()))

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setTitle("EMPLOYEE DETAILS")


        dialogBinding.editText1.setText(empData.name)
        dialogBinding.editText2.setText(empData.age)
        dialogBinding.editText3.setText(empData.salary)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        dialogBinding.buttonSubmit.setOnClickListener {
            val newName = dialogBinding.editText1.text.toString()
            val newAge = dialogBinding.editText2.text.toString()
            val newSalary = dialogBinding.editText3.text.toString()
            updateData(newName, newSalary, newAge)
            alertDialog.dismiss()
        }
    }

    private fun updateData(name: String, salary: String, age: String) {
        val updatedValue = EmpData(empData.id, name, age, salary)
        dbref.child(empData.id!!).setValue(updatedValue).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Data updated successfully", Toast.LENGTH_SHORT).show()
                empData = updatedValue
                updateViews()
            } else {
                Toast.makeText(requireContext(), "Error updating data: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
