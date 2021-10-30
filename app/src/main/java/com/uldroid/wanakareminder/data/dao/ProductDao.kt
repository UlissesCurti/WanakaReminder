package com.uldroid.wanakareminder.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.uldroid.wanakareminder.data.model.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun save(product: Product)

    @Delete
    suspend fun remove(product: Product)

    @Update
    suspend fun update(password: Product)

    @Query("SELECT * FROM table_products")
    fun getAll(): LiveData<List<Product>>

    @Query("SELECT * FROM table_products WHERE id == :key")
    fun get(key: Long): Product

}