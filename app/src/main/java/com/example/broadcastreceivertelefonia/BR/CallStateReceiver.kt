package com.example.broadcastreceivertelefonia.BR

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager

class CallStateReceiver : BroadcastReceiver() {

    // Este método se llama cada vez que se recibe una transmisión de difusión
    override fun onReceive(context: Context?, intent: Intent?) {

        // Obtener el estado de la llamada y el número entrante
        val estado = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)
        val obtenernumero = intent?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

        // Verificar si el estado de la llamada es "ringing" (sonando)
        if (estado == TelephonyManager.EXTRA_STATE_RINGING) {

            // Crear un nuevo intent con la acción "com.example.myapp.NEW_INCOMING_CALL"
            val i = Intent("com.example.myapp.NEW_INCOMING_CALL")
            // Agregar el número entrante como extra al intent
            i.putExtra("incoming_number", obtenernumero)

            // Enviar la transmisión de difusión con el intent
            context?.sendBroadcast(i)
        }
    }
}
