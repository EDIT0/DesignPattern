package com.example.headfirstdesignpatternsexample.CommandPattern.receiver

import com.example.headfirstdesignpatternsexample.Utility

class Light(
    private val name: String
) {
    fun on() {
        Utility.Log(javaClass.simpleName, "${name} Light on")
    }

    fun off() {
        Utility.Log(javaClass.simpleName, "${name} Light off")
    }
}