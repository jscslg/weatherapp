package com.jscode.weatherapp.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.jscode.weatherapp.util.GlideApp
import com.bumptech.glide.request.RequestOptions
import com.jscode.weatherapp.util.convertUtcToDate

@BindingAdapter("formatDateText")
fun bindText(txt: TextView, date: Long) {
    txt.text = convertUtcToDate(date)
}

@BindingAdapter("formatMinTemp")
fun bindMinTempText(txt: TextView, text: Double) {
    txt.text = String.format("Min: %.1f°C", text)
}

@BindingAdapter("formatMaxTemp")
fun bindMaxTempText(txt: TextView, text: Double) {
    txt.text = String.format("Max: %.1f°C", text)
}

@BindingAdapter("iconUrl")
fun bindImage(img: ImageView, url: String) {
    val imgUri = ("https://openweathermap.org/img/wn/${url}@2x.png").toUri().buildUpon().scheme("https").build()
    GlideApp.with(img.context)
        .load(imgUri)
        .apply(RequestOptions.circleCropTransform())
        .into(img)
}