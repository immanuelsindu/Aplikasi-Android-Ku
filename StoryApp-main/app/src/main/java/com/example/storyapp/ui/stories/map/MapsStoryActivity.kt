package com.example.storyapp.ui.stories.map

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivityMapsStoryBinding
import com.example.storyapp.di.Injection

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsStoryActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsStoryBinding
    private lateinit var viewModel: MapStoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyRepository = Injection.provideRepository(this)
        viewModel = ViewModelProvider(this, ViewModelFactory(storyRepository))[MapStoryViewModel::class.java]

        viewModel.getNewStories(1, 10)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        // Add a marker in Sydney and move the camera
        viewModel.newStories.observe(this) { stories ->
            val firstStory = stories[0]
            val startPoint = LatLng(firstStory.lat, firstStory.lon)
            mMap.apply {
                for (story in stories) {
                    val location = LatLng(story.lat, story.lon)
                    addMarker(
                        MarkerOptions()
                            .position(location)
                            .title(story.name)
                    )
                }
                animateCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 0f))
                setOnMarkerClickListener {
                    animateCamera(CameraUpdateFactory.newLatLngZoom(it.position, 15f))
                    it.showInfoWindow()
                    true
                }
            }
        }
        setMapStyle()
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e("MAP_STYLING", "Style parsing failed")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e("MAP_STYLING", "Can't find style. Error: ", exception)
        }
    }
}