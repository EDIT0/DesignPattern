package com.example.headfirstdesignpatternsexample.FactoryPattern.example

import com.example.headfirstdesignpatternsexample.Utility

abstract class Pizza {

    private val className: String = javaClass.name

    private var name = ""
    private lateinit var dough: Dough
    private lateinit var sauce: Sauce
    private lateinit var veggies: Array<Veggies>
    private lateinit var cheese: Cheese
    private lateinit var pepperoni: Pepperoni
    private lateinit var clam: Clams

    open fun prepare() {
//        Utility.apply {
//            Log(className, "준비 중: ${name}")
//            Log(className, "도우를 돌리는 중...")
//            Log(className, "소스를 뿌리는 중...")
//            Log(className, "토핑을 올리는 중: : ")
//            for(topping in toppings) {
//                Log(className, " ${topping}")
//            }
//        }
    }

    fun bake() {
        Utility.apply {
            Log(className, "175도에서 25분 간 굽기")
        }
    }

    open fun cut() {
        Utility.apply {
            Log(className, "피자를 사선으로 자르기")
        }
    }

    fun box() {
        Utility.apply {
            Log(className, "상자에 피자 담기")
        }
    }

    fun setName(str: String) {
        name = str
    }
    fun getName(): String = name

    fun setDough(dough: Dough) {
        this.dough = dough
    }

    fun setSauce(sauce: Sauce) {
        this.sauce = sauce
    }

    fun setVeggies(veggies: Array<Veggies>) {
        this.veggies = veggies
    }

    fun setCheese(cheese: Cheese) {
        this.cheese = cheese
    }

    fun setPepperoni(pepperoni: Pepperoni) {
        this.pepperoni = pepperoni
    }

    fun setClams(clams: Clams) {
        this.clam = clams
    }
}