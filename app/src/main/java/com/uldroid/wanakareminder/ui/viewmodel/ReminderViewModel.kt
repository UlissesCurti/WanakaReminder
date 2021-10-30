package com.uldroid.wanakareminder.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uldroid.wanakareminder.data.model.Product
import com.uldroid.wanakareminder.data.repository.ProductRepository
import kotlinx.coroutines.launch
import java.util.*

class ReminderViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _productList: LiveData<List<Product>> =
        productRepository.products

    val productList: LiveData<List<Product>>
        get() = _productList

    fun getProducts() {
        viewModelScope.launch {
            ProductRepository.items.forEach {
                productRepository.save(it)
            }
        }
    }

    fun start(product: Product) {
        viewModelScope.launch {
            val now = Calendar.getInstance()
            val nextDates = mutableListOf<Calendar>()

            ProductRepository.feeding[product.id]?.forEach {
                val next = Calendar.getInstance()
                next.timeInMillis = now.timeInMillis
                next.add(Calendar.HOUR, it)

                nextDates.add(next)
            }

            product.nextDates = nextDates

            productRepository.update(product)
        }
    }

    fun stop(product: Product) {
        viewModelScope.launch {
            product.nextDates = null
            product.expanded = false
            productRepository.update(product)
        }
    }

    fun expand(product: Product) {
        viewModelScope.launch {
            productRepository.update(product.apply {
                this.expanded = !this.expanded
            })
        }
    }
}