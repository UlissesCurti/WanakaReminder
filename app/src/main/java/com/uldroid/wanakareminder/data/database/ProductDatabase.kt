package com.uldroid.wanakareminder.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uldroid.wanakareminder.data.dao.ProductDao
import com.uldroid.wanakareminder.data.model.DataConverter
import com.uldroid.wanakareminder.data.model.Product

@Database(
    entities = [Product::class],
    version = 7,
    exportSchema = false
)
@TypeConverters(DataConverter::class)
abstract class ProductDatabase : RoomDatabase() {
    abstract val productDao: ProductDao

    companion object {

        @Volatile
        private var INSTANCE: ProductDatabase? = null

        fun getInstance(context: Context): ProductDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ProductDatabase::class.java,
                        "product_db"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}