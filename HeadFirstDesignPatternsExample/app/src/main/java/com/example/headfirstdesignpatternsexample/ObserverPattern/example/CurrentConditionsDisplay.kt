package com.example.headfirstdesignpatternsexample.ObserverPattern.example

import com.example.headfirstdesignpatternsexample.Utility

class CurrentConditionsDisplay(
    private val weatherData: WeatherData
) : Observer, DisplayElement {

    private var temperature: Float = 0f
    private var humidity: Float = 0f

//    init {
//        weatherData.registerObserver(this)
//    }

    fun registerObserver() {
        weatherData.registerObserver(this)
    }

    fun removeObserver() {
        weatherData.removeObserver(this)
    }

//    override fun update(temp: Float, humidity: Float, pressure: Float) {
//        this.temperature = temp
//        this.humidity = humidity
//        display()
//    }

    override fun update() {
        this.temperature = weatherData.getTemperature()
        this.humidity = weatherData.getHumidity()
        display()
    }

    override fun display() {
        Utility.Log(javaClass.name, "현재 상태: 온도 ${temperature}F, 습도 ${humidity}%")
    }
}