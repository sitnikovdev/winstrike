package ru.prsolution.winstrike.datasource.model.payment

import com.squareup.moshi.Json

class PaymentResponse(

    @field:Json(name = "message")
    val message: String? = null,

    @field:Json(name = "redirect_url")
    val redirectUrl: String? = null
)
