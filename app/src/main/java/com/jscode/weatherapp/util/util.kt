package com.jscode.weatherapp.util

import java.text.SimpleDateFormat
import java.util.*

fun convertUtcToDate(timestamp: Long):String {
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}