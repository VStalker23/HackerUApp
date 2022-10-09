package com.vladimirorlov.hackeruapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PeopleDao {

    // add person
    @Insert
    fun insertPerson(person: Person)

    // remove person
    @Delete
    fun deletePerson(person: Person)

    // get all list of all person in list
    @Query("Select * from peopleTable")
    fun getAllPeople(): LiveData<List<Person>>

}