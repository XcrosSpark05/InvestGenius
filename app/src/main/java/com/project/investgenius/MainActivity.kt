package com.project.investgenius

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var greetingTextView: TextView
    private lateinit var customerNameTextView: TextView
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val database: DatabaseReference by lazy { FirebaseDatabase.getInstance().reference }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.fragmentContainerView)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setupWithNavController(navController)

        // Initialize TextViews
        greetingTextView = findViewById(R.id.textView19)
        customerNameTextView = findViewById(R.id.textView21)

        updateGreeting()
        fetchUserName()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun updateGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        val greeting = when {
            hour in 5..11 -> "Good Morning!"
            hour in 12..16 -> "Good Afternoon!"
            hour in 17..20 -> "Good Evening!"
            else -> "Good Night!"
        }

        greetingTextView.text = greeting
    }

    private fun fetchUserName() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child("user").child(userId).child("firstname")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val firstName = snapshot.getValue(String::class.java) ?: "User"
                        customerNameTextView.text = firstName
                    }

                    override fun onCancelled(error: DatabaseError) {
                        customerNameTextView.text = "User"
                    }
                })
        } else {
            customerNameTextView.text = "User"
        }
    }
}
