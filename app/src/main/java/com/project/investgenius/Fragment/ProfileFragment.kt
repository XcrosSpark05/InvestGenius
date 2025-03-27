package com.project.investgenius.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.project.investgenius.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        userReference = database.reference.child("user")

        // Disable text fields & hide save button initially
        binding.editTextText2.isEnabled = false
        binding.editTextText3.isEnabled = false
        binding.editTextText10.isEnabled = false
        binding.editTextTextEmailAddress.isEnabled = false
        binding.button11.isVisible = false

        var isEnable = false
        binding.button9.setOnClickListener {
            isEnable = !isEnable
            binding.editTextText2.isEnabled = isEnable
            binding.editTextText3.isEnabled = isEnable
            binding.editTextText10.isEnabled = isEnable
            binding.editTextTextEmailAddress.isEnabled = isEnable
            binding.button11.isVisible = isEnable
            if (isEnable) {
                binding.editTextText2.requestFocus()
            }
        }

        // Fetch user data from Firebase
        retrieveUserData()
    }

    private fun retrieveUserData() {
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            val userRef = userReference.child(currentUserUid)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userFirstName = snapshot.child("firstname").getValue(String::class.java) ?: "N/A"
                        val userLastName = snapshot.child("lastname").getValue(String::class.java) ?: "N/A"
                        val number = snapshot.child("number").getValue(String::class.java) ?: "N/A"
                        val email = snapshot.child("email").getValue(String::class.java) ?: "N/A"

                        setDataToTextView(userFirstName, userLastName, number, email)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error properly
                }
            })
        }
    }

    private fun setDataToTextView(userFirstName: String, userLastName: String, number: String, email: String) {
        binding.editTextText2.setText(userFirstName)
        binding.editTextText3.setText(userLastName)
        binding.editTextText10.setText(number)
        binding.editTextTextEmailAddress.setText(email)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}
