package br.com.dio.coinconverter.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Build
import android.util.Log

import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged

import br.com.dio.coinconverter.R
import br.com.dio.coinconverter.Utils
import br.com.dio.coinconverter.core.extensions.createDialog
import br.com.dio.coinconverter.core.extensions.createProgressDialog
import br.com.dio.coinconverter.core.extensions.formatCurrency
import br.com.dio.coinconverter.core.extensions.hideSoftKeyboard
import br.com.dio.coinconverter.core.extensions.text
import br.com.dio.coinconverter.data.model.Coin
import br.com.dio.coinconverter.databinding.ActivityMainBinding
import br.com.dio.coinconverter.presentation.MainViewModel
import br.com.dio.coinconverter.ui.history.HistoryActivity

import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    //----------------------------------------------------------------------------------------------
    // Instancializações e inicializações
    //----------------------------------------------------------------------------------------------
    private val viewModel by viewModel<MainViewModel>()

    private val dialog    by lazy { createProgressDialog() }
    private val binding   by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var utils = Utils()

    companion object {

        const val cTAG = "DIO-CoinConverter"

    }

    //---------------------------------------------------------------------------------------------
    // Eventos
    //---------------------------------------------------------------------------------------------
    //--- onCreate
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //---------------
        bindAdapters()
        //----------------
        listeners()
        //----------------
        bindObserve()
        //---------------

        setSupportActionBar(binding.toolbar)

    }

    //--- onCreateOptionsMenu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    //--- onOptionsItemSelected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_history) {

            //----------------------------------------------------------------------
            startActivity(Intent(this, HistoryActivity::class.java))
            //----------------------------------------------------------------------

        }
        return super.onOptionsItemSelected(item)
    }

    //---------------------------------------------------------------------------------------------
    // Funções
    //---------------------------------------------------------------------------------------------
    //--- Adapters
    private fun bindAdapters() {

        //-------------------------
        val list = Coin.values()              // enum class Coin
        //-------------------------

        //--- Adapters
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        binding.tvFrom.setAdapter(adapter)    // androidx.appcompat.widget.AppCompatAutoCompleteTextView
        binding.tvTo.setAdapter  (adapter)    // androidx.appcompat.widget.AppCompatAutoCompleteTextView

        //--- Seleciona USD e BRL
        binding.tvFrom.setText(Coin.USD.name, false)
        binding.tvTo.setText  (Coin.BRL.name, false)

        binding.tilValueIn.hint = "Comprar ${binding.tilFrom.text}"
        binding.tvResult.hint   = "Pagar em ${binding.tilTo.text}"

    }

    //--- Listeners
    @RequiresApi(Build.VERSION_CODES.O)
    private fun listeners() {

        //- tilFrom
        binding.tilFrom.editText?.doAfterTextChanged {

            binding.btnSave.isEnabled = false

            binding.tilValueIn.hint = "Comprar ${binding.tilFrom.text}"

            binding.tvResult.text = ""

        }

        //- tilTo
        binding.tilTo.editText?.doAfterTextChanged {

            binding.btnSave.isEnabled      = false

            binding.tvResult.hint = "Pagar em ${binding.tilTo.text}"

            binding.tvResult.text   = ""

        }

        //- tilValueIn
        binding.tilValueIn.editText?.doAfterTextChanged {

            binding.btnConverter.isEnabled = (it != null && it.toString().isNotEmpty())
            binding.btnSave.isEnabled = false

            binding.tvResult.text   = ""

        }

        //- btnConverter
        binding.btnConverter.setOnClickListener {

            it.hideSoftKeyboard()

            val search = "${binding.tilFrom.text}-${binding.tilTo.text}"
            //-----------------------------------
            viewModel.getExchangeValue(search)
            //-----------------------------------

        }

        //- btnSave
        binding.btnSave.setOnClickListener {

            val value = viewModel.state.value
            try {
                (value as? MainViewModel.State.Success)?.let {

                    val data = utils.sysData("dd/MM/yy")
                    val hora = utils.sysHora("HH:mm:ss")

                    val exchange = it.exchange.copy(
                        cotacao = it.exchange.bid,
                        bid     = it.exchange.bid * binding.tilValueIn.text.toDouble(),
                        dataHora= "$data $hora"
                    )

                    //---------------------------------
                    viewModel.saveExchange(exchange)
                    //---------------------------------

                }
            } catch (exc: Exception){

                Log.d(cTAG, exc.message.toString())

            }
        }
    }

    //--- BindObserve
    private fun bindObserve() {

        //--------------------------------------
        viewModel.state.observe(this) {
        //--------------------------------------

            when (it) {

                //---------------------------------------------
                MainViewModel.State.Loading -> dialog.show()
                //---------------------------------------------

                //----------------------------------
                is MainViewModel.State.Error -> {
                //----------------------------------
                    dialog.dismiss()
                    createDialog {
                        setMessage(it.error.message)
                    }.show()
                }

                //----------------------------------------------
                is MainViewModel.State.Success -> success(it)
                //----------------------------------------------

                //-------------------------------
                MainViewModel.State.Saved -> {
                //-------------------------------
                    dialog.dismiss()
                    createDialog {
                        setMessage("item salvo com sucesso!")
                    }.show()

                }
            }
        }
    }

    //--- Sucesso (chamado por bindObserve() it = MainViewModel.State.Success)
    private fun success(it: MainViewModel.State.Success) {

        dialog.dismiss()
        binding.btnSave.isEnabled = true

        val selectedCoin = binding.tilTo.text
        val coin = Coin.getByName(selectedCoin)

        val cotacao = it.exchange.bid

        val result = cotacao * binding.tilValueIn.text.toDouble()
        binding.tvResult.text = result.formatCurrency(coin.locale)

    }

}