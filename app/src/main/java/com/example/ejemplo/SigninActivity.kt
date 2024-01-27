package com.example.ejemplo

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SigninActivity : AppCompatActivity() {

    lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val correo: EditText = findViewById(R.id.edt_usuario)
        val pass: EditText = findViewById(R.id.edt_contrasenia)
        val passRep: EditText = findViewById(R.id.edt_contrasenia_rep)
        val texto:TextView = findViewById(R.id.textView)

        val btnRegistro: Button = findViewById(R.id.btn_completados)


        btnRegistro.setOnClickListener{
            validarDatos(correo.text.toString(), pass.text.toString(), passRep.text.toString())
        }
    }


    private fun validarDatos(correo:String, contrasenia:String, contraseniaRep: String){
        var error = false
        if(correo.isEmpty() || contrasenia.isEmpty() || contraseniaRep.isEmpty()){
            Toast.makeText(this, "Verifique todos los campos", Toast.LENGTH_LONG).show()
            error = true
        }else if(!validateEmail(correo)){
            Toast.makeText(this, "Verifique el campo de correo", Toast.LENGTH_LONG).show()
            error = true
        }
        if(!contraseniaRep.equals(contrasenia)){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
            error = true
        }

        if(error == false) {
            crearUsuario(correo, contrasenia)
            callInicioSesion()
        } else {
        }
    }

    private fun crearUsuario(correo:String, contrasenia:String){
        auth = Firebase.auth

        auth.createUserWithEmailAndPassword(correo, contrasenia).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d(ContentValues.TAG, "createUserWithEmail:success")
                Toast.makeText(this, "Registrado exitosamente", Toast.LENGTH_LONG).show()
            } else {
                Log.w(ContentValues.TAG, "createUserWithEmail:failure", it.exception)
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun callInicioSesion(){
        val inicio = Intent(this, LoginActivity::class.java)
        startActivity(inicio)
    }

    private fun validateEmail(email:String):Boolean{
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }



}