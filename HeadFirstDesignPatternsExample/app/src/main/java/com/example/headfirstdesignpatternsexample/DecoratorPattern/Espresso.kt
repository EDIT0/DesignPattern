package com.example.headfirstdesignpatternsexample.DecoratorPattern

class Espresso: Beverage() {

    init {
//        description.add("에스프레소")
        description = "에스프레소"
    }

    override fun cost(): Double = 1.99
}