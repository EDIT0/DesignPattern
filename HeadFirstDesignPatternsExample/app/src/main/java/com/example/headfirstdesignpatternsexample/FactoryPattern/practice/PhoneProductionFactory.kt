package com.example.headfirstdesignpatternsexample.FactoryPattern.practice

interface PhoneProductionFactory {
    fun orderPhone(type: String) : Phone? {
        val phone: Phone? = producePhone(type)
        phone?.let {
            it.check()
        }
        return phone
    }

    fun producePhone(type: String): Phone?
}