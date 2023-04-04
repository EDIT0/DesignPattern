package com.example.headfirstdesignpatternsexample.FactoryPattern.example.Creator

import com.example.headfirstdesignpatternsexample.FactoryPattern.example.Pizza

abstract class PizzaStoreFactory {
    fun orderPizza(type: String) : Pizza? {
        val pizza: Pizza? = createPizza(type)
        pizza?.let {
            it.prepare()
            it.bake()
            it.cut()
            it.box()
        }
        return pizza
    }

    protected abstract fun createPizza(type: String): Pizza?
}