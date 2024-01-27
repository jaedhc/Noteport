package com.example.ejemplo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnRegistro:TextView = findViewById(R.id.txt_registro)
        val correo:EditText = findViewById(R.id.edt_usuario)
        val contrasenia:EditText = findViewById(R.id.edt_contrasenia)

        val btnInicio:Button = findViewById(R.id.btn_completados)


        btnRegistro.setOnClickListener{
            val registro = Intent(this, SigninActivity::class.java)
            startActivity(registro)
        }

        btnInicio.setOnClickListener{
            validarDatos(correo.text.toString(), contrasenia.text.toString())
        }
    }

    private fun validarDatos(correo:String, contrasenia:String){
        var error = false
        if(correo.equals(" ") || validateEmail(correo) === false){
            error = true
        }
        if(contrasenia.equals(" ")){
            error = true
        }

        if(error == false) {
            iniciarSesion(correo, contrasenia)
        } else {
            Toast.makeText(this, "Ingrese Datos completos", Toast.LENGTH_LONG).show()
        }
    }

    private fun iniciarSesion(correo:String, contrasenia:String){
        val sharedPref = applicationContext.getSharedPreferences("user", Context.MODE_PRIVATE)
        val edit = sharedPref?.edit()
        auth = Firebase.auth

        auth.signInWithEmailAndPassword(correo, contrasenia).addOnSuccessListener { fort ->
            edit?.putString("prefUser", correo)?.commit()
            edit?.apply()
            callHome()
        }.addOnFailureListener{ fort ->
            Toast.makeText(this, "El usuario o contraseña son equivocados", Toast.LENGTH_LONG).show()
        }
    }

    private fun callHome(){
        val inicio = Intent(this, MainActivity::class.java)
        startActivity(inicio)
        finish()
    }

    private fun validateEmail(email:String):Boolean{
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }


    override fun onBackPressed() {
        //super.onBackPressed()
    }
}