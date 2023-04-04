package com.example.headfirstdesignpatternsexample.DecoratorPattern

class DarkRoast: Beverage() {

    init {
//        description.add("다크 로스트")
        description = "다크 로스트"
    }

    override fun cost(): Double = 3.5
}