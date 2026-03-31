package com.superbee.aeronautics.sopform

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.superbee.aeronautics.R
import androidx.core.content.ContextCompat

class MyProfileActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvPhone: TextView
    private lateinit var submissionContainer: LinearLayout
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        tvName = findViewById(R.id.tvName)
        tvPhone = findViewById(R.id.tvPhone)
        submissionContainer = findViewById(R.id.submissionContainer)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            val userId = user.uid

            // Load user profile from Firestore
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("name") ?: "No Name"
                        val phone = document.getString("phone") ?: "No Phone"
                        tvName.text = name
                        tvPhone.text = phone
                    } else {
                        // If no user document, use Firebase Auth data
                        tvName.text = user.displayName ?: "User"
                        tvPhone.text = user.phoneNumber ?: "No Phone"
                    }
                }
                .addOnFailureListener {
                    tvName.text = "Error loading name"
                    tvPhone.text = "Error loading phone"
                }

            // Load user's submissions
            loadSubmissions(userId)
        } else {
            tvName.text = "Not logged in"
            tvPhone.text = ""
        }
    }

    private fun loadSubmissions(userId: String) {
        // Query submissions where userId matches
        db.collection("sop_submissions")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                submissionContainer.removeAllViews()

                if (documents.isEmpty) {
                    val noData = TextView(this)
                    noData.text = "No submissions yet"
                    noData.textSize = 16f
                    noData.setPadding(16, 16, 16, 16)
                    noData.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
                    submissionContainer.addView(noData)
                    return@addOnSuccessListener
                }

                for (document in documents) {
                    val submissionId = document.id

                    val cropName = document.getString("cropName") ?: "Unknown Crop"
                    val farmerName = document.getString("farmerName") ?: "Unknown Farmer"
                    val timestamp = document.getLong("timestamp") ?: 0L
                    val acres = document.getString("acres") ?: ""

                    val date = if (timestamp > 0) {
                        java.text.SimpleDateFormat(
                            "dd MMM yyyy",
                            java.util.Locale.getDefault()
                        ).format(java.util.Date(timestamp))
                    } else {
                        "No Date"
                    }

                    // Inflate submission item layout
                    val submissionView = layoutInflater.inflate(
                        R.layout.item_submission,
                        submissionContainer,
                        false
                    )

                    val tvTitle = submissionView.findViewById<TextView>(R.id.tvSubmissionTitle)
                    val tvDate = submissionView.findViewById<TextView>(R.id.tvSubmissionDate)
                    val btnView = submissionView.findViewById<android.widget.Button>(R.id.btnViewSubmission)

                    // ✅ Updated UI
                    tvTitle.text = "$farmerName - $cropName"

                    tvDate.text = if (acres.isNotEmpty()) {
                        "Date: $date  |  Area: $acres acres"
                    } else {
                        "Date: $date"
                    }

                    btnView.text = "View Mission"

                    btnView.setOnClickListener {
                        val intent = android.content.Intent(this@MyProfileActivity, SubmissionDetailActivity::class.java)
                        intent.putExtra("submissionId", submissionId)
                        startActivity(intent)
                    }

                    submissionContainer.addView(submissionView)
                }
            }
            .addOnFailureListener { e ->
                val errorText = TextView(this)
                errorText.text = "Error loading submissions: ${e.message}"
                errorText.textSize = 14f
                errorText.setPadding(16, 16, 16, 16)
                errorText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
                submissionContainer.addView(errorText)
            }
    }
}