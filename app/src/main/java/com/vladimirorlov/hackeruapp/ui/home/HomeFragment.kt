package com.vladimirorlov.hackeruapp.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vladimirorlov.hackeruapp.R
import com.vladimirorlov.hackeruapp.data.Repository
import com.vladimirorlov.hackeruapp.databinding.FragmentHomeBinding
import com.vladimirorlov.hackeruapp.model.person.Person
import com.vladimirorlov.hackeruapp.ui.register.person.PersonAdapter
import com.vladimirorlov.hackeruapp.util.ImageManager
import com.vladimirorlov.hackeruapp.util.RecyclerFunctions
import com.vladimirorlov.hackeruapp.util.RecyclerFunctions.onPersonTitleClick
import com.vladimirorlov.hackeruapp.util.SwipeToDeleteCallBack
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*


class HomeFragment : Fragment() {

    private var person: Person? = null
    private lateinit var adapter: PersonAdapter
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PersonAdapter(
            arrayListOf(),
            onPersonTitleClick(),
            onPersonImageClick(),
            onPersonCardClick(),
            requireContext().applicationContext
        )
        createRecyclerView()
        adapter.notifyDataSetChanged()
    }

    private fun onPersonTitleClick(): (Person) -> Unit = { person ->
        onPersonTitleClick(requireView(), requireContext(), person)
    }


    private fun onPersonCardClick(): (Person) -> Unit = { person ->
        RecyclerFunctions.onPersonClick(requireContext(), person)
    }

//    private fun requestCameraPermission(context: Context) {
//        when {
//            ContextCompat.checkSelfPermission(
//                context,
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_DENIED ||
//                    ContextCompat.checkSelfPermission(
//                        context,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    ) == PackageManager.PERMISSION_DENIED -> Log.i("Log", "Permission Granted")
//
//            ActivityCompat.shouldShowRequestPermissionRationale(
//                requireActivity(),
//                Manifest.permission.CAMERA
//            ) -> Log.i("Log", "Show Camera Permission Dialog")
//
//            else -> requestPermission.launch(Manifest.permission.CAMERA)
//        }
//    }

    private val getContentFromCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            ImageManager.onImageResultFromCamera(result, person!!, requireContext())
        }

    private val getContentFromGallery =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            ImageManager.onImageResultFromGallery(result, person!!, requireContext())
        }

//    private val requestPermission =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//            if (isGranted) {
//                requestCameraPermission(requireContext())
////                ImageManager.takePicture(person!!, getContentFromCamera)
//
//            } else {
//                "Access Denied"
//            }
//        }

    private fun onPersonImageClick(): (person: Person) -> Unit = {

        person = it

        val dialog = MaterialAlertDialogBuilder(requireContext())
        dialog.setTitle("Choose an image")
        dialog.setMessage("Choose image for ${person!!.name}")

        dialog.setNeutralButton("Camera") { _: DialogInterface, _: Int ->
//            requestPermission
            ImageManager.takePicture(person!!, getContentFromCamera)
        }
        dialog.setPositiveButton("Gallery") { _: DialogInterface, _: Int ->

            ImageManager.getImageFromGallery(person!!, getContentFromGallery)
        }
        dialog.setNegativeButton("Network") { _: DialogInterface, _: Int ->
            ImageManager.getImageFromApi(person!!, requireActivity())
        }
        dialog.show()
    }

    @OptIn(DelicateCoroutinesApi::class)
    val swipeToDeleteCallBack = object : SwipeToDeleteCallBack() {
        @SuppressLint("UseRequireInsteadOfGet")
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            val position = viewHolder.adapterPosition
            val personList = adapter.dataList
            val person = adapter.dataList[position]

            personList.removeAt(position)
            adapter.notifyItemRemoved(position)

            GlobalScope.launch(Dispatchers.IO) {
                Repository.getInstance(requireContext()).deletePerson(person)
            }

            Snackbar.make(
                binding.homeFragment,
                "You removed ${person.name}!",
                Snackbar.LENGTH_LONG
            )
                .setAction("Undo") {
                    personList.add(position, person)
                    adapter.notifyItemInserted(position)

                    GlobalScope.launch(Dispatchers.IO) {
                        Repository.getInstance(requireContext()).addPerson(person)
                    }
                }.show()
        }
    }

    private fun createRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)

        if (recyclerView != null) {
            recyclerView.layoutManager = layoutManager
        }
        recyclerView?.adapter = adapter
        val peopleListLiveData = Repository.getInstance(requireContext()).getAllPeopleAsLiveData()
        peopleListLiveData.observe(viewLifecycleOwner) { personList ->
            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
            itemTouchHelper.attachToRecyclerView(recyclerView)
            adapter.updateRecyclerView(personList)
        }
    }
}