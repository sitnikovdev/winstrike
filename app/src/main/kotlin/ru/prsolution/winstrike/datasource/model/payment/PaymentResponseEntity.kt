package ru.prsolution.winstrike.datasource.model.payment

import com.squareup.moshi.Json
import ru.prsolution.winstrike.domain.models.payment.PaymentResponse

class PaymentResponseEntity(

    @field:Json(name = "message")
    val message: String? = null,

    @field:Json(name = "redirect_url")
    val redirectUrl: String? = null
)

fun PaymentResponseEntity.mapToDomain(): PaymentResponse = PaymentResponse(
    message = this.message,
    redirectUrl = this.redirectUrl
)
