package com.udacity

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.app.DownloadManager
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var downloadMap = mutableMapOf<Long, MutableMap<String, String>>()

    private lateinit var urlDict: Map<String, String>
    private lateinit var downloadDetail: MutableMap<String, String>
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private lateinit var downloadStatus: String
    private lateinit var uri: String
    private lateinit var url: String
    private lateinit var filename: String

    private var currentProgress = 0
    private lateinit var progressBar: ProgressBar
    private lateinit var startProgress: Button

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(toolbar) // Adding action bar to the top of the screen

//        progressBar = findViewById(R.id.progressBar)
//        startProgress = findViewById(R.id.start_progress)
//
//        startProgress.setOnClickListener {
//            currentProgress = currentProgress + 10
//            progressBar.setProgress(currentProgress)
//            progressBar.max = 100
//        }

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))


        createChannel(getString(R.string.notification_channel_id), getString(R.string.notification_channel_name))
        notificationManager = ContextCompat.getSystemService(this, NotificationManager::class.java) as NotificationManager


//        custom_button.setOnClickListener {
//            download()
//        }

        val downloadButton: LoadingButton = findViewById(R.id.loading_button)
        val radioGroup: RadioGroup = findViewById(R.id.radio_group)

        urlDict = mapOf(
            getString(R.string.glide_text) to "https://github.com/bumptech/glide/archive/master.zip",
            getString(R.string.loadapp_text) to "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip",
            getString(R.string.retrofit_text) to "https://github.com/square/retrofit/archive/master.zip"
        )

//        // Timer test
//        val mainHandler = Handler(Looper.getMainLooper())
//
//        mainHandler.post(object : Runnable {
//            override fun run() {
//                println("a")
//                println("ab")
//                println("abc")
//                mainHandler.postDelayed(this, 100)
//            }
//        })

//        downloadButton.setOnClickListener {
//            val selectedId: Int = radioGroup.checkedRadioButtonId
//
//            if (selectedId != -1) {
//                val radioButton: RadioButton = findViewById(selectedId)
//                Toast.makeText(this, radioButton.text, Toast.LENGTH_SHORT).show()
//
//                val urlKey = radioButton.text.toString()
//
//                Log.d("Pre-Download", "urlKey: " + urlKey)
////                urlDict[urlKey]?.let { it -> Log.d("Pre-Download", "URL: " + it) }
////
////                urlDict[urlKey]?.let { it -> download(it) }
//                download(urlKey)
//
//
//
//            } else {
//                Toast.makeText(this, "No radio button selected", Toast.LENGTH_SHORT).show()
//            }
//        }

        binding.contentMain.loadingButton.setOnClickListener {
            val selectedId: Int = radioGroup.checkedRadioButtonId

            if (selectedId != -1) {
                val radioButton: RadioButton = findViewById(selectedId)
                Toast.makeText(this, radioButton.text, Toast.LENGTH_SHORT).show()

                val urlKey = radioButton.text.toString()

                Log.d("Pre-Download", "urlKey: " + urlKey)
//                urlDict[urlKey]?.let { it -> Log.d("Pre-Download", "URL: " + it) }
//
//                urlDict[urlKey]?.let { it -> download(it) }
                download(urlKey)
                binding.contentMain.loadingButton.startLoading()

            } else {
                Toast.makeText(this, "No radio button selected", Toast.LENGTH_SHORT).show()
            }


        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val q = DownloadManager.Query()
            val downloadedFileId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            q.setFilterById(downloadedFileId!!)
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val c: Cursor = downloadManager.query(q)

            if (c.moveToFirst()) {
                downloadStatus = if (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) "Success" else "Failed"
//                downloadStatus = if (1 > 2) "1" else "2"
//                uri = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)).toString()
                url = downloadMap.getOrDefault(downloadedFileId, mutableMapOf()).getOrDefault("url", "")
                val filePath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE))
                filename = filePath.substring(filePath.lastIndexOf('/') + 1, filePath.length)

                downloadMap[downloadedFileId]!!["filename"] = filename
                downloadMap[downloadedFileId]!!["status"] = downloadStatus

                downloadDetail = downloadMap[downloadedFileId]!!

            } else {
                uri = "1"
                filename = "2"
                downloadStatus = "3"
                downloadDetail
            }

            c.close()

//            val uri: Uri = downloadManager.getUriForDownloadedFile(downloadedFileId!!)

            Thread.sleep(3_000)

            notificationManager.sendNotification(downloadDetail, applicationContext)
        }
    }

    private fun download(urlKey: String) {
        val request =
            DownloadManager.Request(Uri.parse(urlDict[urlKey]))
//                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        downloadMap[downloadID] = mutableMapOf("url" to urlDict[urlKey]!!, "urlKey" to urlKey)
    }

//    companion object {
//        private const val URL =
//            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
//        private const val CHANNEL_ID = "channelId"
//    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Time for breakfast"

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}
