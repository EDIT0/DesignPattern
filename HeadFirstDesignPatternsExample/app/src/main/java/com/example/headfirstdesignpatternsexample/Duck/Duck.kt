package com.example.headfirstdesignpatternsexample.Duck

abstract class Duck {

    lateinit var flyBehavior : FlyBehavior
    lateinit var quackBehavior : QuackBehavior

    abstract fun swim()
    abstract fun display()

    fun performQuack() {
        quackBehavior.quack()
    }

    fun performFly() {
        flyBehavior.fly()
    }
}