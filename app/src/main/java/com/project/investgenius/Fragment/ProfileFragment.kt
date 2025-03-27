package com.project.investgenius.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.project.investgenius.login_activity
import com.project.investgenius.databinding.FragmentProfileBinding
import com.project.investgenius.model.UserModel

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

        binding.button11.setOnClickListener {
            updateUserData()
        }

        // Fetch user data from Firebase
        retrieveUserData()

        // Logout functionality with confirmation dialog
        binding.textView28.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { _, _ ->
            logoutUser()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun logoutUser() {
        auth.signOut()
        Toast.makeText(requireContext(), "Logged Out Successfully!", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), login_activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun updateUserData() {
        val updatefirstName = binding.editTextText2.text.toString()
        val updatelastname = binding.editTextText3.text.toString()
        val updatenumber = binding.editTextText10.text.toString()
        val updateemail = binding.editTextTextEmailAddress.text.toString()

        val updates = mapOf(
            "firstname" to updatefirstName,
            "lastname" to updatelastname,
            "number" to updatenumber,
            "email" to updateemail
        )

        userReference.child(auth.currentUser!!.uid).updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile Updated 😊", Toast.LENGTH_SHORT).show()
                auth.currentUser?.updateEmail(updateemail)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failure 😒", Toast.LENGTH_SHORT).show()
            }
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
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
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
