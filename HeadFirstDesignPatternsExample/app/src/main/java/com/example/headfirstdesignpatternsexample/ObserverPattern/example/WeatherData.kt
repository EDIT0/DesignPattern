package com.example.headfirstdesignpatternsexample.ObserverPattern.example

class WeatherData : Subject {

    private val observers: ArrayList<Observer> by lazy {
        ArrayList<Observer>()
    }
    private var temperature: Float = 0f
    private var humidity: Float = 0f
    private var pressure: Float = 0f

    override fun registerObserver(o: Observer) {
        observers.add(o)
    }

    override fun removeObserver(o: Observer) {
        observers.remove(o)
    }

    override fun notifyObservers() {
        for(it in observers) {
//            it.update(temperature, humidity, pressure)
            it.update()
        }
    }

    fun setMeasurements(temperature: Float, humidity: Float, pressure: Float) {
        this.temperature = temperature
        this.humidity = humidity
        this.pressure = pressure
        measurementsChanged()
    }

    fun getTemperature() : Float = temperature

    fun getHumidity() = humidity

    fun getPressure() = pressure

    private fun measurementsChanged() {
        notifyObservers()
    }

}