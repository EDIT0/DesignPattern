package com.example.headfirstdesignpatternsexample.Duck

import com.example.headfirstdesignpatternsexample.Utility

class FlyWithWings : FlyBehavior {
    override fun fly() {
        Utility.Log(javaClass.name, "FlyWithWings")
    }
}