package com.superbee.aeronautics

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Farmer(
    val id: String,
    val Name: String,
    val Phone: String,
    val State: String,
    val District: String,
    val Village: String,
    val Pincode: String
) : Parcelable
