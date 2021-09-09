package com.udacity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.activity_detail.*
import android.content.Intent




class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        findViewById<TextView>(R.id.filename_content).apply {
            text = intent.getStringExtra("urlKey")
        }

        findViewById<TextView>(R.id.status_content).apply {
            text = intent.getStringExtra("Status")
        }

        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.back_to_main_button).setOnClickListener {
            val goBackIntent = Intent(this, MainActivity::class.java)
            startActivity(goBackIntent)
            finish()
        }
//
    }

}
