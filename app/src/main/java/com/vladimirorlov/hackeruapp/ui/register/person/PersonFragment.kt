package com.vladimirorlov.hackeruapp.ui.register.person



import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
import com.vladimirorlov.hackeruapp.R
import com.vladimirorlov.hackeruapp.databinding.FragmentPersonListBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform


class PersonFragment() : Fragment() {

    private lateinit var binding: FragmentPersonListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPersonListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            binding.closeButton.setOnClickListener {
                findNavController().navigateUp()
                Snackbar.make(view, "Closed!", Snackbar.LENGTH_LONG)
                    .show()
            }

            // Set transitions here so we are able to access Fragment's binding views.
            enterTransition = MaterialContainerTransform().apply {
                // Manually add the Views to be shared since this is not a standard Fragment to
                // Fragment shared element transition.
                startView = requireActivity().findViewById(R.id.fab)
                endView = binding.personFragment
                duration = 300L
                scrimColor = Color.TRANSPARENT
//                containerColor = requireContext().themeColor(R.attr.colorSurface)
//                startContainerColor = requireContext().themeColor(R.attr.colorSecondary)
//                endContainerColor = requireContext().themeColor(R.attr.colorSurface)
            }
            returnTransition = Slide().apply {
                duration = 225L
                addTarget(R.id.person_fragment)
            }
        }
    }
}