package com.example.ajiekc.karoon.extensions

import java.text.SimpleDateFormat
import java.util.*

object Timezones {
    val ekb: TimeZone = TimeZone.getTimeZone("Asia/Yekaterinburg")
}

enum class DateFormat(val formatter: SimpleDateFormat) {
    D_MMMM(SimpleDateFormat("d MMMM", Locale("ru", "RU")).also { it.timeZone = Timezones.ekb }),
    D_MMMM_HH_MM(SimpleDateFormat("d MMMM, HH:mm", Locale("ru", "RU")).also { it.timeZone = Timezones.ekb }),
    D_MM_YYYY(SimpleDateFormat("d.MM.yyyy", Locale("ru", "RU")).also { it.timeZone = Timezones.ekb })
}

fun Date.format(format: DateFormat): String {
    return format.formatter.format(this)
}