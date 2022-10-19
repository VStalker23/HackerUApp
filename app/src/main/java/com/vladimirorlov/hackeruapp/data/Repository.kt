package com.vladimirorlov.hackeruapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.vladimirorlov.hackeruapp.model.person.IMAGE_TYPE
import com.vladimirorlov.hackeruapp.model.person.Person
import com.vladimirorlov.hackeruapp.model.person.PersonDatabase

class Repository private constructor(context: Context) {
    private val dao = PersonDatabase.getDatabase(context).getNotesDao()

    companion object{
        private lateinit var instance: Repository

        fun getInstance(context: Context) : Repository {
            if (!Companion::instance.isInitialized){
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

    fun updatePerson(id : Int , personName: String, personDetails: String){
        return dao.updatePersonById(id , personName, personDetails)
    }

    fun updatePersonImage(person: Person, imagePath: String, imageType: IMAGE_TYPE) {
        dao.updatePersonImage(person, imagePath, imageType)
    }
}