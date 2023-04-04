package com.example.headfirstdesignpatternsexample.DecoratorPattern

import com.example.headfirstdesignpatternsexample.Utility

/**
 *
 * */
fun main() {

    /**
     * 이 패턴의 기능을 다 나열해주는 Beverage
     * 구성(Espresso, DarkRoast)
     * 데코레이터(CondimentDecorator)
     * 데코레이터 객체(Mocha, Soy, Whip)
     *
     * Beverage를 구성과 데코레이터가 상속받아 구성 틀을 맞춘다.
     * 구성에 데코레이터 객체를 추가하는 방식이기 때문에 데코레이터 클래스에 Beverage 인스턴스 객체를 만들어주고 구성으로부터 이 객체를 받는다.
     * 구성을 데코레이터 클래스에 넘긴 후 데코레이터를 상속받는 데코레이터 객체들은 Beverage를 상속 받는 구성 객체를 이용하여 내용을 추가(cost, 이름 변경)한다.
     * */

    var beverage = Espresso() as Beverage
    Utility.Log("${beverage.description} $${beverage.cost()}")

    var beverage2 = DarkRoast() as Beverage
    beverage2.size = Beverage.Size.VENTI
    beverage2 = Mocha(beverage2)
    beverage2 = Soy(beverage2)
    beverage2 = Whip(beverage2)
    Utility.Log("${beverage2.description} $${beverage2.cost()}")

//    var beverage3 = DarkRoast() as Beverage
//    beverage3 = Mocha(beverage3)
//    beverage3 = Soy(beverage3)
//    beverage3 = Whip(beverage3)
////    beverage3 = Mocha(beverage3)
////    beverage3 = Chocolate(beverage3)
//    Utility.Log("${beverage3.description} $${beverage3.cost()}")

}