package com.vladimirorlov.hackeruapp.util

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.vladimirorlov.hackeruapp.R
import com.vladimirorlov.hackeruapp.model.person.IMAGE_TYPE
import com.vladimirorlov.hackeruapp.model.person.Person
import com.vladimirorlov.hackeruapp.data.Repository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlin.concurrent.thread

object RecyclerFunctions {

    fun onPersonClick(context: Context, person: Person) {

        val dialog = MaterialAlertDialogBuilder(context)
        val dialogView: View =
            LayoutInflater.from(context).inflate(R.layout.fragment_person_card, null)

        val dialogProfileImage: ImageView = dialogView.findViewById(R.id.fragment_person_image)
        val dialogProfileName: TextView = dialogView.findViewById(R.id.fragment_person_name)
        val dialogProfileDetails: TextView = dialogView.findViewById(R.id.fragment_person_details)
        dialogProfileName.text = person.name
        dialogProfileDetails.text = person.details
        if (person.imagePath == null)
            dialogProfileImage.setImageResource(R.drawable.ic_person)
        else {
            if (person.imageType == IMAGE_TYPE.URI)
                dialogProfileImage.setImageURI(Uri.parse(person.imagePath))
            else
                Glide.with(context).load(person.imagePath).into(dialogProfileImage)
        }
        dialog.setView(dialogView)
        dialog.setCancelable(true)
        dialog.show()
    }

    fun onPersonTitleClick(view: View, context: Context, person: Person) {
        val dialog = MaterialAlertDialogBuilder(context)
        val dialogView: View =
            LayoutInflater.from(context).inflate(R.layout.fragment_person_update, null)

        dialog.setView(dialogView)
        dialog.setPositiveButton("Update") { dialog, which ->
            val dialogPersonName =
                dialogView.findViewById<TextInputEditText>(R.id.et_person_update_name).text.toString()
            val dialogPersonDetails =
                dialogView.findViewById<TextInputEditText>(R.id.et_person_update_details).text.toString()

            if (dialogPersonName.isNullOrEmpty() || dialogPersonDetails.isNullOrEmpty()) {
                Snackbar.make(
                    view,
                    "Failed to update ${person.name}! Please try again...",
                    Snackbar.LENGTH_LONG
                ).setAction("Retry") {
                    onPersonTitleClick(view, context, person)
                }.show()

            } else {
                thread(start = true) {
                    Repository.getInstance(context)
                        .updatePerson(person.id, dialogPersonName, dialogPersonDetails)
                }
                Toast.makeText(context, "${person.name} updated successfully!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        dialog.setNeutralButton("Cancel") { dialog, which -> }
        dialog.setCancelable(true)
        dialog.show()
    }

    fun isInputValid(input: String): Boolean {
        if (input.contains("[a-zA-Z]".toRegex())) {
            return true
        }
        return false
    }
}