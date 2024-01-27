package com.example.ejemplo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(validarSesion()){
            callInicioSesion()
        } else {
            init()
        }
    }

    private fun init(){
        val btnCrearReporte = findViewById<Button>(R.id.btn_reporte)
        val btnReporteCompletado = findViewById<Button>(R.id.btn_completados)
        val btnCerrarSesion = findViewById<Button>(R.id.btn_cerrar)


        btnCrearReporte.setOnClickListener{
            val crearReporte = Intent(this, CRActivity::class.java)
            startActivity(crearReporte)
        }

        btnReporteCompletado.setOnClickListener{
            val reporteCompleto = Intent(this, RCActivity::class.java)
            startActivity(reporteCompleto)
        }

        btnCerrarSesion.setOnClickListener{
            borrarPreferences()
            val iniciarSesion = Intent(this, LoginActivity::class.java)
            startActivity(iniciarSesion)
            finish()
        }
    }

    private fun borrarPreferences(){
        val sharedPref = applicationContext.getSharedPreferences("user", Context.MODE_PRIVATE)
        val edit = sharedPref?.edit()
        edit?.putString("prefUser", "Usuario")?.commit()
        edit?.apply()
    }

    private fun validarSesion():Boolean{
        val sharedPref = applicationContext.getSharedPreferences("user", Context.MODE_PRIVATE)
        val user = sharedPref.getString("prefUser", "Usuario")
        return user.equals("Usuario")
    }

    private fun callInicioSesion(){
        val sesion = Intent(this, LoginActivity::class.java)
        startActivity(sesion)
        finish()
    }
}