package itmo.android.pictures

import android.content.BroadcastReceiver
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import kotlinx.android.synthetic.main.activity_main.*
import android.content.IntentFilter

val pictures: MutableList<Pic> = mutableListOf()

class MainActivity : AppCompatActivity() {

    private var loaded = false
    private lateinit var mMyBroadcastReceiver: MyBroadcastReceiver

    private val myAdapter = UserAdapter(pictures) {
        startActivity(
            Intent(this@MainActivity, FullScreenPicture::class.java).putExtra(
                "link",
                it.highQ
            )
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("loaded", loaded)
        if (!loaded) {
            outState.putInt("progress", progressBar.progress)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            layoutManager = viewManager
            adapter = myAdapter
        }
        mMyBroadcastReceiver = MyBroadcastReceiver()
        val intentFilter = IntentFilter("filter")
        registerReceiver(mMyBroadcastReceiver, intentFilter)
        if (savedInstanceState != null) {
            loaded = savedInstanceState.getBoolean("loaded")
            if (loaded) {
                progressBar.visibility = View.GONE
                my_recycler_view.visibility = View.VISIBLE
            } else {
                progressBar.progress = savedInstanceState.getInt("progress")
            }
        } else {
            val intentMyIntentService = Intent(this, ImagesService::class.java)
            intentMyIntentService.putExtra(
                "link",
                "https://api.unsplash.com/photos/random?count=30" +
                        "&client_id=857bc8e8cbd3f72ecc4c7338bd61543ad0120c2610f415aa671ead06a468c057"
            )
            startService(intentMyIntentService)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mMyBroadcastReceiver)
    }

    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val update = intent!!.getIntExtra("progress", 0)
            if (update == 100) {
                my_recycler_view.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                myAdapter.notifyDataSetChanged()
                loaded = true
            } else {
                progressBar.progress = update
            }
        }
    }
}

