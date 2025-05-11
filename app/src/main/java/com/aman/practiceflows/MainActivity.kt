package com.aman.practiceflows

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        GlobalScope.launch {
            val data = producer()
            data.collect{
                Log.d("Flows 1", it.toString())
            }
        }

        GlobalScope.launch {
            val data = producer()
            delay(2500)
            data.collect{
                Log.d("Flows 2", it.toString())
            }
        }

    }

    fun producer() = flow<Int>{
        val list = listOf(1,2,3,4,5)
        list.forEach {
            delay(1000)
            emit(it)
        }
    }
}