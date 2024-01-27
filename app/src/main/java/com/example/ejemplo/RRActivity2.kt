package com.example.ejemplo

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.core.view.get
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage

class RRActivity2 : AppCompatActivity() {
    lateinit var contexto: Context
    val pickImageCode = 123
    private var imageUri: Uri? = null
    private var pickedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rellenar_reporte2)
        contexto = applicationContext

        val sharedPref = applicationContext.getSharedPreferences("user", Context.MODE_PRIVATE)
        val user = sharedPref.getString("prefUser", "Usuario")

        val spinner: Spinner = findViewById(R.id.spn_areas)
        val spinner_2: Spinner = findViewById(R.id.spn_servicio)
        var seleccion = 0
        val btnEnviar: Button = findViewById(R.id.bnt_enviar)
        val txt_fecha:TextView = findViewById(R.id.txt_fecha)
        val txt_sucrusal:TextView = findViewById(R.id.txt_sucursal)
        val ratingbar:RatingBar = findViewById(R.id.ratingBar2)

        val imgComent: ImageView = findViewById(R.id.img_coment)
        val opSucursal = this.intent.getExtras()?.getInt("seleccion")
        val fecha = this.intent.getExtras()?.getString("fecha")
        val edtComentario:EditText = findViewById(R.id.txt_comentario)
        val sucursal = opSucursal?.let { setSucursal(it) }

        txt_fecha.text = fecha
        txt_sucrusal.text = sucursal

        ArrayAdapter.createFromResource(
            this,
            R.array.areas_array,
            R.layout.spinner_layout2
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_layout2)
            spinner.adapter = adapter
        }

        spinner.setSelection(0)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                seleccion = spinner.selectedItemPosition
                when (seleccion) {
                    0 -> {
                        ArrayAdapter.createFromResource(
                            contexto,
                            R.array.mantenimiento_patio,
                            R.layout.spinner_layout2
                        ).also { adapter ->
                            adapter.setDropDownViewResource(R.layout.spinner_layout2)
                            spinner_2.adapter = adapter
                        }
                    }

                    1 -> {
                        ArrayAdapter.createFromResource(
                            contexto,
                            R.array.mantenimiento_atms,
                            R.layout.spinner_layout2
                        ).also { adapter ->
                            adapter.setDropDownViewResource(R.layout.spinner_layout2)
                            spinner_2.adapter = adapter
                        }
                    }

                    2 -> {
                        ArrayAdapter.createFromResource(
                            contexto,
                            R.array.mantenimiento_ejecutivos,
                            R.layout.spinner_layout2
                        ).also { adapter ->
                            adapter.setDropDownViewResource(R.layout.spinner_layout2)
                            spinner_2.adapter = adapter
                        }
                    }

                    3 -> {
                        ArrayAdapter.createFromResource(
                            contexto,
                            R.array.mantenimiento_cajas,
                            R.layout.spinner_layout2
                        ).also { adapter ->
                            adapter.setDropDownViewResource(R.layout.spinner_layout2)
                            spinner_2.adapter = adapter
                        }
                    }

                    4 -> {
                        ArrayAdapter.createFromResource(
                            contexto,
                            R.array.mantenimiento_boveda,
                            R.layout.spinner_layout2
                        ).also { adapter ->
                            adapter.setDropDownViewResource(R.layout.spinner_layout2)
                            spinner_2.adapter = adapter
                        }
                    }

                    5 -> {
                        ArrayAdapter.createFromResource(
                            contexto,
                            R.array.mantenimiento_interna,
                            R.layout.spinner_layout2
                        ).also { adapter ->
                            adapter.setDropDownViewResource(R.layout.spinner_layout2)
                            spinner_2.adapter = adapter
                        }
                    }

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        imgComent.setOnClickListener{
            pickImage()
        }

        btnEnviar.setOnClickListener{
            val comentario = edtComentario.text.toString()
            val servicio = "Servicio de Mantenimiento"
            val area = spinner.selectedItem.toString()
            val elemento = spinner_2.selectedItem.toString()
            val estrellas = ratingbar.rating.toFloat()

            if(imageUri === null){
                Toast.makeText(this, "Favor de cargar una imagen", Toast.LENGTH_LONG).show()
            }else {
                subirImagen(user, fecha, comentario, sucursal, servicio, area, elemento, estrellas)
            }
        }

    }

    private fun subirImagen(
        correo: String?,
        fecha: String?,
        comentario:String,
        sucursal:String?,
        servicio:String,
        area:String,
        elemento:String,
        estrellas:Float,){

        val uniqueId = System.currentTimeMillis().toInt()
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("reportes").child(uniqueId.toString())
        var urlImg:String


        imagesRef.putFile(imageUri!!).addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener { uri ->
                urlImg = uri.toString()
                subirReporte(correo, fecha, comentario, sucursal, servicio, area, elemento, estrellas, urlImg, uniqueId)
            }
        }.addOnFailureListener{
        }

    }

    private fun subirReporte(
        correo: String?,
        fecha: String?,
        comentario:String,
        sucursal:String?,
        servicio:String,
        area:String,
        elemento:String,
        estrellas:Float,
        url:String,
        id:Int){

        val db = FirebaseFirestore.getInstance()

        val hashMap = hashMapOf(
            "correo" to correo,
            "fecha" to fecha,
            "comentario" to comentario,
            "sucursal" to sucursal,
            "servicio" to servicio,
            "area" to area,
            "elemento" to elemento,
            "estrellas" to estrellas,
            "url" to url
        )

        db.collection("Reportes").document(id.toString()).set(hashMap, SetOptions.merge()).addOnSuccessListener {
            Toast.makeText(this, "Reporte Agregado con éxito", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }


    }

    private fun setSucursal(op:Int):String{
        val sucursales = resources.getStringArray(R.array.sucursal_array)
        return sucursales[op]
    }

    private fun pickImage(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImageCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == pickImageCode && resultCode == RESULT_OK){
            imageUri = data?.data
            if(imageUri != null){
                pickedBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                val img: ImageView = findViewById(R.id.img_coment)
                img.setImageBitmap(pickedBitmap)
            }
        }

    }

}

