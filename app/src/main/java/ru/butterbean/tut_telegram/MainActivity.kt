package ru.butterbean.tut_telegram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.butterbean.tut_telegram.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding:ActivityMainBinding
    val f=123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}