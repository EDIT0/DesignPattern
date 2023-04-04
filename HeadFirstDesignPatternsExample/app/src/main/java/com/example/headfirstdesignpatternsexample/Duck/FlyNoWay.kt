package com.example.headfirstdesignpatternsexample.Duck

import com.example.headfirstdesignpatternsexample.Utility

class FlyNoWay : FlyBehavior {
    override fun fly() {
        Utility.Log(javaClass.name, "FlyNoWay")
    }
}