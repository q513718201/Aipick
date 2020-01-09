package com.hazz.aipick.net


class BaseResult<T>{
    lateinit var msg: String
    var code = 0
    var data: T? = null
}