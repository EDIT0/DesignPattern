package com.example.headfirstdesignpatternsexample.FactoryPattern.example.Product

import com.example.headfirstdesignpatternsexample.FactoryPattern.example.PizzaForFactoryMethod
import com.example.headfirstdesignpatternsexample.Utility

class ChicagoStyleCheesePizza: PizzaForFactoryMethod() {
    init {
        setName("시카고 스타일 딥 디쉬 치즈 피자")
        setDough("아주 두꺼운 크러스트 도우")
        setSauce("플럼토마토 소스")

        getToppings().add("잘게 썬 레지아노 치즈")
    }
    override fun cut() {
        Utility.Log(javaClass.name, "네모난 모양으로 피자 자르기")
    }
}