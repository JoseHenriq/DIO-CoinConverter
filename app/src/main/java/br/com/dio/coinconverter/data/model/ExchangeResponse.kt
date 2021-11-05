package br.com.dio.coinconverter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

typealias ExchangeResponse = HashMap<String, ExchangeResponseValue>

@Entity(tableName = "tb_exchange_1")      // "tb_exchange"
data class ExchangeResponseValue(
    @PrimaryKey(autoGenerate = true)
    var id       : Long,
    val dataHora : String,    // <---
    val code     : String,
    val codein   : String,
    val name     : String,
    val cotacao  : Double,    // <---
    val bid      : Double
)