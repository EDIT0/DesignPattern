package com.my.mvistudymultimodule.core.di

import com.my.mvistudymultimodule.core.util.LogUtil
import java.security.SecureRandom

object TestToken {

    private var token: String? = null

    fun refreshToken(): String = "aa${SecureRandom().nextInt(100)}"

    fun getToken(): String? = token

    fun saveToken(newToken: String) {
        LogUtil.d_dev("새로운 토큰 저장\n기존: ${token}\n변경: ${newToken}")
        token = newToken
    }
}