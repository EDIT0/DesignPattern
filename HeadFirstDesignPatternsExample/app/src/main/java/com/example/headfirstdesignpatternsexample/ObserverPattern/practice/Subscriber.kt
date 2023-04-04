package com.example.headfirstdesignpatternsexample.ObserverPattern.practice

interface Subscriber {
    fun getNewPosting(bloggerSetting: BloggerSetting)
    fun getBloggerIntro(bloggerSetting: BloggerSetting)
    fun getSubUserInfo()
}