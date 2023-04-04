package com.example.headfirstdesignpatternsexample.CommandPattern.receiver

import com.example.headfirstdesignpatternsexample.Utility

class CeilingFan(
    private val name: String
) {
    companion object {
        const val HIGH = 3
        const val MEDIUM = 2
        const val LOW = 1
        const val OFF = 0
    }
    private var speed = 0
//    fun on() {
//        Utility.Log(javaClass.simpleName, "${name} CeilingFan on")
//    }
//
//    fun off() {
//        Utility.Log(javaClass.simpleName, "${name} CeilingFan off")
//    }

//    var getSpeed: () -> Int = {
//        Utility.Log(javaClass.simpleName, "${name} speed : ${speed}")
//        speed
//    }

    fun getSpeed(): Int {
//        Utility.Log(javaClass.simpleName, "${name} speed : ${speed}")
        return speed
    }

//    var high : () -> Unit = {
//
//    }

    fun high() {
        Utility.Log(javaClass.simpleName, "${name} HIGH로 설정")
        speed = HIGH
    }

    fun medium() {
        Utility.Log(javaClass.simpleName, "${name} MEDIUM로 설정")
        speed = MEDIUM
    }

    fun low() {
        Utility.Log(javaClass.simpleName, "${name} LOW로 설정")
        speed = LOW
    }

    fun off() {
        Utility.Log(javaClass.simpleName, "${name} 끄기")
        speed = OFF
    }

//    var medium: () -> Unit = {
//        Utility.Log(javaClass.simpleName, "${name} MEDIUM로 설정")
//        speed = MEDIUM
//    }

//    var low: () -> Unit = {
//        Utility.Log(javaClass.simpleName, "${name} LOW로 설정")
//        speed = LOW
//    }

//    var off: () -> Unit = {
//        Utility.Log(javaClass.simpleName, "${name} 끄기")
//        speed = OFF
//    }
}