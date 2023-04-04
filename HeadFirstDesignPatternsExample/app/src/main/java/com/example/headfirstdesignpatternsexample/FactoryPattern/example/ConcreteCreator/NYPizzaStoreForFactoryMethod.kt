package com.example.headfirstdesignpatternsexample.FactoryPattern.example.ConcreteCreator

import com.example.headfirstdesignpatternsexample.FactoryPattern.example.PizzaForFactoryMethod
import com.example.headfirstdesignpatternsexample.FactoryPattern.example.Product.NYStyleCheesePizza
import com.example.headfirstdesignpatternsexample.FactoryPattern.example.Creator.PizzaStoreFactoryForFactoryMethod

class NYPizzaStoreForFactoryMethod : PizzaStoreFactoryForFactoryMethod() {
    override fun createPizza(type: String): PizzaForFactoryMethod? {
        when(type) {
            "cheese" -> {
                return NYStyleCheesePizza()
            }
            "veggie" -> {

            }
            "clam" -> {

            }
            "pepperoni" -> {

            }
            else -> {
                return null
            }
        }
        return null
    }
}