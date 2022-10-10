package com.vladimirorlov.hackeruapp

import androidx.lifecycle.LiveData
import androidx.room.*

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

    @Update
    fun updateNote(person: Person)

    fun updatePersonImage(person: Person, imagePath: String, imageType: IMAGE_TYPE){
        person.imagePath = imagePath
        person.imageType = imageType
        updateNote(person)
    }

}