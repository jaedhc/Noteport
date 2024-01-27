package com.example.ejemplo.adapter

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ejemplo.ListReports
import com.example.ejemplo.R
import com.squareup.picasso.Picasso

class ReportesHolder (view: View, listener: ReportesAdapter.onItemClickListener): RecyclerView.ViewHolder(view){

    val image: ImageView = view.findViewById(R.id.img_reporte)
    val estrellas: RatingBar = view.findViewById(R.id.estrellas)
    val textEstrellas: TextView = view.findViewById(R.id.txt_estrellas)
    val servicio: TextView = view.findViewById(R.id.txt_servicio)
    val sucursal: TextView = view.findViewById(R.id.txt_sucursal)
    val area: TextView = view.findViewById(R.id.txt_area)


    init {
        itemView.setOnClickListener{
            listener.onItemClick(adapterPosition)
        }
    }

    fun render(reporte: ListReports){
        estrellas.rating = reporte.repEstrellas
        textEstrellas.text = reporte.repEstrellas.toString()
        servicio.text = reporte.repServicio
        area.text = reporte.repArea
        sucursal.text = reporte.repSucursal

        Picasso.get()
            .load(reporte.repUrl)
            .error(R.drawable.iss_logo)
            .into(image)
    }

}