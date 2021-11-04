package br.com.dio.coinconverter.ui.history

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.dio.coinconverter.core.extensions.createDialog
import br.com.dio.coinconverter.core.extensions.createProgressDialog
import br.com.dio.coinconverter.databinding.ActivityHistoryBinding
import br.com.dio.coinconverter.presentation.HistoryViewModel
import br.com.dio.coinconverter.ui.main.MainActivity.Companion.cTAG
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryActivity : AppCompatActivity() {

    private val adapter by lazy { HistoryListAdapter() }
    private val dialog by lazy { createProgressDialog() }
    private val viewModel by viewModel<HistoryViewModel>()
    private val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rvHistory.adapter = adapter
        binding.rvHistory.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
        )

        //--- Prepara o actionBar (toolBar)
        //setting toolbar
        setSupportActionBar(binding.toolbar)
        //home navigation
        //supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bindObserve()

        lifecycle.addObserver(viewModel)
    }

    //--- Navegação - Actions on click menu items
    // https://github.com/EyeHunts/AndroidToolbarexample
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        /* NÃO utilizada
        R.id.action_print -> {
            // User chose the "Print" item
            Toast.makeText(this,"Print action", Toast.LENGTH_LONG).show()
            true
        }
         */

        android.R.id.home -> {

            Log.d(cTAG, "   -- retorna à mainActivity")

            //---------
            finish()
            //---------

            true

        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            //----------------------------------
            super.onOptionsItemSelected(item)
            //----------------------------------
        }
    }

    private fun bindObserve() {
        viewModel.state.observe(this) {
            when (it) {
                HistoryViewModel.State.Loading -> dialog.show()
                is HistoryViewModel.State.Error -> {
                    dialog.dismiss()
                    createDialog {
                        setMessage(it.error.message)
                    }.show()
                }
                is HistoryViewModel.State.Success -> {
                    dialog.dismiss()
                    adapter.submitList(it.list)
                }
            }
        }
    }

}