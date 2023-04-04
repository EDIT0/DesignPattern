package com.example.headfirstdesignpatternsexample.ObserverPattern.practice

interface Blogger {
    fun registerUser(user: Subscriber)
    fun removeUser(user: Subscriber)
    fun updatePosting()
}