package com.aman.practiceflows

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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
        GlobalScope.launch(Dispatchers.Main) {
             producer()
                 .map{
                     delay ( 100)
                    it * 2
                     Log.d("Map Thread", "${ Thread.currentThread().name }")
                 }
                 .filter {
                     Log.d("Filter Thread", "${ Thread.currentThread().name }")
                     delay(200)
                    it < 6
                 }.flowOn(Dispatchers.IO) /*  Used for context switching in Flows as
                 by default flows assume consumer producer run on same context

                 ** the code above flowOn run on the context defined on flowOn
                 */
            .collect{
                Log.d("Collected Thread", "${ Thread.currentThread().name }")
            }
        }

    }

    fun producer() = flow<Int>{
        val list = listOf(1,2,3,4,5)
        list.forEach {
            delay(1000)
            emit(it)
            Log.d("Emitter Thread", "${ Thread.currentThread().name }")
        }
    }
}