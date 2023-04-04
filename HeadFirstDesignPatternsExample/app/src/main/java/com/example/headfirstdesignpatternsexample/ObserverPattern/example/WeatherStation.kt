package com.example.headfirstdesignpatternsexample.ObserverPattern.example

fun main() {
    // 주제
    val weatherData = WeatherData()

    // 주제를 구독하는 디스플레이
    val currentDisplay = CurrentConditionsDisplay(weatherData)

    currentDisplay.registerObserver()
    weatherData.setMeasurements(80f, 65f, 30.4f)
    weatherData.setMeasurements(10f, 20f, 20.1f)
    currentDisplay.removeObserver()
    weatherData.setMeasurements(10f, 12f, 50.1f)
    currentDisplay.registerObserver()
    weatherData.setMeasurements(50f, 15f, 35.4f)
}