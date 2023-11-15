package com.ezgikara.gathereality.data.repository

import com.ezgikara.gathereality.common.Resource
import com.ezgikara.gathereality.data.mapper.mapProductEntityToProductUI
import com.ezgikara.gathereality.data.mapper.mapProductToProductUI
import com.ezgikara.gathereality.data.mapper.mapToProductEntity
import com.ezgikara.gathereality.data.mapper.mapToProductUI
import com.ezgikara.gathereality.data.model.request.AddToCartRequest
import com.ezgikara.gathereality.data.model.request.ClearCartRequest
import com.ezgikara.gathereality.data.model.request.DeleteFromCartRequest
import com.ezgikara.gathereality.data.model.response.BaseResponse
import com.ezgikara.gathereality.data.model.response.ProductUI
import com.ezgikara.gathereality.data.source.local.ProductDao
import com.ezgikara.gathereality.data.source.remote.ProductService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository (
    private val productService:ProductService,
    private val productDao: ProductDao
) {
    suspend fun getProducts(): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.getProducts().body()
                val favorites = productDao.getProductIds()

                if (response?.status == 200) {
                    Resource.Success(response.products.orEmpty().mapProductToProductUI(favorites))
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }
    suspend fun getSaleProducts(): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.getSaleProducts().body()
                val favorites = productDao.getProductIds()

                if (response?.status == 200 && response.products != null) {
                    Resource.Success(response.products.mapProductToProductUI(favorites))
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }
    suspend fun getProductDetail(id: Int): Resource<ProductUI> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.getProductDetail(id).body()
                val favorites = productDao.getProductIds()

                if (response?.status == 200 && response.product != null) {
                    Resource.Success(response.product.mapToProductUI(favorites))
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }
    suspend fun getCartProducts(userId: String): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.getCartProducts(userId).body()
                val favorites = productDao.getProductIds()

                if (response?.status == 200) {
                    Resource.Success(response.products.orEmpty().mapProductToProductUI(favorites))
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }
    suspend fun addToCart(userId: String, productId: Int): Resource<BaseResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.addToCart(AddToCartRequest(userId, productId)).body()

                if (response?.status == 200) {
                    Resource.Success(response)
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }
    suspend fun deleteFromCart(userId: String, productId: Int): Resource<BaseResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.deleteFromCart(DeleteFromCartRequest(userId, productId)).body()

                if (response?.status == 200) {
                    Resource.Success(response)
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }
    suspend fun clearCart(userId: String) : Resource<BaseResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.clearCart(ClearCartRequest(userId)).body()
                if (response?.status == 200) {
                    Resource.Success(response)
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }
    suspend fun getFavorites(): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val products = productDao.getProducts()

                if (products.isEmpty()) {
                    Resource.Fail("Products not found")
                } else {
                    Resource.Success(products.mapProductEntityToProductUI())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }
    suspend fun searchProduct(query : String): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.searchProduct(query).body()
                val favorites = productDao.getProductIds()

                if (response?.status == 200) {
                    Resource.Success(response.products.orEmpty().mapProductToProductUI(favorites))
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }
    suspend fun addToFavorites(productUI: ProductUI) {
        productDao.addProduct(productUI.mapToProductEntity())
    }

    suspend fun deleteFromFavorites(productUI: ProductUI) {
        productDao.deleteProduct(productUI.mapToProductEntity())
    }
    suspend fun clearFavorites() {
        productDao.clearFavorites()
    }
}