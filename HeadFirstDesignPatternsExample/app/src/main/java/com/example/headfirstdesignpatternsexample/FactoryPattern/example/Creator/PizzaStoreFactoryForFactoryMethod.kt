package com.example.headfirstdesignpatternsexample.FactoryPattern.example.Creator

import com.example.headfirstdesignpatternsexample.FactoryPattern.example.PizzaForFactoryMethod

abstract class PizzaStoreFactoryForFactoryMethod {
    fun orderPizza(type: String) : PizzaForFactoryMethod? {
        val pizza: PizzaForFactoryMethod? = createPizza(type)
        pizza?.let {
            it.prepare()
            it.bake()
            it.cut()
            it.box()
        }
        return pizza
    }

    protected abstract fun createPizza(type: String): PizzaForFactoryMethod?
}