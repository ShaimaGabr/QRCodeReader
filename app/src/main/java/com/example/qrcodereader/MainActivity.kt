package com.example.qrcodereader

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
private const val CAMERA_REQUEST_CODE=101
class MainActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    lateinit var scannerView:CodeScannerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         scannerView = findViewById(R.id.scanner_view)

        setupPermision()
        codeScanner()
    }
    private fun codeScanner() {
        codeScanner=CodeScanner(this,scannerView)
        codeScanner.apply {
            camera=CodeScanner.CAMERA_BACK
            formats=CodeScanner.ALL_FORMATS
            autoFocusMode= AutoFocusMode.SAFE
            scanMode= ScanMode.CONTINUOUS
            isAutoFocusEnabled=true
            isFlashEnabled=false
            decodeCallback= DecodeCallback {
              //  activity?.                                          use this with fragment
                runOnUiThread {
                     val tvTextview=it.text
                    ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
                    Toast.makeText(this@MainActivity,tvTextview, Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    bundle.putString("data", tvTextview)

                    val intent = Intent(this@MainActivity, SendQrcode::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)

                }

            }
            errorCallback= ErrorCallback {
               // activity?.                                   use this with fragment
                runOnUiThread{
                    Log.e("Main","Camera initialization error:${it.message}")
                }
            }
        }
       scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
    private fun setupPermision() {
        val Permission= ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
        if(Permission!= PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_REQUEST_CODE->{
                if(grantResults.isEmpty()||grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"you need the camera permission to be able to use this app", Toast.LENGTH_SHORT).show()
                }
                else{}
            }
        }
    }
}