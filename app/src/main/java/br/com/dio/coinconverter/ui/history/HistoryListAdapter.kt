package br.com.dio.coinconverter.ui.history

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.dio.coinconverter.core.extensions.formatCurrency
import br.com.dio.coinconverter.data.model.Coin
import br.com.dio.coinconverter.data.model.ExchangeResponseValue
import br.com.dio.coinconverter.databinding.ItemHistoryBinding
import br.com.dio.coinconverter.ui.main.MainActivity.Companion.cTAG

class HistoryListAdapter : ListAdapter<ExchangeResponseValue, HistoryListAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding  = ItemHistoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder( private val binding: ItemHistoryBinding ) :
                                                            RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ExchangeResponseValue) {

            val coin = Coin.getByName(item.codein)

            val arNameOfCoin = arrayOf(item.name.split("/"))
            try {
                binding.tvNomesMoedas.text = "${arNameOfCoin[0][1]}/${arNameOfCoin[0][0]}" //item.name
            }
            catch (exc : Exception) {

                Log.d(cTAG, "Erro: ${exc.message.toString()}")
                binding.tvNomesMoedas.text  = " "

            }
            binding.tvCotacao.text  = item.cotacao.formatCurrency(coin.locale)

            binding.tvDateHora.text = "Compra: ${item.dataHora}"
            binding.tvValue.text = item.bid.formatCurrency(coin.locale)

        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<ExchangeResponseValue>() {
    override fun areItemsTheSame(oldItem: ExchangeResponseValue, newItem: ExchangeResponseValue) = oldItem == newItem
    override fun areContentsTheSame(oldItem: ExchangeResponseValue, newItem: ExchangeResponseValue) = oldItem.id == newItem.id
}