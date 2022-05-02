package id.ac.ukdw.sub1_intermediate.newStory

import android.Manifest
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import id.ac.ukdw.sub1_intermediate.R
import id.ac.ukdw.sub1_intermediate.UserPreference
import id.ac.ukdw.sub1_intermediate.api.ApiConfig
import id.ac.ukdw.sub1_intermediate.camera.CameraActivity
import id.ac.ukdw.sub1_intermediate.camera.reduceFileImage
import id.ac.ukdw.sub1_intermediate.camera.rotateBitmap
import id.ac.ukdw.sub1_intermediate.camera.uriToFile
import id.ac.ukdw.sub1_intermediate.databinding.ActivityNewStoryBinding
import id.ac.ukdw.sub1_intermediate.homeStory.*
import id.ac.ukdw.sub1_intermediate.userSession.UserPreferencesDS
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.security.Permission

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
class NewStoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewStoryBinding
//    private lateinit var mUserPreference: UserPreference
//    private lateinit var userModel: UserModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var myLocation: Location
    private var getFile: File? = null
    private lateinit var database: StoryDatabase

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val TAG = "NewStoryActivity"
        private const val PICTURE = "picture"
        private const val ISBACKCAMERA= "isBackCamera"
        private const val TYPE = "image/*"
        private const val PHOTO = "photo"
        private const val BEARER = "Bearer "
        private const val STORYCREATEDSUSSCESS = "Story created successfully"
        private const val TOKENMAX = "Token maximum age exceeded"
        private const val TOKEN = "token"
        private const val NAME = "name"


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.failedGetPermission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.titleNewStory)
//        mUserPreference = UserPreference(this)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


//        getMyLastLocation()
        myLocation = intent.getParcelableExtra("location")!!
        showLoading(false)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        getMyLastLocation()


        binding.btnUpload.setOnClickListener {
            val pref = UserPreferencesDS.getInstance(dataStore)
            lifecycleScope.launch{
                if(pref.getCurrenctName() != ""){
//                    getMyLastLocation()
                    uploadImage()
                }else{
//                    getMyLastLocation()
                    uploadImagGuest()
                }
            }

//            userModel = mUserPreference.getUser()
//            if(userModel.name.toString() != ""){
//                uploadImage()
//            }else{
//                uploadImagGuest()
//            }
        }

        binding.btnCamera.setOnClickListener {
            startCameraX()
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }
    }

    private fun intentToHomeStory(name: String, token: String = "" ){
//        val token = intent.getStringExtra(TOKEN).toString()
        val intent = Intent(this, HomeStoryActivity::class.java)
        intent.flags =  Intent.FLAG_ACTIVITY_CLEAR_TOP
        if(name != "Guest" && token !=""){
            intent.putExtra(NAME,name)
            intent.putExtra("token", token)
        }
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra(PICTURE) as File
            val isBackCamera = it.data?.getBooleanExtra(ISBACKCAMERA, true) as Boolean
            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            binding.imgStory.setImageBitmap(result)
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = TYPE
        val chooser = Intent.createChooser(intent, resources.getString(R.string.choosePicture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.imgStory.setImageURI(selectedImg)
        }
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val description = findViewById<EditText>(R.id.editText).text.toString()
            if(description != ""){
                val desc2 = description.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    PHOTO,
                    file.name,
                    requestImageFile
                )
                showLoading(true)

//                val userPreference_2 = UserPreference(this)
//                val myToken2 = BEARER+ userPreference_2.getToken()
//                val pref = UserPreferencesDS.getInstance(dataStore)

                val name = intent.getStringExtra(NAME).toString()
                val token = intent.getStringExtra(TOKEN).toString()
                val service = ApiConfig.getApiService().uploadImage(
                    BEARER + token,
                    imageMultipart,
                    desc2,
                    myLocation.latitude,
                    myLocation.longitude
                )
                service.enqueue(object : Callback<UploadStoryResponse> {
                    override fun onResponse(
                        call: Call<UploadStoryResponse>,
                        response: Response<UploadStoryResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null && !responseBody.error) {
                                when(responseBody.message){
                                    STORYCREATEDSUSSCESS ->{
                                        showLoading(false)
                                        Toast.makeText(this@NewStoryActivity,getString(R.string.StoryCreatedSuccessfully), Toast.LENGTH_SHORT).show()

//                                        lifecycleScope.launch {
//                                            database.remoteKeysDao().deleteRemoteKeys()
//                                            database.storyDao().deleteAll()
//                                        }
                                        intentToHomeStory(name,token)
                                    }
                                    TOKENMAX ->{
                                        showLoading(false)
                                        Toast.makeText(this@NewStoryActivity,getString(R.string.tokenMaxAgeExceeded), Toast.LENGTH_SHORT).show()
                                    }else->{
                                        showLoading(false)
                                        Toast.makeText(this@NewStoryActivity,getString(R.string.anErrorOccurred), Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } else {
                            showLoading(false)
                            Toast.makeText(this@NewStoryActivity,  resources.getString(R.string.enterImageFirst), Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<UploadStoryResponse>, t: Throwable) {
                        showLoading(false)
                        Toast.makeText(this@NewStoryActivity, resources.getString(R.string.failedInstaceRetrofit), Toast.LENGTH_SHORT).show()
                    }
                })
            }else{
                Toast.makeText(this@NewStoryActivity, getString(R.string.enterDescriptionFirst), Toast.LENGTH_SHORT).show()
            }
        } else {
            showLoading(false)
            Toast.makeText(this@NewStoryActivity, resources.getString(R.string.enterImageFirst), Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImagGuest() {
        if (getFile != null) {
            showLoading(true)
            val file = reduceFileImage(getFile as File)

            val description = binding.editText.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                PHOTO,
                file.name,
                requestImageFile
            )

            val service = ApiConfig.getApiService().uploadImageGuest(imageMultipart,description, myLocation.latitude, myLocation.longitude)
            service.enqueue(object : Callback<GuestUploadResponse> {
                override fun onResponse(
                    call: Call<GuestUploadResponse>,
                    response: Response<GuestUploadResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            when(responseBody.message){
                                STORYCREATEDSUSSCESS ->{
                                    showLoading(false)
                                    Toast.makeText(this@NewStoryActivity,getString(R.string.StoryCreatedSuccessfully), Toast.LENGTH_SHORT).show()
                                    intentToHomeStory("Guest")
                                }
                                TOKENMAX ->{
                                    showLoading(false)
                                    Toast.makeText(this@NewStoryActivity,getString(R.string.tokenMaxAgeExceeded), Toast.LENGTH_SHORT).show()
                                }else->{
                                    showLoading(false)
                                    Toast.makeText(this@NewStoryActivity,getString(R.string.anErrorOccurred), Toast.LENGTH_SHORT).show()
                                }
                            }

                        }
                    } else {
                        showLoading(false)
                        Toast.makeText(this@NewStoryActivity,  getString(R.string.enterDescriptionFirst), Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<GuestUploadResponse>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(this@NewStoryActivity, resources.getString(R.string.failedInstaceRetrofit), Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            showLoading(false)
            Toast.makeText(this@NewStoryActivity, resources.getString(R.string.enterImageFirst), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progresBar.visibility = View.VISIBLE
        } else {
            binding.progresBar.visibility = View.GONE
        }
    }

//    private val requestPermissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//            when {
//                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
//                    // Precise location access granted.
//                    getMyLastLocation()
//                }
//                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
//                    // Only approximate location access granted.
//                    getMyLastLocation()
//                }
//                else -> {
//                    // No location access granted.
//                }
//            }
//        }

//    private fun checkPermission(permission: String): Boolean {
//        return ContextCompat.checkSelfPermission(
//            this,
//            permission
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun getMyLastLocation() {
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&  checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
//            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//                if (location != null) {
//                        setLocation(location)
//                } else {
//                    Toast.makeText(
//                        this,
//                        "Location is not found. Try Again",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        } else {
//            requestPermissionLauncher.launch(
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//            )
//        }
//    }

    private fun setLocation(location: Location){
        myLocation = location
    }


}