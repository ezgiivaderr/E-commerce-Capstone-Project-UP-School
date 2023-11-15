package com.ezgikara.gathereality.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ezgikara.gathereality.data.model.response.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
abstract class ProductRoomDB: RoomDatabase() {

    abstract fun productDao(): ProductDao
}