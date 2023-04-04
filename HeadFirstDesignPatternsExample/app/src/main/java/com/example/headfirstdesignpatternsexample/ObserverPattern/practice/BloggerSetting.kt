package com.example.headfirstdesignpatternsexample.ObserverPattern.practice

class BloggerSetting(
    private val bloggerModel: BloggerModel
) : Blogger {

    private val subscriberList : ArrayList<Subscriber> by lazy {
        ArrayList<Subscriber>()
    }

    private val blogTitle = bloggerModel.blogTitle
    private val blogSubTitle = bloggerModel.blogSubTitle
    private var blogNewPosting = ""

    override fun registerUser(user: Subscriber) {
        subscriberList.add(user)
    }

    override fun removeUser(user: Subscriber) {
        subscriberList.remove(user)
    }

    override fun updatePosting() {
        for(it in subscriberList) {
            it.getSubUserInfo()
            it.getBloggerIntro(this)
            it.getNewPosting(bloggerSetting = this)
        }
    }

    fun getBlogTitle() = blogTitle
    fun getBlogSubTitle() = blogSubTitle

    fun setNewPosting(str: String) {
        blogNewPosting = str
        updatePosting()
    }

    fun getNewPosting() = blogNewPosting
}