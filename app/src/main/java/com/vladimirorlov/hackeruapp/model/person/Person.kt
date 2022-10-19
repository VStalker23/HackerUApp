package com.vladimirorlov.hackeruapp.model.person

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class IMAGE_TYPE {
    URI, URL, BMP
}

@Entity(tableName = "peopleTable")
data class Person(
    @ColumnInfo(name = "person_name") var name: String,
    @ColumnInfo(name = "person_details") var details: String? = null,
    @ColumnInfo(name = "image_path") var imagePath: String? = null,
    @ColumnInfo(name = "image_type") var imageType: IMAGE_TYPE? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}