package com.my.mvistudymultimodule.core.base

sealed class RequestResult<out T>(
    open val resultData: T? = null,
    open val code: String? = null,
    open val message: String? = null,
    open val throwable: Throwable? = null
) {
    data class Success<T>(override val resultData: T) : RequestResult<T>(resultData = resultData)

    data class Error<T>(
        override val code: String,
        override val message: String?
    ) : RequestResult<T>(code = code, message = message)

    data class ExceptionError<T>(
        override val throwable: Throwable
    ) : RequestResult<T>(throwable = throwable)

    data class DataEmpty<T>(
        override val message: String? = null
    ) : RequestResult<T>(message = message)
}