package id.ac.ukdw.sub1_intermediate

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import id.ac.ukdw.sub1_intermediate.databinding.ActivityMapsBinding
import id.ac.ukdw.sub1_intermediate.homeStory.StoryViewModel
import id.ac.ukdw.sub1_intermediate.homeStory.ViewModelFactory

class MyMaps : AppCompatActivity(), OnMapReadyCallback {
    companion object{
        private const val TAG = "MapsActivity"
        private const val TOKEN = "token"
        private const val BEARER = "Bearer "
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var storyViewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val iToken = BEARER + intent.getStringExtra(TOKEN).toString()
//        val iToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWFGRGExd3hfRkdFVnJoUHgiLCJpYXQiOjE2NTEzMzQ4ODF9.zrlqKIc8IFJFFYbLS8wNjqxQiMhxdFOFdYRhx8JApFw"
        Log.d(TAG, "ini token = $iToken")
        storyViewModel= ViewModelProvider(this, ViewModelFactory(iToken, this))[StoryViewModel::class.java]

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        storyViewModel.getMapListStory(1, 10)
        storyViewModel.mapsListStory.observe(this){
            val firstLocation = LatLng(it[0].lat,it[0].lon)
            mMap.apply {
                for (locStory in it){
                    val location = LatLng(locStory.lat, locStory.lon)
                    addMarker(
                        MarkerOptions().position(location).title(locStory.name)
                    )
                }
                animateCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 0f))
                setOnMarkerClickListener {marker ->
                    animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 15f))
                    marker.showInfoWindow()
                    true
                }
            }
        }




        getMyLocation()
//        setMapStyle()
//
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))



//        val dicodingSpace = LatLng(-6.8957643, 107.6338462)
//        mMap.addMarker(
//            MarkerOptions()
//                .position(dicodingSpace)
//                .title("Dicoding Space")
//                .snippet("Batik Kumeli No.50")
//        )
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 15f))

//        mMap.setOnMapLongClickListener { latLng ->
//            mMap.addMarker(
//                MarkerOptions()
//                    .position(latLng)
//                    .title("New Marker")
//                    .snippet("Lat: ${latLng.latitude} Long: ${latLng.longitude}")
//                    .icon(vectorToBitmap(R.drawable.ic_android_black_24dp, Color.parseColor("#3DDC84")))
//            )
//        }
        mMap.setOnPoiClickListener { pointOfInterest: PointOfInterest ->
            mMap.addMarker(
                MarkerOptions()
                    .position(pointOfInterest.latLng)
                    .title(pointOfInterest.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            )?.showInfoWindow()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.map_options, menu)
//        return true
//    }

    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

//    private fun setMapStyle() {
//        try {
//            val success =
//                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
//            if (!success) {
//                Log.e(TAG, "Style parsing failed.")
//            }
//        } catch (exception: Resources.NotFoundException) {
//            Log.e(TAG, "Can't find style. Error: ", exception)
//        }
//    }
}