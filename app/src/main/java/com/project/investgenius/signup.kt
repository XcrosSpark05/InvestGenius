package com.project.investgenius

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.project.investgenius.databinding.ActivityCreateAccountBinding
import com.project.investgenius.databinding.ActivitySignupBinding

class signup : AppCompatActivity() {
    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

    binding.button6.setOnClickListener{
        val email = binding.editTextTextEmailAddress2.text.toString()
        val fullName = binding.editTextText.text.toString()
        val lastName = binding.editTextText2.text.toString()
        val phoneNumber = binding.editTextPhone.text.toString()
        val password = binding.editTextTextPassword2.text.toString()
        val confrimPassword = binding.editTextTextPassword3.text.toString()

        if (email.isEmpty()||fullName.isEmpty()||lastName.isEmpty()||phoneNumber.isEmpty()||password.isEmpty()||confrimPassword.isEmpty()){
            Toast.makeText(this, "Please Fill all-Details", Toast.LENGTH_SHORT).show()
        }else if (password!=confrimPassword){
            Toast.makeText(this, "Password not matching!!!", Toast.LENGTH_SHORT).show()
        }else{
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
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
}