package com.project.investgenius

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.project.investgenius.databinding.ActivityCreateAccountBinding
import com.project.investgenius.databinding.ActivitySignupBinding
import com.project.investgenius.model.UserModel

class signup : AppCompatActivity() {
    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var email : String
    private lateinit var fullname : String
    private lateinit var lastname : String
    private lateinit var pass : String
    private lateinit var Confpass : String
    private lateinit var database : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        //initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        //initialize Firebase database
        database = Firebase.database.reference


    binding.button6.setOnClickListener{
        val email = binding.emailregnametext.text.toString().trim()
        val firstName = binding.firstnametext.text.toString().trim()
        val lastName = binding.lastnametext.text.toString().trim()
        val phoneNumber = binding.phoneregnametext.text.toString().trim()
        val password = binding.passregnametext.text.toString().trim()
        val confrimPassword = binding.confrimpasswordregnametext.text.toString().trim()

        if (email.isEmpty()||firstName.isEmpty()||lastName.isEmpty()||phoneNumber.isEmpty()||password.isEmpty()||confrimPassword.isEmpty()){
            Toast.makeText(this, "Please Fill all-Details", Toast.LENGTH_SHORT).show()
        }else if (password!=confrimPassword){
            Toast.makeText(this, "Password not matching!!!", Toast.LENGTH_SHORT).show()
        }else{
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        saveUserData()
                        val intent = Intent(this,otp::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this, "Login Failed : ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun saveUserData() {
        val email = binding.emailregnametext.text.toString().trim()
        val firstName = binding.firstnametext.text.toString().trim()
        val lastName = binding.lastnametext.text.toString().trim()
        val phoneNumber = binding.phoneregnametext.text.toString().trim()
        val password = binding.passregnametext.text.toString().trim()
        val confrimPassword = binding.confrimpasswordregnametext.text.toString().trim()

        val user = UserModel(email, firstName, lastName, phoneNumber, password, confrimPassword)

        // Get the current user ID safely
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            val userId = firebaseUser.uid
            database.child("user").child(userId).setValue(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "User data saved successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show()
        }
    }



}