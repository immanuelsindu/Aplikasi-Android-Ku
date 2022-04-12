package id.ac.ukdw.myservice

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var mServiceBound = false
    private lateinit var mBoundService: MyBoundService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStartService = findViewById<Button>(R.id.btn_start_service)
        val btnStartBoundService = findViewById<Button>(R.id.btn_start_bound_service)
        val btnStopBoundService = findViewById<Button>(R.id.btn_stop_bound_service)


        btnStartService.setOnClickListener {
            val mStartServiceIntent = Intent(this, MyService::class.java)
            startService(mStartServiceIntent)
        }

        btnStartBoundService.setOnClickListener {
            val mBoundServiceIntent = Intent(this, MyBoundService::class.java)
            bindService(mBoundServiceIntent, mServiceConnection, BIND_AUTO_CREATE)
        }

        btnStopBoundService.setOnClickListener {
            unbindService(mServiceConnection)
        }
    }

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            mServiceBound = false
        }
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val myBinder = service as MyBoundService.MyBinder
            mBoundService = myBinder.getService
            mServiceBound = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mServiceBound) {
            unbindService(mServiceConnection)
        }
    }

}