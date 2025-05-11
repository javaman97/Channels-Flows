package com.aman.practiceflows

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

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
            val time = measureTimeMillis {
                producer().buffer(3) /*
                 Buffer Operator to store emitted value in buffer
                  when producer is producing more fast than consumer to consume it
              */
                    .collect {
                        delay(1500)
                        Log.d("Collect Flow", it.toString())
                    }
            }
            Log.d("Time with Buffer", time.toString())
            /*
             Approximate  time without buffer is around 1250  but with
             buffer it is around 800
            *  */
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