package com.example.headfirstdesignpatternsexample.ObserverPattern.example

interface Subject {
    fun registerObserver(o: Observer)
    fun removeObserver(o: Observer)
    fun notifyObservers()
}