package com.example.headfirstdesignpatternsexample.FactoryPattern.example.ConcreteCreator

import com.example.headfirstdesignpatternsexample.FactoryPattern.example.PizzaForFactoryMethod
import com.example.headfirstdesignpatternsexample.FactoryPattern.example.Product.ChicagoStyleCheesePizza
import com.example.headfirstdesignpatternsexample.FactoryPattern.example.Creator.PizzaStoreFactoryForFactoryMethod

class ChicagoPizzaStoreForFactoryMethod : PizzaStoreFactoryForFactoryMethod() {
    override fun createPizza(type: String): PizzaForFactoryMethod? {
        when(type) {
            "cheese" -> {
                return ChicagoStyleCheesePizza()
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