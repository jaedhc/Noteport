package com.example.ejemplo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ejemplo.adapter.ReportesAdapter
import com.google.firebase.firestore.FirebaseFirestore

class RCActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportes_completados)

        val sharedPref = applicationContext.getSharedPreferences("user", Context.MODE_PRIVATE)
        val user = sharedPref.getString("prefUser", "Usuario")

        user?.let { obtenerReportes(it) }

    }


    private fun obtenerReportes(usuario:String){
        val db = FirebaseFirestore.getInstance()
        var url:String
        var servicio:String
        var area:String
        var estrellas:Float
        var sucursal:String
        var documento:Int

        val collection = db.collection("Reportes")
        val reportesList: MutableList<ListReports> = mutableListOf()

        collection.whereEqualTo("correo", usuario).get().addOnSuccessListener { n ->
            for(document in n){
                servicio = document.get("servicio").toString()
                area = document.get("area").toString()
                estrellas = document.get("estrellas").toString().toFloat()
                url = document.get("url").toString()
                sucursal = document.get("sucursal").toString()
                documento = document.id.toInt()
                reportesList.add(ListReports(url, servicio, estrellas, sucursal, area, documento))

                initRecyclerView(reportesList)

            }
        }

    }


    private fun initRecyclerView(reports: List<ListReports>){
        val recyclerView = findViewById<RecyclerView>(R.id.recycleView_reportes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        var adapter = ReportesAdapter(reports)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : ReportesAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val id = reports.get(position).repId.toString()
                callReporte(id)
            }

        })
    }

    private fun callReporte(id:String){
        val reporte = Intent(this, ReporteActivity::class.java)
        reporte.putExtra("id", id)
        startActivity(reporte)
    }
}