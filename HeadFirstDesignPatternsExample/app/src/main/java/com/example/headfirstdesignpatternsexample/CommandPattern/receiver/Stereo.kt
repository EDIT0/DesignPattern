package com.example.headfirstdesignpatternsexample.CommandPattern.receiver

import com.example.headfirstdesignpatternsexample.Utility

class Stereo(
    private val name: String
) {
    fun on() {
        Utility.Log(javaClass.simpleName, "${name} Stereo on")
    }

    fun off() {
        Utility.Log(javaClass.simpleName, "${name} Stereo off")
    }

    fun setCD() {
        Utility.Log(javaClass.simpleName, "${name} setCD")
    }

    fun setVolume(volume: Int) {
        Utility.Log(javaClass.simpleName, "${name} Stereo volume ${volume}")
    }
}