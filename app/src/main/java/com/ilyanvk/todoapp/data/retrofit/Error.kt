package com.ilyanvk.todoapp.data.retrofit

enum class Error(val code: Int) {
    REVISION(400),
    AUTHORISATION(401),
    NOT_FOUND(404),
    UNKNOWN(500)
}
