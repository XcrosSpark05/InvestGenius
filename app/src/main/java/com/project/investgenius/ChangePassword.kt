package com.project.investgenius

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ChangePassword : AppCompatActivity() {
    private lateinit var oldPassword: EditText
    private lateinit var newPassword: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var saveButton: Button
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        oldPassword = findViewById(R.id.editTextOldPassword)
        newPassword = findViewById(R.id.editTextNewPassword)
        confirmPassword = findViewById(R.id.editTextConfirmPassword)
        saveButton = findViewById(R.id.buttonSavePassword)

        saveButton.setOnClickListener {
            changePassword()
        }
    }

    private fun changePassword() {
        val oldPass = oldPassword.text.toString().trim()
        val newPass = newPassword.text.toString().trim()
        val confirmPass = confirmPassword.text.toString().trim()

        if (newPass.isEmpty() || confirmPass.isEmpty() || oldPass.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPass != confirmPass) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            return
        }

        val user = auth.currentUser
        if (user != null && user.email != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, oldPass)

            user.reauthenticate(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.updatePassword(newPass).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            // ✅ Update both pass and confpass fields correctly
                            val databaseReference = FirebaseDatabase.getInstance().getReference("user").child(user.uid)
                            val updateMap = mapOf(
                                "pass" to newPass,
                                "confpass" to newPass // Ensure confpass is also updated
                            )

                            databaseReference.updateChildren(updateMap)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Password Updated Successfully!", Toast.LENGTH_LONG).show()
                                    finish() // Close activity after success
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Failed to update in database: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, "Failed to update password!", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Incorrect Old Password!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show()
        }
    }


}
