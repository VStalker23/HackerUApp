package com.vladimirorlov.hackeruapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class IMAGE_TYPE{
    URI, URL
}
// room using for database in sql
@Entity(tableName = "peopleTable")
data class Person(
    @ColumnInfo(name = "person_name") val name: String,
    @ColumnInfo(name = "person_image") val image: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image_path") var imagePath: String? = null,
    @ColumnInfo(name = "image_type") var imageType: IMAGE_TYPE? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}