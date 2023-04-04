package com.example.headfirstdesignpatternsexample.Duck

import com.example.headfirstdesignpatternsexample.Utility

class MallardDuck : Duck() {

    init {
        flyBehavior = FlyWithWings()
        quackBehavior = Quack()
    }

    override fun swim() {
        Utility.Log(javaClass.name, "swim")
    }

    override fun display() {
        Utility.Log(javaClass.name, "display")
    }

}