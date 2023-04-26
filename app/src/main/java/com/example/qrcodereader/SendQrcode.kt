package com.example.qrcodereader

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder


class SendQrcode : AppCompatActivity() {

    lateinit var data:TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_qrcode)
        data=findViewById(R.id.data)
        val bundle = intent.extras
        if (bundle != null){
            data.text = " ${bundle.getString("data")}"

        }
        val encoder = BarcodeEncoder()
        val bitmap = encoder.encodeBitmap(" ${bundle!!.getString("data")}", BarcodeFormat.QR_CODE, 400, 400)
        val myImageView: ImageView = findViewById(R.id.idIVQrcode)
        myImageView.setImageBitmap(bitmap)
    }
}