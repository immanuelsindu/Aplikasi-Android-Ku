package com.example.storyapp.ui.stories.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.data.Result
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.di.Injection
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.File


class AddStoryActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String
    private lateinit var viewModel: AddStoryViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var marker: Marker
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var getFile: File? = null
    private val resultIntent = Intent()

    companion object {
        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        const val UPLOAD_STATUS = "upload_status"
        const val RESULT_CODE = 100
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.permission_not_granted),
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyRepository = Injection.provideRepository(this)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(storyRepository)
        )[AddStoryViewModel::class.java]

        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSION,
                REQUEST_CODE_PERMISSIONS
            )
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnUpload.setOnClickListener {
            setButton(false)
            val description = binding.edtDescription.text.toString()
            viewModel.uploadImage(
                getFile?.let { file -> reduceFileImage(file) },
                marker.position.latitude,
                marker.position.longitude,
                description
            )
                .observe(this) {
                    when (it) {
                        is Result.Success -> {
                            resultIntent.putExtra(UPLOAD_STATUS, true)
                            setResult(RESULT_CODE, resultIntent)
                            finish()
                        }
                        is Result.Loading -> {
                            binding.uploadLoading.visibility = View.VISIBLE
                        }
                        is Result.Error -> {
                            binding.uploadLoading.visibility = View.GONE
                            setButton(true)
                            Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_selector) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setButton(isEnable: Boolean) {
        binding.btnUpload.isEnabled = isEnable
        binding.btnUpload.isClickable = isEnable
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto() {
        val intent = Intent()
        intent.action = MediaStore.ACTION_IMAGE_CAPTURE
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI = FileProvider.getUriForFile(
                this,
                "com.example.storyapp",
                it
            )

            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImage = result.data?.data as Uri
            getFile = uriToFile(selectedImage, this@AddStoryActivity)

            binding.ivPreview.setImageURI(selectedImage)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            getFile = File(currentPhotoPath)

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.ivPreview.setImageBitmap(result)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getMyLocation()
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMapClickListener {
            showMarker(it)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val startPoint = LatLng(location.latitude, location.longitude)
                    showMarker(startPoint)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 17f))
                } else {
                    Toast.makeText(
                        this,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showMarker(latLng: LatLng) {
        mMap.clear()
        if (this::marker.isInitialized) {
            marker.remove()
        }
        marker = mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .draggable(true)
        ) as Marker
    }
}