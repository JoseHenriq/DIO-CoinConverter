package br.com.dio.coinconverter

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import br.com.dio.coinconverter.ui.main.MainActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Utils {

    //----------------------------------------------------------------------------------------------
    // Instancializações e inicializações
    //----------------------------------------------------------------------------------------------
    private val cTAG = MainActivity.cTAG      // "Utils"

    //----------------------------------------------------------------------------------------------
    // Métodos do Sistema
    //----------------------------------------------------------------------------------------------

    //--- Data do sistema ("dd/MM/yy")
    @SuppressLint("never used")
    @RequiresApi(Build.VERSION_CODES.O)
    fun sysData(formatoData : String): String {

        val formatter = DateTimeFormatter.ofPattern(formatoData)
        return LocalDateTime.now().format(formatter)

    }

    //--- Hora do sistema ("HH:mm:ss.SSS")
    @SuppressLint("never used")
    @RequiresApi(Build.VERSION_CODES.O)
    fun sysHora(formatoHora : String): String {

        val formatter = DateTimeFormatter.ofPattern(formatoHora)
        return LocalDateTime.now().format(formatter)

    }

}
