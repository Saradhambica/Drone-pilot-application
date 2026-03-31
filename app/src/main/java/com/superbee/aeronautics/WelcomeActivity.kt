package com.superbee.aeronautics

import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.app.Activity.OVERRIDE_TRANSITION_OPEN

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val titleText: TextView = findViewById(R.id.titleText)
        val welcomeBtn: Button = findViewById(R.id.welcomeBtn)

        // Fade-in title
        val fadeInTitle = AlphaAnimation(0f, 1f).apply {
            duration = 1500
            fillAfter = true
        }
        titleText.startAnimation(fadeInTitle)

        // After title fade, fade in button and start glow loop
        fadeInTitle.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                val fadeInBtn = AlphaAnimation(0f, 1f).apply {
                    duration = 1000
                    fillAfter = true
                }
                welcomeBtn.startAnimation(fadeInBtn)

                fadeInBtn.setAnimationListener(object : SimpleAnimationListener() {
                    override fun onAnimationEnd(animation: Animation?) {
                        startGlowAnimation(welcomeBtn)
                    }
                })
            }
        })

        // Navigate to LoginActivity on click
        // Navigate to SignupActivity on click
        welcomeBtn.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
        }
    }

    // Smooth repeating glow effect (fade in/out loop)
    private fun startGlowAnimation(button: Button) {
        val glowIn = AlphaAnimation(1f, 0.6f).apply {
            duration = 800
            fillAfter = true
        }
        val glowOut = AlphaAnimation(0.6f, 1f).apply {
            duration = 800
            fillAfter = true
        }

        glowIn.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                button.startAnimation(glowOut)
            }
        })

        glowOut.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                button.startAnimation(glowIn)
            }
        })

        button.startAnimation(glowIn)
    }

    // Helper to skip unused animation callbacks
    private open class SimpleAnimationListener : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {}
        override fun onAnimationEnd(animation: Animation?) {}
        override fun onAnimationRepeat(animation: Animation?) {}
    }

