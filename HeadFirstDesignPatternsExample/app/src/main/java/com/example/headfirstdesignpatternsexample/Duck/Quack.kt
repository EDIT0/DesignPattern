package com.example.headfirstdesignpatternsexample.Duck

import com.example.headfirstdesignpatternsexample.Utility

class Quack : QuackBehavior {
    override fun quack() {
        Utility.Log(javaClass.name, "Quack")
    }
}