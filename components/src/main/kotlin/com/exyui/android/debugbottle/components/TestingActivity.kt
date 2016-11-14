//@file:Suppress("DEPRECATION")
//
//package com.exyui.android.debugbottle.components
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import com.squareup.leakcanary.internal.DisplayLeakActivity
//import com.exyui.android.debugbottle.components.floating.Floating3DService
//import com.exyui.android.debugbottle.components.injector.InjectorActivity
//import com.exyui.android.debugbottle.ui.layout.__DisplayBlockActivity
//
///**
// * Created by yuriel on 8/11/16.
// */
//@Deprecated("This activity is not in used.")
//internal class TestingActivity : Activity() {
//    val button1 by lazy {
//        val result = findViewById(R.id.__dt_button1) as Button
//        //result.setOnClickListener { startActivity(Intent(this, __ExampleActivity::class.java)) }
//        result
//    }
//
//    val button2 by lazy {
//        val result = findViewById(R.id.__dt_button2) as Button
//        result.setOnClickListener {
//            val intent = Intent(this, InjectorActivity::class.java)
//            intent.putExtra(InjectorActivity.TYPE, InjectorActivity.TYPE_ALL_ACTIVITIES)
//            startActivity(intent)
//        }
//        result
//    }
//
//    val button3 by lazy {
//        val result = findViewById(R.id.__dt_button3) as Button
//        result.setOnClickListener {
//            val intent = Intent(this, DisplayLeakActivity::class.java)
//            startActivity(intent)
//        }
//        result
//    }
//
//    val button4 by lazy {
//        val result = findViewById(R.id.__dt_button4) as Button
//        result.setOnClickListener {
//            val intent = Intent(this, __DisplayBlockActivity::class.java)
//            startActivity(intent)
//        }
//        result
//    }
//
//    val button5 by lazy {
//        val result = findViewById(R.id.__dt_button5) as Button
//        result.setOnClickListener {
//            val intent = Intent(this, Floating3DService::class.java)
//            startService(intent)
//        }
//        result
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.__activity_testing)
//        button1; button2; button3; button4; button5
//    }
//}