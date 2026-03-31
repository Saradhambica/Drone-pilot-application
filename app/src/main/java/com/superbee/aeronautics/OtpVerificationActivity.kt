package com.superbee.aeronautics

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OtpVerificationActivity : AppCompatActivity() {

    private lateinit var otp1: EditText
    private lateinit var otp2: EditText
    private lateinit var otp3: EditText
    private lateinit var otp4: EditText
    private lateinit var otp5: EditText
    private lateinit var otp6: EditText
    private lateinit var verifyOtpBtn: Button
    private lateinit var resendOtpBtn: TextView
    private lateinit var phoneNumberText: TextView
    private lateinit var auth: FirebaseAuth

    private var verificationId: String = ""
    private var phoneNumber: String = ""
    private var username: String = ""
    private var isSignup: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)

        // Background animation
        val bgImage = findViewById<ImageView>(R.id.backgroundImage)
        val zoomAnim = AnimationUtils.loadAnimation(this, R.anim.background_zoom)
        bgImage.startAnimation(zoomAnim)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Get data from intent
        verificationId = intent.getStringExtra("verificationId") ?: ""
        phoneNumber = intent.getStringExtra("phone") ?: ""
        username = intent.getStringExtra("username") ?: ""
        isSignup = intent.getBooleanExtra("isSignup", false)

        // Initialize views
        otp1 = findViewById(R.id.otp1)
        otp2 = findViewById(R.id.otp2)
        otp3 = findViewById(R.id.otp3)
        otp4 = findViewById(R.id.otp4)
        otp5 = findViewById(R.id.otp5)
        otp6 = findViewById(R.id.otp6)
        verifyOtpBtn = findViewById(R.id.verifyOtpBtn)
        resendOtpBtn = findViewById(R.id.resendOtpBtn)
        phoneNumberText = findViewById(R.id.phoneNumberText)

        // Set phone number text
        phoneNumberText.text = "OTP sent to $phoneNumber"

        // Fade-in animation
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeIn.duration = 1000
        findViewById<LinearLayout>(R.id.otpContainer).startAnimation(fadeIn)

        // Setup OTP auto-focus
        setupOtpInputs()

        // Verify OTP button
        verifyOtpBtn.setOnClickListener {
            val otp = otp1.text.toString() + otp2.text.toString() +
                    otp3.text.toString() + otp4.text.toString() +
                    otp5.text.toString() + otp6.text.toString()

            if (otp.length == 6) {
                verifyOtp(otp)
            } else {
                Toast.makeText(this, "Please enter complete OTP", Toast.LENGTH_SHORT).show()
            }
        }

        // Resend OTP button
        resendOtpBtn.setOnClickListener {
            Toast.makeText(this, "OTP resent successfully", Toast.LENGTH_SHORT).show()
            // TODO: Implement resend OTP logic
        }
    }

    private fun setupOtpInputs() {
        val editTexts = arrayOf(otp1, otp2, otp3, otp4, otp5, otp6)

        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        // Move to next field
                        if (i < editTexts.size - 1) {
                            editTexts[i + 1].requestFocus()
                        }
                    } else if (s?.isEmpty() == true && before == 1) {
                        // Move to previous field on backspace
                        if (i > 0) {
                            editTexts[i - 1].requestFocus()
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun verifyOtp(otp: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Verification successful!", Toast.LENGTH_SHORT).show()

                    // Navigate to FarmersActivity
                    val intent = Intent(this, FarmersActivity::class.java)
                    intent.putExtra("username", username)
                    intent.putExtra("phone", phoneNumber)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()

                } else {
                    Toast.makeText(this, "Invalid OTP. Please try again.", Toast.LENGTH_LONG).show()
                }
            }
    }
}