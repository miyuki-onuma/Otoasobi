package com.myk.numa.otoasobi.util

import org.joda.time.DateTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Convert "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" or  "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'" to DateTime
 * Example : 2020-10-12T06:40:27.136Z -> "2020-10-12 13:40"
 */
fun String.parseRFC3339Date(): DateTime {
    return  try {
        DateTime(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.parse(this))
    } catch (ex: ParseException){
        DateTime(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
            isLenient  = true
        }.parse(this))
    }
}