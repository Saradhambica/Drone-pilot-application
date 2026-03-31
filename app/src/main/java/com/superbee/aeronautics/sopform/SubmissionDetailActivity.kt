package com.superbee.aeronautics.sopform

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.superbee.aeronautics.R
import java.text.SimpleDateFormat
import java.util.*

class SubmissionDetailActivity : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submission_detail)

        tableLayout = findViewById(R.id.tableLayoutDetails)

        val submissionId = intent.getStringExtra("submissionId")
        if (submissionId == null) {
            Toast.makeText(this, "Invalid submission", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val db = FirebaseFirestore.getInstance()

        db.collection("sop_submissions").document(submissionId)
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) {
                    addMessageRow("Submission not found.")
                    return@addOnSuccessListener
                }

                val data = doc.data ?: emptyMap<String, Any>()
                var rowIndex = 0

                // ✅ Show Submission ID
                addRow("Submission ID", submissionId, rowIndex++)

                // ✅ Show Submitted At
                val submittedAt = data["submittedAt"]
                val formattedDate = when (submittedAt) {
                    is com.google.firebase.Timestamp -> {
                        val date = submittedAt.toDate()
                        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(date)
                    }

                    is Long -> {
                        val date = Date(submittedAt)
                        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(date)
                    }

                    else -> "—"
                }
                addRow("Submitted At", formattedDate, rowIndex++)

                // ✅ List other fields sorted alphabetically
                for ((key, value) in data.toSortedMap()) {

                    if (key in listOf("submissionId", "submittedAt", "userId")) continue

                    val displayKey = key.replaceFirstChar { it.uppercase() }
                    val displayValue = value?.toString() ?: "—"

                    addRow(displayKey, displayValue, rowIndex++)
                }
            }
            .addOnFailureListener { e ->
                addMessageRow("Error: ${e.message}")
            }
    }

    private fun addRow(key: String, value: String, index: Int) {
        val row = TableRow(this)

        row.setBackgroundColor(
            if (index % 2 == 0) Color.parseColor("#FFFFFF")
            else Color.parseColor("#F5F5F5")
        )

        val keyView = TextView(this).apply {
            text = key
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.BLACK)
            setPadding(16, 12, 16, 12)
        }

        val valueView = TextView(this).apply {
            text = value
            setTextColor(Color.DKGRAY)
            setPadding(16, 12, 16, 12)
        }

        row.addView(keyView)
        row.addView(valueView)
        tableLayout.addView(row)
    }

    private fun addMessageRow(message: String) {
        val row = TableRow(this)
        val msg = TextView(this)
        msg.text = message
        msg.setTextColor(Color.RED)
        msg.setPadding(16, 12, 16, 12)
        row.addView(msg)
        tableLayout.addView(row)
    }
}
