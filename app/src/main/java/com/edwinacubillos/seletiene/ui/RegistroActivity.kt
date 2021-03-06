package com.edwinacubillos.seletiene.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.edwinacubillos.seletiene.R
import com.edwinacubillos.seletiene.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registro.*

class RegistroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
    }

    fun bRegistrar_sesionClicked(view: View) {
        val email = etEmail.text.toString()
        val password = etContrasena.text.toString()
        val reppassword = etRepContrasena.text.toString()

        if (!(password == reppassword)) {
            etContrasena.error = "Las contraseñas no coinciden"
        } else {
            createUser(email, password)
        }
    }

    private fun createUser(email: String, password: String) {

        auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    createUserDatabase()
                    goToMainActivity()
                } else {
                    Log.w("Registro", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, task.exception?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun createUserDatabase() {
        // Write a message to the database
        val database = FirebaseDatabase.getInstance()
        val myRefUser = database.getReference("usuarios")

        val currentUser = auth.currentUser

        val usuario =
            Usuario(
                currentUser!!.uid,
                currentUser.email,
                "",
                "",
                ""
            )

        myRefUser.child(currentUser.uid).setValue(usuario)

    }

    private fun goToMainActivity() {
        val intent = Intent (this, MainActivity::class.java )
        startActivity(intent)
    }
}
