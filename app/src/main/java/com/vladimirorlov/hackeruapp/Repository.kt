package com.vladimirorlov.hackeruapp

import android.content.Context
import androidx.lifecycle.LiveData

class Repository(applicationContext: Context) {
    private val dao = PersonDatabase.getDatabase(applicationContext).getNotesDao()

    companion object{
        private lateinit var instance:Repository

        fun getInstance(context:Context): Repository{
            if(!::instance.isInitialized){
                instance = Repository(context)
            }
            return instance
        }
    }

    fun getAllPeopleAsLiveData(): LiveData<List<Person>> {
        return dao.getAllPeople()
    }

    fun addPerson(person: Person) {
        dao.insertPerson(person)
    }

    fun deletePerson(person: Person) {
        dao.deletePerson(person)
    }

    fun updateNoteImage(person: Person, uri: String, imageType: IMAGE_TYPE) {
        dao.updatePersonImage(person, uri, imageType)
    }

}