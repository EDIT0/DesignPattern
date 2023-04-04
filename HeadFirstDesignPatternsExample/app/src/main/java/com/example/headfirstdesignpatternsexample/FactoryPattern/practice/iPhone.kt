package com.example.headfirstdesignpatternsexample.FactoryPattern.practice

import com.example.headfirstdesignpatternsexample.Utility

class iPhone: Phone {
    override fun check() {
        Utility.Log(javaClass.name, "아이폰 휴대폰 검수 ${this.hashCode()}")
    }

    override fun switchOn() {
        Utility.Log(javaClass.name, "아이폰 휴대폰 전원 켜기")
    }

    override fun switchOff() {
        Utility.Log(javaClass.name, "아이폰 휴대폰 전원 끄기")
    }
}