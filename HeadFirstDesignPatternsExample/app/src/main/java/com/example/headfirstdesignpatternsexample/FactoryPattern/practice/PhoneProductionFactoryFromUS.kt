package com.example.headfirstdesignpatternsexample.FactoryPattern.practice

import com.example.headfirstdesignpatternsexample.Utility

class PhoneProductionFactoryFromUS: PhoneProductionFactory {
    override fun orderPhone(type: String): Phone? {
        Utility.Log(javaClass.name, "US Factory에 휴대폰 생산 주문이 들어왔습니다.")
        return super.orderPhone(type)
    }

    override fun producePhone(type: String): Phone? {
        when(type) {
            "iPhone" -> {
                Utility.Log(javaClass.name, "iPhone From US")
                return iPhone()
            }
            "AndroidPhone" -> {
                Utility.Log(javaClass.name, "AndroidPhone From US")
                return AndroidPhone()
            }
            else -> {

            }
        }
        return null
    }
}