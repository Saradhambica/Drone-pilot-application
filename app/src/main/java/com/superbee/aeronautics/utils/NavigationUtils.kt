package com.superbee.aeronautics.sopform.utils

import androidx.navigation.NavController

object NavigationUtils {
    fun navigateSafe(navController: NavController, actionId: Int) {
        try {
            navController.navigate(actionId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
