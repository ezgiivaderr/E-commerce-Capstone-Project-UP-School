package com.ezgikara.gathereality.di


import com.ezgikara.gathereality.data.repository.AuthRepository
import com.ezgikara.gathereality.data.repository.ProductRepository
import com.ezgikara.gathereality.data.source.local.ProductDao
import com.ezgikara.gathereality.data.source.remote.ProductService
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductsRepository(
        productService: ProductService,
        productDao: ProductDao
    ) = ProductRepository(productService, productDao)

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth) = AuthRepository(firebaseAuth)
}