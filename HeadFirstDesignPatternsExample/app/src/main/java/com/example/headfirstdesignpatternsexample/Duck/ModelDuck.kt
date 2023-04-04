package com.example.headfirstdesignpatternsexample.Duck

import com.example.headfirstdesignpatternsexample.Utility

class ModelDuck : Duck() {

    init {
        flyBehavior = FlyNoWay()
        quackBehavior = Quack()
    }

    override fun swim() {
        Utility.Log(javaClass.name, "swim")
    }

    override fun display() {
        Utility.Log(javaClass.name, "display")
    }
}