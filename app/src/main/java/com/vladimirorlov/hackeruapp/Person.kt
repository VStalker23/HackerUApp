package com.vladimirorlov.hackeruapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// room using for database in sql
@Entity(tableName = "peopleTable")
data class Person(
    @ColumnInfo(name = "person_name") val name: String,
    @ColumnInfo(name = "person_image") val image: Int,
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}