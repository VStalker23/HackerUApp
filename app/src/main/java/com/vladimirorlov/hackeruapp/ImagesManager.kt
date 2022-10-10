package com.vladimirorlov.hackeruapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

object ImagesManager {

    fun getImageFromGallery(person: Person, getContent: ActivityResultLauncher<Intent>) {
        Log.d("Test", "Click on ${person.name}")
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        getContent.launch(intent)
    }

    fun onImageResultFromGallery(
        result: ActivityResult,
        chosenNote: Person,
        context: Context
    ) {
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                addImageToNote(chosenNote, uri.toString(), IMAGE_TYPE.URI, context)
            }
        }
    }

    private fun addImageToNote(
        person: Person,
        imagePath: String,
        imageType: IMAGE_TYPE,
        context: Context
    ) {
        thread(start = true) {
            Repository.getInstance(context).updateNoteImage(person, imagePath, imageType)
        }
    }

    fun getImageFromApi(person: Person, context: Context) {
        val retrofit = ApiInterface.create()
        retrofit.getImages(person.name).enqueue(object : Callback<ApiResponseHitsList> {
            override fun onResponse(
                call: Call<ApiResponseHitsList>,
                response: Response<ApiResponseHitsList>
            ) {
                val apiResponse = response.body()
                val apiImage = apiResponse!!.imagesList[3]
                addImageToNote(person, apiImage.imageUrl, IMAGE_TYPE.URL, context)
            }

            override fun onFailure(call: Call<ApiResponseHitsList>, t: Throwable) {
                Log.e("Wrong api response", t.message.toString())
            }
        })
    }

    fun displayImagesAlertDialog(
        context: Context,
        person: Person,
        getContent: ActivityResultLauncher<Intent>
    ) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Choose an image")
        alertDialogBuilder.setMessage("Choose image for ${person.name}")
        alertDialogBuilder.setNeutralButton("Cancel") { dialogInterface: DialogInterface, i: Int -> }
        alertDialogBuilder.setPositiveButton("Gallery") { dialogInterface: DialogInterface, i: Int ->
            getImageFromGallery(person, getContent)
        }
        alertDialogBuilder.setNegativeButton("Network") { dialogInterface: DialogInterface, i: Int ->
            getImageFromApi(person, context)
        }
        alertDialogBuilder.show()
    }
}