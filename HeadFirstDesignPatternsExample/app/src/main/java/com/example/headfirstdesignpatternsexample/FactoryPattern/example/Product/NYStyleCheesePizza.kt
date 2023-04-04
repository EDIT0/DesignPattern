package com.example.headfirstdesignpatternsexample.FactoryPattern.example.Product

import com.example.headfirstdesignpatternsexample.FactoryPattern.example.PizzaForFactoryMethod

class NYStyleCheesePizza: PizzaForFactoryMethod() {
    init {
        setName("뉴욕 스타일 소스와 치즈 피자")
        setDough("씬 크러스트 도우")
        setSauce("마리나라 소스")

        getToppings().add("잘게 썬 레지아노 치즈")
    }
}