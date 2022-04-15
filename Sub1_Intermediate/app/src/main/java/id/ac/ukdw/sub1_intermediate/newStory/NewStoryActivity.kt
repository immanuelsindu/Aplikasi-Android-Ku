package id.ac.ukdw.sub1_intermediate.newStory

import android.Manifest
import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import id.ac.ukdw.sub1_intermediate.R
import id.ac.ukdw.sub1_intermediate.UserPreference
import id.ac.ukdw.sub1_intermediate.api.ApiConfig
import id.ac.ukdw.sub1_intermediate.camera.CameraActivity
import id.ac.ukdw.sub1_intermediate.camera.reduceFileImage
import id.ac.ukdw.sub1_intermediate.camera.rotateBitmap
import id.ac.ukdw.sub1_intermediate.camera.uriToFile
import id.ac.ukdw.sub1_intermediate.databinding.ActivityNewStoryBinding
import id.ac.ukdw.sub1_intermediate.homeStory.HomeStoryActivity
import id.ac.ukdw.sub1_intermediate.homeStory.UserModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class NewStoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewStoryBinding
    private lateinit var mUserPreference: UserPreference
    private lateinit var userModel: UserModel
    private var getFile: File? = null

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


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
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
        mUserPreference = UserPreference(this)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        showLoading(false)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnUpload.setOnClickListener {
            userModel = mUserPreference.getUser()
            if(userModel.name.toString() != ""){
                uploadImage()
            }else{
                uploadImagGuest()
            }
        }

        binding.btnCamera.setOnClickListener {
            startCameraX()
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }
    }

    private fun intentToHomeStory(){
        val intent = Intent(this, HomeStoryActivity::class.java)
        intent.flags =  Intent.FLAG_ACTIVITY_CLEAR_TOP
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

                val userPreference_2 = UserPreference(this)
                val myToken2 = BEARER+ userPreference_2.getToken()

                val service = ApiConfig.getApiService().uploadImage(myToken2, imageMultipart,desc2)
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
                                        intentToHomeStory()
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

            val service = ApiConfig.getApiService().uploadImageGuest(imageMultipart,description)
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
                                    intentToHomeStory()
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

}