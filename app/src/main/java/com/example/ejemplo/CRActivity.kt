package com.example.ejemplo

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*


class CRActivity : AppCompatActivity() {
    private lateinit var edtFecha : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_reporte)

        if(validarSesion()){
            callInicioSesion()
        } else {
            init()
        }
    }

    private fun init(){
        edtFecha = findViewById(R.id.edt_fecha)

        val calendar = Calendar.getInstance()
        val btnLimpieza: Button = findViewById(R.id.btn_limpieza)
        val btnMantenimiento: Button = findViewById(R.id.btn_mantenimiento)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        edtFecha.setText(dateFormat.format(Date()))


        val datePicker = DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(calendar)

        }

        val spinner: Spinner = findViewById(R.id.spn_sucursal)

        ArrayAdapter.createFromResource(
            this,
            R.array.sucursal_array,
            R.layout.spinner_layout
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_layout)
            spinner.adapter = adapter
        }

        spinner.setSelection(0)


        btnLimpieza.setOnClickListener{
            val rellenarReporte = Intent(this, RRActivity::class.java)
            rellenarReporte.putExtra("seleccion", spinner.selectedItemPosition)
            rellenarReporte.putExtra("fecha", edtFecha.text.toString())
            startActivity(rellenarReporte)
        }

        btnMantenimiento.setOnClickListener{
            val rellenarReporte = Intent(this, RRActivity2::class.java)
            rellenarReporte.putExtra("seleccion", spinner.selectedItemPosition)
            rellenarReporte.putExtra("fecha", edtFecha.text.toString())
            startActivity(rellenarReporte)
        }


        edtFecha.setOnClickListener{
            DatePickerDialog(this, datePicker, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun validarSesion():Boolean{
        val sharedPref = applicationContext.getSharedPreferences("user", 0)
        val user = sharedPref.getString("prefUser", "Usuario")
        return user.equals("Usuario")
    }

    private fun callInicioSesion(){
        val sesion = Intent(this, LoginActivity::class.java)
        startActivity(sesion)
    }

    private fun updateLable(calendar: Calendar) {
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.US)
        edtFecha.setText(sdf.format(calendar.time))

    }
}