package com.example.headfirstdesignpatternsexample.ObserverPattern.practice


/**
 * 옵저버 패턴
 * 1. 객체들 사이에 일대다 관계 정의
 * 2. 주제는 동일한 인터페이스를 사용해서 옵저버에게 연락
 * 3. 옵저버(Subscriber) 인터페이스를 구현하면 사용 가능
 * 4. 주제는 옵저버 인터페이스를 구현한다는 것을 제외하고는 옵저버에 대해 아무것도 모름 (느슨한 결합)
 * 5. pull(옵저버가 주제로부터 데이터를 가져옴), push(주제가 옵저버에게 데이터를 보냄) 방식이 있음
 * */
fun main() {
    val bloggerMike = BloggerSetting(BloggerModel("마이크 블로그", "비행기에 흥미있음"))
    val bloggerJohn = BloggerSetting(BloggerModel("존 블로그", "펫 관심"))

    val subscriberAnn = SubscriberSetting(SubscriberModel("앤", 10))
    val subscriberJeon = SubscriberSetting(SubscriberModel("춘식", 20))

    registrationBlog(bloggerMike, subscriberAnn) // 앤이 마이크 블로그 구독
    bloggerMike.setNewPosting("안녕하세요. 마이크입니다. 저의 첫 포스팅입니다.\n")
    registrationBlog(bloggerMike, subscriberJeon) // 전이 마이크 블로그 구독
    bloggerMike.setNewPosting("안녕하세요. 마이크입니다. 저의 두번째 포스팅입니다.\n")

    bloggerJohn.setNewPosting("안녕하세요. 존입니다. 저의 첫 포스팅입니다.\n")
    bloggerJohn.setNewPosting("안녕하세요. 존입니다. 저의 두번째 포스팅입니다.\n")
    registrationBlog(bloggerJohn, subscriberJeon) // 전이 존 블로그 구독
    bloggerJohn.setNewPosting("안녕하세요. 존입니다. 저의 세번째 포스팅입니다.\n")
}

fun registrationBlog(blogger: BloggerSetting, subscriber: SubscriberSetting) {
    blogger.registerUser(subscriber)
}