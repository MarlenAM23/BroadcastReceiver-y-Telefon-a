package com.example.broadcastreceivertelefonia

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    // Declaración de variables
    private lateinit var txtNumero: TextView
    private lateinit var txtMensaje: TextView
    private val PERMISSIONS_REQUEST_CODE = 100
    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.SEND_SMS
    )
    private val incomingCallReceiver = object : BroadcastReceiver() {
        // Este método se llama cada vez que se recibe una transmisión de difusión
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?) {
            // Obtener el número entrante de la transmisión de difusión
            val Obtenernumero = intent?.getStringExtra("incoming_number")
            // Verificar si el número entrante coincide con el número ingresado por el usuario
            if (Obtenernumero == txtNumero.text.toString()) {
                // Enviar un mensaje de texto al número entrante
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(
                    txtNumero.text.toString(), null, txtMensaje.text.toString(), null, null
                )
            }

        }
    }

    lateinit var botonAux: Button

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Verificar si se han otorgado los permisos necesarios
        if (arePermissionsGranted()) {
            // Establecer el diseño de la actividad y buscar las vistas
            setContentView(R.layout.activity_main)
            txtNumero = findViewById(R.id.txtTelefono)
            txtMensaje = findViewById(R.id.txtMensaje)
            // Registrar el receptor de llamadas entrantes para recibir transmisiones de difusión
            val filter = IntentFilter("com.example.myapp.NEW_INCOMING_CALL")
            registerReceiver(incomingCallReceiver, filter)
        } else {
            // Solicitar permisos si no se han otorgado
            requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE)
        }

        botonAux = findViewById(R.id.btnGuardar)

        botonAux.setOnClickListener {
            if (txtMensaje.isEnabled==true) {
                txtMensaje.isEnabled = false
                txtNumero.isEnabled = false
                botonAux.setText("Editar")
            } else {
                txtMensaje.isEnabled = true
                txtNumero.isEnabled = true
                botonAux.setText("Guardar")
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancelar el registro del receptor de llamadas entrantes
        unregisterReceiver(incomingCallReceiver)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (areAllPermissionsGranted(grantResults)) {
                // Establecer el diseño de la actividad y buscar las vistas
                setContentView(R.layout.activity_main)
                txtNumero = findViewById(R.id.txtTelefono)
                txtMensaje = findViewById(R.id.txtMensaje)
                // Registrar el receptor de llamadas entrantes para recibir transmisiones de difusión
                val filter = IntentFilter("com.example.myapp.NEW_INCOMING_CALL")
                registerReceiver(incomingCallReceiver, filter)
            } else {
                // Solicitar permisos nuevamente si no se han otorgado
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE)

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun arePermissionsGranted(): Boolean {
        // Verificar si se han otorgado todos los permisos necesarios
        for (permission in PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(
                    this, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun areAllPermissionsGranted(grantResults: IntArray): Boolean {
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }


}

