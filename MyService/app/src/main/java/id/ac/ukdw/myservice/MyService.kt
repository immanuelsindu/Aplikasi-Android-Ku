package id.ac.ukdw.myservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MyService : Service() {

    companion object {
        internal val TAG = MyService::class.java.simpleName
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service dijalankan...")
        delaying()
        return START_STICKY
    }

    private fun delaying() = runBlocking {
        launch {
            delay(3000)
            stopSelf()
            Log.d(TAG, "Service dihentikan")
        }
    }
}