package com.example.headfirstdesignpatternsexample.DecoratorPattern

import com.example.headfirstdesignpatternsexample.Utility

abstract class Beverage {
//    open var description: String = "제목 없음"
//        get() {
//            return field
//        }
//        set(value) {
//            field = value
//        }

    enum class Size {
        TALL, GRANDE, VENTI
    }

    open var description: String = "제목 없음"
        get() {
            Utility.Log(javaClass.name, "description get()")
            return field
        }
        set(value) {
            Utility.Log(javaClass.name, "description set()")
            field = value
        }

    open var size : Size = Size.TALL
        get() {
            return field
        }
        set(value) {
            field = value
        }
//    private var description = "제목 없음"
//
//    open fun setDescription(str : String) {
//        description = str
//    }
//    open fun getDescription() : String = description

    abstract fun cost(): Double
}