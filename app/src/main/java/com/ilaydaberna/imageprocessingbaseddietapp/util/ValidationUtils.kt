package com.ilaydaberna.imageprocessingbaseddietapp.util

import java.util.regex.Pattern

private val PASSWORD_PATTERN: Pattern = Pattern.compile("^" +
        "(?=.*[0-9])" +         //at least 1 digit
        "(?=.*[a-z])" +         //at least 1 lower case letter
        "(?=.*[A-Z])" +         //at least 1 upper case letter
        ".{4,}" +  //at least 4 characters
        "$")

private val EMAIL_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
)

private val NAME_SURNAME_PATTERN = Pattern.compile(
    "(\\w+)\\s+(\\w+)"
)

fun String.isEmailValid(): Boolean {
    if(!EMAIL_PATTERN.matcher(this).matches())
        return true
    return false
}

fun String.isPasswordValid(): Boolean {
    if(!PASSWORD_PATTERN.matcher(this).matches())
        return false
    return true
}

fun String.isAmountValid():Boolean{
    return true
}

fun String.isNameSurnameValid():Boolean{
    if(!NAME_SURNAME_PATTERN.matcher(this).matches())
        return false
    return true
}