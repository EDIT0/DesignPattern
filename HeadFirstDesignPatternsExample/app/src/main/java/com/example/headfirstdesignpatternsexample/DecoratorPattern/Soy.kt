package com.example.headfirstdesignpatternsexample.DecoratorPattern

import com.example.headfirstdesignpatternsexample.Utility

class Soy(
    override var beverage: Beverage
) : CondimentDecorator() {

    var count = 0

    override var description: String
        get() {
            Utility.Log(javaClass.name, "두유 추가")
            return beverage.description + ", 두유"
//            count++
//            beverage.description.add("두유 ${count}")
//            Utility.Log(javaClass.name, "두유 추가 ${beverage.description}")
//            return beverage.description
        }
        set(value) {}

//    override fun getDescription(): String = beverage.getDescription() + ", 두유"

    override fun cost(): Double {
        var cost = beverage.cost()
        when(beverage.size) {
            Size.TALL -> {
                cost += 0.2
                Utility.Log(javaClass.name, "TALL ${cost}")
            }
            Size.GRANDE -> {
                cost += 0.3
                Utility.Log(javaClass.name, "GRANDE ${cost}")
            }
            Size.VENTI -> {
                cost += 0.4
                Utility.Log(javaClass.name, "VENTI ${cost}")
            }
            else -> {}
        }
        return cost
    }

}