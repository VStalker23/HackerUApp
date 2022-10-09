package com.vladimirorlov.hackeruapp

import android.content.Context
import androidx.lifecycle.LiveData

class Repository(context: Context) {
    private val dao = PersonDatabase.getDatabase(context).getNotesDao()

    fun getAllPeopleAsLiveData(): LiveData<List<Person>> {
        return dao.getAllPeople()
    }

    fun addPerson(person: Person) {
        dao.insertPerson(person)
    }

    fun deletePerson(person: Person) {
        dao.deletePerson(person)
    }
}