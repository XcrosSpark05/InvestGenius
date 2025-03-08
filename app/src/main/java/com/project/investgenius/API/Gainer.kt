package com.project.investgenius.API

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gainer(
    val symbol: String,
    val company_name: String,
    val price: Double,
    val price_change: Double,
    val imageUrl: String
) : Parcelable
