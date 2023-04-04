package com.example.headfirstdesignpatternsexample.Duck

fun main() {
    val mallardDuck = MallardDuck()

    mallardDuck.swim()
    mallardDuck.display()

    mallardDuck.performFly()
    mallardDuck.performQuack()


    val modelDuck = ModelDuck()
    modelDuck.apply {
        performFly()
        performQuack()
        flyBehavior = FlyWithWings()
        performFly()
        quackBehavior = MuteQuack()
        performQuack()
    }
}