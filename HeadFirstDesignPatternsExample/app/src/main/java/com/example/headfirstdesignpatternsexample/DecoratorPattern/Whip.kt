package com.example.headfirstdesignpatternsexample.DecoratorPattern

import com.example.headfirstdesignpatternsexample.Utility

class Whip(
    override var beverage: Beverage
) : CondimentDecorator() {

    var count = 0

    override var description: String
        get() {
            Utility.Log(javaClass.name, "휘핑크림 추가")
            return beverage.description + ", 휘핑크림"
//            count++
//            beverage.description.add("휘핑크림 ${count}")
//            Utility.Log(javaClass.name, "휘핑크림 추가 ${beverage.description}")
//            return beverage.description
        }
        set(value) {}

//    override fun getDescription(): String = beverage.getDescription() + ", 휘핑크림"

    override fun cost(): Double {
        var cost = beverage.cost()
        when(beverage.size) {
            Size.TALL -> {
                cost += 0.3
                Utility.Log(javaClass.name, "TALL ${cost}")
            }
            Size.GRANDE -> {
                cost += 0.4
                Utility.Log(javaClass.name, "GRANDE ${cost}")
            }
            Size.VENTI -> {
                cost += 0.5
                Utility.Log(javaClass.name, "VENTI ${cost}")
            }
            else -> {}
        }
        return cost
    }
}