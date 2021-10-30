package com.uldroid.wanakareminder.data.repository

import androidx.lifecycle.LiveData
import com.uldroid.wanakareminder.data.dao.ProductDao
import com.uldroid.wanakareminder.data.model.Product

class ProductRepository(private val productDao: ProductDao) {

    val products: LiveData<List<Product>>
        get() = productDao.getAll()

    suspend fun save(newProduct: Product) {
        productDao.save(newProduct)
    }

    suspend fun update(product: Product) {
        productDao.update(product)
    }

    suspend fun delete(product: Product) {
        productDao.remove(product)
    }

    fun get(product: Product): Product {
        return productDao.get(product.id)
    }

    companion object {
        val items = mutableListOf(
            Product(0L, "Koi Fish"),// total = 18h  water/feed  0h - 4h - 8h - 12h
            Product(1L, "Apple"),// total = 12h  water/feed  0h - 4h - 6h - 8h
            Product(2L, "Cow"),// total = 24h  water/feed  0h - 4h - 8h - 12h - 16h
            Product(3L, "Cabbage")// total = 6h  water/feed  0h - 2h - 4h
        )
        val feeding: HashMap<Long, List<Int>> = hashMapOf<Long, List<Int>>().apply {
            put(0L, listOf(0, 4, 8, 12, 18))
            put(1L, listOf(0, 4, 6, 8, 12))
            put(2L, listOf(0, 4, 8, 12, 16, 24))
            put(3L, listOf(0, 2, 4, 6))
        }
    }
}