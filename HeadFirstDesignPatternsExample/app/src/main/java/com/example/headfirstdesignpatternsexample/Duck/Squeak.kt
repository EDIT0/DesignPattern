package com.example.headfirstdesignpatternsexample.Duck

import android.util.Log
import com.example.headfirstdesignpatternsexample.Utility

class Squeak : QuackBehavior {
    override fun quack() {
        Utility.Log(javaClass.name, "Squeak")
    }
}