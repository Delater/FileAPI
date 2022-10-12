package com.fk.file_api.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fk.file_api.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}