package com.uldroid.wanakareminder.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uldroid.wanakareminder.ui.adapter.ReminderAdapter
import java.util.*

@Entity(tableName = "table_products")
data class Product(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0L,
    val name: String,
    var nextDates: List<Calendar>? = null,
    var expanded: Boolean = false
)

class DataConverter {

    @TypeConverter
    fun fromNextDatesList(value: List<Calendar>?): String? {
        val gson = Gson()
        val type = object : TypeToken<List<Calendar>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toNextDatesList(value: String?): List<Calendar>? {
        val gson = Gson()
        val type = object : TypeToken<List<Calendar>>() {}.type
        return gson.fromJson(value, type)
    }
}

fun List<Product>.toReminderAdapterDataset(): List<ReminderAdapter.Dataset> {
    val dataset = mutableListOf<ReminderAdapter.Dataset>()
    forEach {
        dataset.add(
            ReminderAdapter.Dataset(
                product = Product(
                    it.id,
                    it.name,
                    it.nextDates,
                    it.expanded
                )
            )
        )
    }
    return dataset
}