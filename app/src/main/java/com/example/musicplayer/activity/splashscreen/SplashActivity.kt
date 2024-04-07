package com.example.musicplayer.activity.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.R
import com.example.musicplayer.activity.music.presentation.SearchActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import timber.log.Timber

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()
        setupAppCenter()
        setupScreen()
    }

    private fun setupScreen() {
        val background: Thread = object : Thread() {
            override fun run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep((1 * 1000).toLong())

                    val i = Intent(baseContext, SearchActivity::class.java)
                    startActivity(i)

                    //Remove activity
                    finish()
                } catch (e: Exception) {
                    Timber.tag("SplashActivity").e("Err : %s", e.message)
                }
            }
        }
        // start thread
        background.start()
    }

    private fun setupAppCenter() {
        AppCenter.configure(application, "21c7c5c8-3df3-4473-9f9a-352b24fa896b")
        if (AppCenter.isConfigured()) {
            AppCenter.start(Analytics::class.java)
            AppCenter.start(Crashes::class.java)
        }
    }
}