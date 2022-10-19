package com.vladimirorlov.hackeruapp.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.makeText
import com.vladimirorlov.hackeruapp.R
import com.vladimirorlov.hackeruapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.vladimirorlov.hackeruapp.ui.register.LoginActivity
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        goToLoginActivityOnClick()

        binding.directToLoginTv.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left)
            finish()

        }
    }

    private fun goToLoginActivityOnClick() {
        binding.continueButtonSignup.setOnClickListener {
            createFirebaseUserWithCredentials()
        }
    }

    private fun createFirebaseUserWithCredentials() {
        val email = email_signup_et.text.toString()
        val password = password_signup_et.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) firebaseAuth.createUserWithEmailAndPassword(
            email, password
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                makeText(this, "Signed up successfully!", Toast.LENGTH_LONG).show()

            } else makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
        }
        else makeText(this, "Email or Password Cannot be empty!", Toast.LENGTH_SHORT).show()
    }

}
