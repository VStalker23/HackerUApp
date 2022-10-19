package com.vladimirorlov.hackeruapp.ui.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.vladimirorlov.hackeruapp.ui.MainActivity
import com.vladimirorlov.hackeruapp.R
import com.vladimirorlov.hackeruapp.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        onButtonCLick()

//        binding.directSignupTv.setOnClickListener {
//            val intent = Intent(this, SignUpActivity::class.java)
//            startActivity(intent)
//            overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left)
//            finish()
//        }
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        } else {
            intent = Intent(this, LoginActivity::class.java)
        }
    }

    private fun onButtonCLick() {
        binding.continueButtonLogin.setOnClickListener{
            loginFireBaseUserWithCredentials()
        }
    }

    private fun loginFireBaseUserWithCredentials() {
        val email = findViewById<EditText>(R.id.email_login_et).text.toString()
        val password = findViewById<EditText>(R.id.password_login_et).text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) firebaseAuth.signInWithEmailAndPassword(
            email, password
        ).addOnCompleteListener {
            if (it.isSuccessful) {

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
//                overridePendingTransition(R.transition.slide_in_bottom, R.transition.slide_out_top)
                finish()

            } else Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Snackbar.make(binding.loginActivity,"Something went wrong... Please try again later",Snackbar.LENGTH_LONG).show()
        }
        else Snackbar.make(binding.loginActivity,"Email or Password Cannot be empty!",Snackbar.LENGTH_LONG).show()
    }
}