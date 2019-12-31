package itmo.android.pictures

import android.app.IntentService
import android.content.Intent
import android.graphics.BitmapFactory
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ImagesService(name: String = "") : IntentService(name) {

    override fun onHandleIntent(intent: Intent?) {
        val url = intent?.getStringExtra("link")
        val c =
            URL(url).openConnection() as HttpURLConnection
        val json = JSONArray(c.inputStream.use { it.reader().readText() })
        var i = 0
        while (i != json.length()) {
            val it = json[i] as JSONObject
            pictures.add(
                Pic(
                    it.get("alt_description").toString(),
                    URL(
                        (it.get("urls") as JSONObject)
                            .get("thumb").toString()
                    ).openConnection().getInputStream().use {BitmapFactory.decodeStream(it)},
                    (it.get("urls") as JSONObject).get("full").toString()
                )
            )
            val responseIntent = Intent("filter")
            responseIntent.putExtra("progress", (i + 1) * 100 / json.length())
            sendBroadcast(responseIntent)
            ++i
        }
    }

}

