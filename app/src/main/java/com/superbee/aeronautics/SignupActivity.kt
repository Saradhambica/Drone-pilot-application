package com.superbee.aeronautics

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class SignupActivity : AppCompatActivity() {

    private lateinit var usernameEdit: EditText
    private lateinit var phoneEdit: EditText
    private lateinit var sendOtpBtn: Button
    private lateinit var loginRedirect: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Background animation
        val bgImage = findViewById<ImageView>(R.id.backgroundImage)
        val zoomAnim = AnimationUtils.loadAnimation(this, R.anim.background_zoom)
        bgImage.startAnimation(zoomAnim)

        // Initialize views
        usernameEdit = findViewById(R.id.usernameEdit)
        phoneEdit = findViewById(R.id.phoneEdit)
        sendOtpBtn = findViewById(R.id.sendOtpBtn)
        loginRedirect = findViewById(R.id.loginRedirect)

        // Fade-in animation for form
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeIn.duration = 1000
        findViewById<LinearLayout>(R.id.signupContainer).startAnimation(fadeIn)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Send OTP button click
        sendOtpBtn.setOnClickListener {
            val username = usernameEdit.text.toString().trim()
            val phoneNumber = phoneEdit.text.toString().trim()

            if (username.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Enter username and phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!phoneNumber.startsWith("+")) {
                Toast.makeText(
                    this,
                    "Please enter full phone number with country code (e.g. +918123456789)",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            sendOtp(phoneNumber, username)
        }

        // Login redirect click
        loginRedirect.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    private fun sendOtp(phoneNumber: String, username: String) {
        // Optional: test number to avoid real SMS
        val testNumber = "+917337401673"
        val numberToUse = if (phoneNumber == testNumber) testNumber else phoneNumber

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(numberToUse)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto verification for test number — go straight to OTP screen
                    saveUserToFirestore(username, phoneNumber)
                    val intent = Intent(this@SignupActivity, OtpVerificationActivity::class.java)
                    intent.putExtra("phone", phoneNumber)
                    intent.putExtra("username", username)
                    intent.putExtra("credential", credential) // Pass credential for auto sign-in
                    intent.putExtra("isSignup", true)
                    startActivity(intent)
                    finish()
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(
                        this@SignupActivity,
                        "Verification failed: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    // Save user in Firestore when OTP is sent
                    saveUserToFirestore(username, phoneNumber)

                    Toast.makeText(
                        this@SignupActivity,
                        "OTP sent to $phoneNumber",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@SignupActivity, OtpVerificationActivity::class.java)
                    intent.putExtra("phone", phoneNumber)
                    intent.putExtra("username", username)
                    intent.putExtra("verificationId", verificationId)
                    intent.putExtra("isSignup", true)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // Helper function to save user in Firestore
    private fun saveUserToFirestore(username: String, phoneNumber: String) {
        val user = hashMapOf(
            "username" to username,
            "phone" to phoneNumber
        )

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(phoneNumber) // unique ID
            .set(user)
            .addOnSuccessListener {
                // Optional: silent save or Toast
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Error saving user: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}
