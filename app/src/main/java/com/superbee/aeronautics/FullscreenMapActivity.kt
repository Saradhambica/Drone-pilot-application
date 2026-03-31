package com.superbee.aeronautics

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

class FullscreenMapActivity : AppCompatActivity() {

    private lateinit var fullMap: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Force landscape mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))

        fullMap = MapView(this)
        fullMap.setTileSource(TileSourceFactory.MAPNIK)
        fullMap.controller.setZoom(18.0)

        setContentView(fullMap)
    }
}
