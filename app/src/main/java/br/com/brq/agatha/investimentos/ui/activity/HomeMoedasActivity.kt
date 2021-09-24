package br.com.brq.agatha.investimentos.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.constantes.CHAVE_MOEDA
import br.com.brq.agatha.investimentos.extension.setMyActionBar
import br.com.brq.agatha.investimentos.model.Finance
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.ui.recyclerview.ListaMoedasAdpter
import br.com.brq.agatha.investimentos.viewModel.ListaDeMoedasViewModel
import kotlinx.android.synthetic.main.activity_moedas_home.*

class HomeMoedasActivity : AppCompatActivity() {

    private val adapter: ListaMoedasAdpter by lazy {
        ListaMoedasAdpter(this@HomeMoedasActivity)
    }

    private val viewModel: ListaDeMoedasViewModel by lazy {
        ListaDeMoedasViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moedas_home)
        setMyActionBar("Home/Moeda", false)
        configuraAdapter()
        if(savedInstanceState == null){
            configuraViewModel()
        }
    }

    private fun configuraAdapter() {
        home_recyclerView.adapter = adapter
        adapter.quandoMoedaClicado = this::vaiParaActivityCambio
    }

    private fun vaiParaActivityCambio(moeda: Moeda) {
        if (moeda.sell == null || moeda.buy == null) {
            Toast.makeText(
                this,
                "Moeda com valor de compra ou venda inválido",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val intent = Intent(this@HomeMoedasActivity, CambioActivity::class.java)
            intent.putExtra(CHAVE_MOEDA, moeda)
            startActivity(intent)
        }
    }

    private fun configuraViewModel() {
        viewModel.quandoFalha = {
            Toast.makeText(
                this@HomeMoedasActivity,
                "Não foi possível buscar moedas",
                Toast.LENGTH_LONG
            ).show()
        }

        viewModel.retornaMoedas(quanSucesso = {
            it.observe(this, Observer {
                adapter.adiciona(it?.results?.currencies?.usd)
                adapter.adiciona(it?.results?.currencies?.jpy)
                adapter.adiciona(it?.results?.currencies?.gbp)
                adapter.adiciona(it?.results?.currencies?.eur)
                adapter.adiciona(it?.results?.currencies?.cny)
                adapter.adiciona(it?.results?.currencies?.cad)
                adapter.adiciona(it?.results?.currencies?.btc)
                adapter.adiciona(it?.results?.currencies?.aud)
                adapter.adiciona(it?.results?.currencies?.ars)
            })
        }, quandoConexaoFalha = {
            it.observe(this, Observer {
               adapter.atualiza(it)
            })
        })

    }

}

