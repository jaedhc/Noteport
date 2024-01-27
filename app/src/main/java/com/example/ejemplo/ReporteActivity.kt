package com.example.ejemplo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ReporteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte)

        val id = this.intent.extras?.getString("id")
        var servicio:String
        var fecha:String
        var sucursal:String
        var areas:String
        var url:String
        var elemento:String
        var comentario:String
        var estrellas:Float

        val txtServicio = findViewById<TextView>(R.id.txt_servicio)
        val txtFecha = findViewById<TextView>(R.id.txt_fecha)
        val txtSucursal = findViewById<TextView>(R.id.txt_sucursal)
        val txtAreas = findViewById<TextView>(R.id.spn_areas)
        val txtElemento = findViewById<TextView>(R.id.spn_servicio)
        val txtComentario = findViewById<EditText>(R.id.txt_comentario)
        val imgComentario = findViewById<ImageView>(R.id.img_coment)
        val rEstrellas = findViewById<RatingBar>(R.id.ratingBar2)
        val btnEliminar = findViewById<Button>(R.id.btn_eliminar)


        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("Reportes")
        val documento = collection.document("$id")



        documento.get().addOnSuccessListener { doc ->
            servicio = doc.get("servicio").toString()
            fecha = doc.get("fecha").toString()
            sucursal = doc.get("sucursal").toString()
            areas = doc.get("area").toString()
            url = doc.get("url").toString()
            elemento = doc.get("elemento").toString()
            comentario = doc.get("comentario").toString()
            estrellas = doc.get("estrellas").toString().toFloat()

            txtServicio.text = servicio
            txtComentario.setText(comentario)
            txtFecha.text = fecha
            txtSucursal.text = sucursal
            txtAreas.text = areas
            txtElemento.text = elemento
            rEstrellas.rating = estrellas

            Picasso.get()
                .load(url)
                .error(R.drawable.iss_logo)
                .into(imgComentario)

        }


        btnEliminar.setOnClickListener{
            documento.delete()
            Toast.makeText(this, "Reporte Eliminado", Toast.LENGTH_LONG).show()
            val creados = Intent(this, MainActivity::class.java)
            creados.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(creados)
            finish()
        }



    }
}

