package com.example.headfirstdesignpatternsexample.ObserverPattern.practice

import com.example.headfirstdesignpatternsexample.Utility

class SubscriberSetting(
    private val subscriberModel: SubscriberModel
) : Subscriber {

    private var subNickname = subscriberModel.nickname
    private var subAge = subscriberModel.age

    override fun getNewPosting(bloggerSetting: BloggerSetting) {
        Utility.Log(javaClass.name, "${bloggerSetting.getNewPosting()}")
    }

    override fun getBloggerIntro(bloggerSetting: BloggerSetting) {
        Utility.Log(javaClass.name, "${bloggerSetting.getBlogTitle()} / ${bloggerSetting.getBlogSubTitle()}")
    }


    override fun getSubUserInfo() {
        Utility.Log(javaClass.name, "구독자 닉네임: ${subNickname}, 나이: ${subAge}에게 소식 보냄")
    }
}