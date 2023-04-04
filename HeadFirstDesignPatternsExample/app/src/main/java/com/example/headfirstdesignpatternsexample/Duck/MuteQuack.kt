package com.example.headfirstdesignpatternsexample.Duck

import android.util.Log
import com.example.headfirstdesignpatternsexample.Utility

class MuteQuack : QuackBehavior {
    override fun quack() {
        Utility.Log(javaClass.name, "MuteQuack")
    }
}