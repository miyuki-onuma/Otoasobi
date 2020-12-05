package com.myk.numa.otoasobi.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Voice(

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("duration")
    val duration: Long? = 0,

    @SerializedName("path")
    val path: String,

    @SerializedName("record_time")
    val recordTime: Long

) : Serializable
