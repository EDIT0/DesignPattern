package com.example.headfirstdesignpatternsexample.FactoryPattern.example.ConcreteCreator

import com.example.headfirstdesignpatternsexample.FactoryPattern.example.Pizza
import com.example.headfirstdesignpatternsexample.FactoryPattern.example.NYPizzaIngredientFactory
import com.example.headfirstdesignpatternsexample.FactoryPattern.example.Product.CheesePizza
import com.example.headfirstdesignpatternsexample.FactoryPattern.example.Product.ClamPizza
import com.example.headfirstdesignpatternsexample.FactoryPattern.example.Creator.PizzaStoreFactory

class NYPizzaStore : PizzaStoreFactory() {
    override fun createPizza(type: String): Pizza? {
        var pizza: Pizza? = null
        val ingredientFactory = NYPizzaIngredientFactory()
        when(type) {
            "cheese" -> {
                pizza = CheesePizza(ingredientFactory)
                pizza.setName("뉴욕 스타일 치즈 피자")
                return pizza
            }
            "veggie" -> {

            }
            "clam" -> {
                pizza = ClamPizza(ingredientFactory)
                pizza.setName("뉴욕 스타일 조개 피자")
                return pizza
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