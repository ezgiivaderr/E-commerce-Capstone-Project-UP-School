package com.ezgikara.gathereality.data.model.response

data class GetSaleProductResponse(
    val product: List<Product>?
) : BaseResponse()
