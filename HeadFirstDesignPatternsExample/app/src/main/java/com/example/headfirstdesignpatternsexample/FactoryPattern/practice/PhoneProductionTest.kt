package com.example.headfirstdesignpatternsexample.FactoryPattern.practice

import com.example.headfirstdesignpatternsexample.Utility

fun main() {

    val TAG = "PhoneProductionTest"

    val factoryOfSKorea = PhoneProductionFactoryFromSKorea()
    val factoryOfUS = PhoneProductionFactoryFromUS()

    val iPhoneFromUS = factoryOfUS.orderPhone("iPhone")
    iPhoneFromUS?.switchOn()
    val iPhoneFromSKorea = factoryOfSKorea.orderPhone("iPhone")
    iPhoneFromSKorea?.switchOn()
    val androidPhoneFromUS = factoryOfUS.orderPhone("AndroidPhone")
    androidPhoneFromUS?.switchOn()
    val androidPhoneFromSKorea = factoryOfSKorea.orderPhone("AndroidPhone")
    androidPhoneFromSKorea?.switchOn()
}