package itmo.android.pictures

import android.app.IntentService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.full_image.*
import java.net.URL

class FullScreenPicture : AppCompatActivity() {

    private var loaded = false
    private lateinit var mMyBroadcastReceiver: MyBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.full_image)
        mMyBroadcastReceiver = MyBroadcastReceiver()
        val intentFilter = IntentFilter("loaded")
        registerReceiver(mMyBroadcastReceiver, intentFilter)
        val url = intent?.getStringExtra("link")
        if (savedInstanceState != null) {
            loaded = savedInstanceState.getBoolean("loaded")
            if (loaded) {
                picture.setImageBitmap(openFileInput("temp.txt").use { BitmapFactory.decodeStream(it) })
                pb.visibility = View.GONE
            }
        } else {
            val intentService = Intent(this, LoadFullImage::class.java)
            intentService.putExtras(Bundle().apply {
                putString("link", url)
            })
            startService(intentService)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("loaded", loaded)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mMyBroadcastReceiver)
    }

    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            loaded = true
            if (intent!!.getStringExtra("progress") == "ready") {
                picture.setImageBitmap(openFileInput("temp.txt").use { BitmapFactory.decodeStream(it) })
                pb.visibility = View.GONE
            }
        }
    }

}

class LoadFullImage(name: String = "") : IntentService(name) {

    override fun onHandleIntent(intent: Intent?) {
        val url = intent?.extras!!.getString("link")
        val a = BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
        val fileOutput = openFileOutput("temp.txt", MODE_PRIVATE)
        fileOutput.use {
            a.compress(Bitmap.CompressFormat.PNG, 100, fileOutput)
            fileOutput.flush()
        }
        val responseIntent = Intent("loaded")
        responseIntent.putExtra("progress", "ready")
        sendBroadcast(responseIntent)
    }
}

