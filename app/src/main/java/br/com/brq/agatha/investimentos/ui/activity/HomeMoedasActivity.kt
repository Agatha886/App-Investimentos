package br.com.brq.agatha.investimentos.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.constantes.CHAVE_MOEDA
import br.com.brq.agatha.investimentos.constantes.MENSAGEM_FALHA_API
import br.com.brq.agatha.investimentos.constantes.MENSAGEM_MOEDA_INVALIDA
import br.com.brq.agatha.investimentos.extension.mensagem
import br.com.brq.agatha.investimentos.extension.setMyActionBar
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.ui.recyclerview.ListaMoedasAdpter
import br.com.brq.agatha.investimentos.viewModel.HomeViewModel
import br.com.brq.agatha.investimentos.viewModel.RetornoStade
import kotlinx.android.synthetic.main.activity_moedas_home.*

@Suppress("UNCHECKED_CAST")
class HomeMoedasActivity : AppCompatActivity() {

    private val adapter: ListaMoedasAdpter by lazy {
        ListaMoedasAdpter(this@HomeMoedasActivity)
    }

    private val viewModel: HomeViewModel by lazy {
        HomeViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moedas_home)
        setMyActionBar("Home/Moeda", false)
        configuraAdapter()
        observerViewModel()
        if (savedInstanceState == null) {
            configuraViewModel()
        }
    }

    private fun observerViewModel() {
        viewModel.viewEventRetornoApi.observe(this, Observer {
            when (it) {
                is RetornoStade.Sucesso -> adapter.atualiza(it.listaMoeda)
                is RetornoStade.FalhaApi<*> -> setAdapterComBancoDeDados(it.`object`as List<Moeda>)
            }
        })
    }

    private fun configuraAdapter() {
        home_recyclerView.adapter = adapter
        adapter.quandoMoedaClicado = this::vaiParaActivityCambio
    }

    private fun vaiParaActivityCambio(moeda: Moeda) {
        if (moeda.sell == null || moeda.buy == null) {
            mensagem(MENSAGEM_MOEDA_INVALIDA)
        } else {
            val intent = Intent(this@HomeMoedasActivity, CambioActivity::class.java)
            intent.putExtra(CHAVE_MOEDA, moeda)
            startActivity(intent)
        }
    }

    private fun configuraViewModel() {
        viewModel.buscaDaApi()
    }

    private fun setAdapterComBancoDeDados(lista: List<Moeda>) {
        adapter.atualiza(lista)
        mensagem(MENSAGEM_FALHA_API)
    }


}

