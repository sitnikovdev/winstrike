package ru.prsolution.winstrike.datasource.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

import java.io.Serializable

class Wall(
		@field:Json(name = "start")
		@Expose
		val start: Start? = null,
		@field:Json(name = "end")
		@Expose
		val end: End? = null
)
