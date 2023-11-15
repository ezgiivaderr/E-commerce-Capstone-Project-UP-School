package com.ezgikara.gathereality.data.model.response

data class GetProductsResponse(
    val products: List<Product>?
) : BaseResponse()
