package com.example.headfirstdesignpatternsexample.DecoratorPattern

abstract class CondimentDecorator : Beverage() {
    open lateinit var beverage: Beverage
    override var size: Size
        get() = beverage.size
        set(value) {}
    override var description: String
        get() = beverage.description
        set(value) {}

//    override fun getDescription(): String {
//        return super.getDescription()
//    }
//    override fun getDescription(): String {
//        return beverage.getDescription()
//    }
}