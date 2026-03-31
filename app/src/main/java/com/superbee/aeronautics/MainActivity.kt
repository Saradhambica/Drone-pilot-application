/*package com.superbee.aeronautics

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.superbee.aeronautics.sopform.SopFormActivity
import com.superbee.aeronautics.sopform.MyProfileActivity
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var welcomeText: TextView
    private lateinit var submitSopCard: LinearLayout
    private lateinit var profileCard: LinearLayout
    private lateinit var SagcsCard: LinearLayout
    private lateinit var missionCard: LinearLayout

    private lateinit var changeFarmerText: TextView

    private lateinit var auth: FirebaseAuth

    private var username: String = ""
    private var phoneNumber: String = ""
    private var farmerId: String = ""
    private var farmerName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        farmerId = intent.getStringExtra("selectedFarmerId") ?: ""
        farmerName = intent.getStringExtra("selectedFarmerName") ?: ""

        username = intent.getStringExtra("username") ?: "User"
        phoneNumber = intent.getStringExtra("phoneNumber") ?: ""

        val bgImage = findViewById<ImageView>(R.id.backgroundImage)
        val zoomAnim = AnimationUtils.loadAnimation(this, R.anim.background_zoom)
        bgImage.startAnimation(zoomAnim)

        welcomeText = findViewById(R.id.welcomeText)
        submitSopCard = findViewById(R.id.submitSopCard)
        profileCard = findViewById(R.id.profileCard)
        SagcsCard = findViewById(R.id.SagcsCard)
        missionCard = findViewById(R.id.missionCard)

        changeFarmerText = findViewById(R.id.changeFarmerText)

        welcomeText.text = "Farmer: $farmerName"

        changeFarmerText.setOnClickListener {
            val intent = Intent(this, FarmersActivity::class.java)
            startActivity(intent)
            finish()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(
                    this@MainActivity,
                    "Use logout to exit",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        submitSopCard.setOnClickListener {
            val intent = Intent(this, SopFormActivity::class.java)
            intent.putExtra("farmerId", farmerId)
            intent.putExtra("farmerName", farmerName)
            startActivity(intent)
        }

        missionCard.setOnClickListener {
            val intent = Intent(this, MissionActivity::class.java)
            intent.putExtra("farmerId", farmerId)
            intent.putExtra("farmerName", farmerName)
            startActivity(intent)
        }

        profileCard.setOnClickListener {
            startActivity(Intent(this, MyProfileActivity::class.java))
        }

        // ⭐ OPEN OR INSTALL QGC APK
        SagcsCard.setOnClickListener {
            openOrInstallQGC()
        }

        // ⭐ APPLY CARD ANIMATIONS HERE
        applyCardAnimations()
    }

    private fun applyCardAnimations() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.scale_in)

        submitSopCard.startAnimation(anim)
        missionCard.startAnimation(anim)
        profileCard.startAnimation(anim)
        SagcsCard.startAnimation(anim)
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    // ---------------------------------------------------------
    // OPEN QGC OR INSTALL IF NOT INSTALLED
    // ---------------------------------------------------------
    private fun openOrInstallQGC() {
        val packageName = "org.mavlink.qgroundcontrol"   // QGC package name

        if (isQgcInstalled(packageName)) {
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                startActivity(launchIntent)
            } else {
                Toast.makeText(this, "Unable to launch QGC", Toast.LENGTH_SHORT).show()
            }
        } else {
            installQGCFromAssets()
        }
    }

    // ---------------------------------------------------------
    // CHECK IF INSTALLED
    // ---------------------------------------------------------
    private fun isQgcInstalled(pkg: String): Boolean {
        return try {
            packageManager.getPackageInfo(pkg, 0)
            true
        } catch (e: Exception) {
            false
        }
    }

    // ---------------------------------------------------------
    // INSTALL QGC FROM assets/qgc.apk
    // ---------------------------------------------------------
    private fun installQGCFromAssets() {
        try {
            val input = assets.open("qgc.apk") // your custom QGC apk
            val outFile = File(getExternalFilesDir(null), "qgc.apk")
            val output = FileOutputStream(outFile)

            input.copyTo(output)
            input.close()
            output.close()

            val apkUri = FileProvider.getUriForFile(
                this,
                "$packageName.fileprovider",
                outFile
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            startActivity(intent)

        } catch (e: Exception) {
            Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
*/