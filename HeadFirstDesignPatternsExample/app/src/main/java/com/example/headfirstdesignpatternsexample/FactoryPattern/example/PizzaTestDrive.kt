package com.example.headfirstdesignpatternsexample.FactoryPattern.example

import com.example.headfirstdesignpatternsexample.FactoryPattern.example.ConcreteCreator.ChicagoPizzaStore
import com.example.headfirstdesignpatternsexample.FactoryPattern.example.ConcreteCreator.ChicagoPizzaStoreForFactoryMethod
import com.example.headfirstdesignpatternsexample.FactoryPattern.example.ConcreteCreator.NYPizzaStore
import com.example.headfirstdesignpatternsexample.FactoryPattern.example.ConcreteCreator.NYPizzaStoreForFactoryMethod
import com.example.headfirstdesignpatternsexample.Utility

fun main() {
    val TAG = "PizzaTestDrive"

    /**
     * 추상 팩토리 패턴
     * 서로 관련이 있는 객체들을 통째로 묶어서 팩토리 클래스로 만들고, 이들 팩토리를 조건에 따라 생성하도록 다시 팩토리를 만들어서 객체를 생성하는 패턴
     * NY피자를 만드는데 NY피자에 관련된 재료들만 다시 팩토리화(NYPizzaIngredientFactory)해서 사용하고,
     * 시카고피자를 만드는데 시카고 피자에 관련된 재료들만 팩토리화(ChicagoPizzaIngredientFactory)해서 사용한다.
     * 기존 팩토리 메소드 패턴에서 한단계 더 나아가 캡슐화를 진행하는 느낌
     * 장점
     * 1. 기존 제품군 안에 있는 재료들(치즈(Reggiano, Mozzarella), Dough(Thick, Thin) 등)을 공통으로 사용할 수 있기 때문에 기존 제품을 쉽게 대체할 수 있다.
     * 단점
     * 1. 완전 새로운 종류의 제품을 제공하기 어렵다. 추상 팩토리를 상속하고 있는 모든 팩토리에도 새로운 제품에 대한 구현 방법이 적용되야 하기 때문에 기존 추상 팩토리를 확장하기 쉽지 않다.
     * */
    val nyStore = NYPizzaStore()
    val chicagoStore = ChicagoPizzaStore()

    var pizza: Pizza? = nyStore.orderPizza("cheese")
    Utility.Log(TAG, "주문1: ${pizza?.getName()}")

    pizza = chicagoStore.orderPizza("cheese")
    Utility.Log(TAG, "주문2: ${pizza?.getName()}")


    /**
     * 팩토리 메소드 패턴
     * 부모 클래스에서 객체들을 생성할 수 있는 인터페이스를 제공하지만, 자식 클래스들이 생성될 객체들의 유형을 변경할 수 있도록 하는 패턴.
     * 객체 생성을 직접하지 않고, 팩토리라는 클래스에 위임하여 객체를 생성해서 반환하도록 하는 방식
     * Creator(PizzaStoreFactoryForFactoryMethod): 새로운 제품 객체들을 반환하는 팩토리 메소드를 선언. 중요한 점은 이 팩토리 메소드의 반환 유형이 제품 인터페이스와 일치해야함.
     * Concrete Creator(NYPizzaStoreForFactoryMethod): 기초 팩토리 메서드(PizzaStoreFactoryForFactoryMethod)를 오버라이드(재정의)하여 다른 유형의 제품을 반환하게 하도록 한다.
     * *** 팩토리 메서드는 항상 새로운 인스턴스들을 생성해야 할 필요가 없다. 팩토리 메서드는 기존 객체들을 캐시, 객체 풀 또는 다른 소스로부터 반환 가능 ***
     * 장점
     * 1. Creator와 Product를 결합되지 않도록 할 수 있다.
     * 2. OCP(개방, 폐쇠 원칙)을 적용하여 수정 없이 새로운 Product를 도입할 수 있다.
     * 단점
     * 1. 패턴 구현을 위하여 많은 자식 클래스들이 필요하다. (복잡)
     * */
    val nyStoreForFactoryMethod = NYPizzaStoreForFactoryMethod()
    val chicagoStoreForFactoryMethod = ChicagoPizzaStoreForFactoryMethod()

    var pizza2: PizzaForFactoryMethod? = nyStoreForFactoryMethod.orderPizza("cheese")
    Utility.Log(TAG, "주문3: ${pizza2?.getName()}")

    pizza2 = chicagoStoreForFactoryMethod.orderPizza("cheese")
    Utility.Log(TAG, "주문4: ${pizza2?.getName()}")

}