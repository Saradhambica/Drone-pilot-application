package com.example.sopform.utils

object ValidationUtils {
    fun isFieldValid(value: String): Boolean {
        return value.isNotEmpty()
    }
}
