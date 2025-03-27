package com.project.investgenius

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.project.investgenius.databinding.ActivityCreateAccountBinding
import com.project.investgenius.databinding.ActivityStartBinding

class create_account : AppCompatActivity() {
    private val binding: ActivityCreateAccountBinding by lazy {
        ActivityCreateAccountBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)

        binding.button5.setOnClickListener{
            signInGoogle()
        }

        binding.textView13.setOnClickListener{
            val intent = Intent(this,login_activity::class.java)
            startActivity(intent)
        }

        binding.button4.setOnClickListener{
            val intent = Intent(this,signup::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account!=null){
                updateUI(account)
            }
        }else{
            Toast.makeText(this, "Failed!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val firebaseUser = auth.currentUser
                val userReference = FirebaseDatabase.getInstance().reference.child("user").child(firebaseUser!!.uid)

                // Check if user data already exists
                userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()) {
                            // If user does not exist, store Google Sign-In data in Firebase
                            val userMap = hashMapOf(
                                "firstname" to (account.givenName ?: ""),
                                "lastname" to (account.familyName ?: ""),
                                "email" to (account.email ?: ""),
                                "number" to "" // Google doesn't provide phone number
                            )

                            userReference.setValue(userMap)
                                .addOnSuccessListener {
                                    Log.d("GoogleSignIn", "User data stored successfully")
                                }
                                .addOnFailureListener {
                                    Log.e("GoogleSignIn", "Failed to store user data: ${it.message}")
                                }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("FirebaseError", "Database error: ${error.message}")
                    }
                })

                // Redirect to the main screen
                val intent = Intent(this, allsetscreen::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

}