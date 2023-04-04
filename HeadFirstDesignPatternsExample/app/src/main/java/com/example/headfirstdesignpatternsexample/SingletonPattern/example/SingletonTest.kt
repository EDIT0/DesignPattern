package com.example.headfirstdesignpatternsexample.SingletonPattern.example

import com.example.headfirstdesignpatternsexample.Utility

fun main() {
    val TAG = "SingletonTest"

    /**
     * 멀티스레드를 사용할 때 문제
     * */
    Thread {
        val singleton1 = Singleton.getInstance()
        Utility.Log(TAG, "singleton1: ${singleton1.hashCode()}")
    }.start()

    Thread {
        val singleton2 = Singleton.getInstance()
        Utility.Log(TAG, "singleton2: ${singleton2.hashCode()}")
    }.start()

    /**
     * 코틀린 싱글톤
     * */
    Thread {
        val singletonWithKotlin1 = SingletonWithKotlin
        Utility.Log(TAG, "${singletonWithKotlin1.hashCode()}")
    }.start()

    Thread {
        val singletonWithKotlin2 = SingletonWithKotlin
        Utility.Log(TAG, "${singletonWithKotlin2.hashCode()}")
    }.start()

    /**
     * Enum 클래스
     * */
    val coin1 = EnumSingleton.UNIQUE_INSTANCE1
    val coin2 = EnumSingleton.UNIQUE_INSTANCE2
    val coin3 = EnumSingleton.UNIQUE_INSTANCE1

    coin1.coin = 10
    Utility.Log(TAG, "coin1: ${coin1.coin}")
    Utility.Log(TAG, "coin2: ${coin2.coin}")
    Utility.Log(TAG, "coin3: ${coin3.coin}")
    coin2.coin = 100
    coin3.coin = 999
    Utility.Log(TAG, "coin1: ${coin1.coin}")
    Utility.Log(TAG, "coin2: ${coin2.coin}")
    Utility.Log(TAG, "coin3: ${coin3.coin}")
}