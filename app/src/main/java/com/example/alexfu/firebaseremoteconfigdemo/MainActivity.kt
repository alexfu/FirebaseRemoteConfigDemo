package com.example.alexfu.firebaseremoteconfigdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private val remoteConfig = FirebaseRemoteConfig.getInstance()
    private val bannerImageView by lazy { findViewById<ImageView>(R.id.banner) }
    private val sampleTextView by lazy { findViewById<TextView>(R.id.text_sample) }

    companion object {
        val KEY_BANNER_URL = "banner_url"
        val KEY_SAMPLE_TEXT = "sample_text"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)
        setUpRemoteConfigDefaultValues()
        reloadUI()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.fetch) {
            // For demo purposes only!
            // Normally, we would fetch the values from Firebase and then call activateFetched
            // next time the app starts to avoid changing content while user is using the app.
            remoteConfig.fetch(0)
                    .addOnSuccessListener {
                        Toast.makeText(this@MainActivity, "Fetch succeeded!", Toast.LENGTH_LONG).show()
                        remoteConfig.activateFetched()
                        reloadUI()
                    }
                    .addOnFailureListener { exception ->
                        exception.printStackTrace()
                        Toast.makeText(this@MainActivity, "Fetch failed!", Toast.LENGTH_LONG).show()
                    }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun reloadUI() {
        val state = ViewState(
                bannerImageUrl = remoteConfig.getString(KEY_BANNER_URL),
                sampleText = remoteConfig.getString(KEY_SAMPLE_TEXT)
        )
        render(state)
    }

    private fun render(state: ViewState) {
        Picasso.with(this).load(state.bannerImageUrl).into(bannerImageView)
        sampleTextView.text = state.sampleText
    }

    private fun setUpRemoteConfigDefaultValues() {
        // Can also define default values from XML.
        // See https://github.com/firebase/quickstart-android/blob/master/config/app/src/main/res/xml/remote_config_defaults.xml
        val defaultValues = mapOf(
                KEY_BANNER_URL to "https://www.android.com/static/2016/img/share/oreo-lg.jpg",
                KEY_SAMPLE_TEXT to "Praesent non mi rizzle mauris dawg bibendizzle. lacinia boofron shiznit. Crizzle id get down get down et leo sodalizzle euismod. Aliquizzle lobortizzle, maurizzle doggy dapibizzle bling bling, nulla ligula bibendum metus, izzle shiznit augue boom shackalack daahng dawg bizzle. Vivamizzle go to hizzle lacizzle crazy ipsum. Vivamizzle arcu gangsta, fermentizzle fo shizzle mah nizzle fo rizzle, mah home g-dizzle amizzle, faucibus izzle, break it down izzle, boom shackalack. Fo shizzle stuff laorizzle bizzle. Vestibulizzle erizzle phat, check out this et, fo shizzle izzle, dizzle the bizzle, boom shackalack"
        )
        remoteConfig.setDefaults(defaultValues)
    }
}
