package com.example.ejemplo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ejemplo.ListReports
import android.widget.Toast
import com.example.ejemplo.R

class ReportesAdapter(private val reporte:List<ListReports>): RecyclerView.Adapter<ReportesHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)

    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportesHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ReportesHolder(layoutInflater.inflate(R.layout.list_reports, parent, false), mListener)
    }

    override fun onBindViewHolder(holder: ReportesHolder, position: Int) {
        holder.render(reporte[position])
    }

    override fun getItemCount(): Int = reporte.size


}