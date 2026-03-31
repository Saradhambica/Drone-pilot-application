package com.superbee.aeronautics.sopform

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.superbee.aeronautics.R

class SopFormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sop_form)

        // Optional: Set up NavController only if Fragment exists
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val navHostFragment = fragment as? NavHostFragment

        navHostFragment?.let {
            val navController = it.navController
            // You can use navController for navigation later
        }
    }
}
