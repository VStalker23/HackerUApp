package com.vladimirorlov.hackeruapp.model.person

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Person::class], version = 1, exportSchema = false)
abstract class PersonDatabase : RoomDatabase() {

    abstract fun getNotesDao(): PeopleDao

    companion object{
        fun getDatabase(context: Context): PersonDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                PersonDatabase::class.java,
                "people_database"
            ).build()
        }
    }
}