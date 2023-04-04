package com.example.headfirstdesignpatternsexample


object Utility {
    fun Log(className: String, msg: String) {
        System.out.println("${className} $msg")
    }
    fun Log(msg: String) {
        System.out.println("${msg}")
    }
}
