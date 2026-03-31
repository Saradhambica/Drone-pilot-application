package com.superbee.aeronautics

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEdit: EditText
    private lateinit var phoneEdit: EditText
    private lateinit var loginBtn: Button
    private lateinit var signupRedirect: TextView
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val bgImage = findViewById<ImageView>(R.id.backgroundImage)
        val zoomAnim = AnimationUtils.loadAnimation(this, R.anim.background_zoom)
        bgImage.startAnimation(zoomAnim)

        usernameEdit = findViewById(R.id.usernameEdit)
        phoneEdit = findViewById(R.id.phoneEdit)
        loginBtn = findViewById(R.id.requestOtpBtn)
        signupRedirect = findViewById(R.id.signupRedirect)

        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeIn.duration = 1000
        findViewById<LinearLayout>(R.id.loginContainer).startAnimation(fadeIn)

        db = FirebaseFirestore.getInstance()

        loginBtn.setOnClickListener {
            val username = usernameEdit.text.toString().trim()
            val phone = phoneEdit.text.toString().trim()

            if (username.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Enter username and phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginBtn.isEnabled = false
            db.collection("users")
                .whereEqualTo("username", username)
                .whereEqualTo("phone", phone)
                .get()
                .addOnSuccessListener { result ->
                    loginBtn.isEnabled = true
                    if (!result.isEmpty) {
                        val intent = Intent(this, FarmersActivity::class.java)
                        intent.putExtra("username", username)
                        intent.putExtra("phone", phone)
                        startActivity(intent)
                        finish()
                } else {
                        Toast.makeText(this, "Invalid username or phone", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener {
                    loginBtn.isEnabled = true
                    Toast.makeText(this, "Error logging in: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }

        signupRedirect.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
