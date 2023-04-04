package com.example.headfirstdesignpatternsexample.DecoratorPattern

import com.example.headfirstdesignpatternsexample.Utility
import kotlin.random.Random

class Mocha(
    override var beverage: Beverage
) : CondimentDecorator() {

//    fun Mocha(beverage: Beverage){
//        this.beverage = beverage
//    }

    var count = 0

    override var description: String
        get() {
            Utility.Log(javaClass.name, "모카 추가")
            return beverage.description + ", 모카"
//            count++
//            beverage.description.add("모카 ${count}")
//            Utility.Log(javaClass.name, "모카 추가 ${beverage.description}")
//            return beverage.description
        }
        set(value) {}

//    override fun getDescription(): String = beverage.getDescription() + ", 모카"

    override fun cost(): Double {
        var cost = beverage.cost()
        when(beverage.size) {
            Size.TALL -> {
                cost += 0.1
                Utility.Log(javaClass.name, "TALL ${cost}")
            }
            Size.GRANDE -> {
                cost += 0.2
                Utility.Log(javaClass.name, "GRANDE ${cost}")
            }
            Size.VENTI -> {
                cost += 0.3
                Utility.Log(javaClass.name, "VENTI ${cost}")
            }
            else -> {}
        }
        return cost
    }
}