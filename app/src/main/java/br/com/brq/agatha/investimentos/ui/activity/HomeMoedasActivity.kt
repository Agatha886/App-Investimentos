package br.com.brq.agatha.investimentos.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.ui.adapter.ListaMoedasAdpter
import br.com.brq.agatha.investimentos.util.mensagem
import br.com.brq.agatha.investimentos.util.setMyActionBar
import br.com.brq.agatha.investimentos.viewModel.HomeViewModel
import br.com.brq.agatha.investimentos.viewModel.RetornoStadeApi
import kotlinx.android.synthetic.main.activity_moedas_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("UNCHECKED_CAST")
class HomeMoedasActivity : AppCompatActivity() {


    private val adapter: ListaMoedasAdpter by lazy {
        ListaMoedasAdpter(this@HomeMoedasActivity)
    }

    private val viewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moedas_home)
        setMyActionBar("Home/Moeda", false)
        configuraAdapter()
        observerViewModel()
        configuraViewModel()
        configuraSwipe()
    }

    private fun configuraSwipe() {
        home_swipe.setOnRefreshListener {
            viewModel.buscaDaApi()
        }

        viewModel.quandoFinaliza = {
            home_swipe.isRefreshing = false
        }
    }

    private fun observerViewModel() {
        viewModel.viewModelRetornoDaApi.observe(this, Observer {
            when (it) {
                is RetornoStadeApi.SucessoRetornoApi -> {
                    adapter.atualiza(it.listaMoeda)
                    adapter.quandoMoedaClicado = this::vaiParaActivityCambio
                }
                is RetornoStadeApi.SucessoRetornoBanco-> {
                    setAdapterComBancoDeDados(it.listaMoeda)
                    adapter.quandoMoedaClicado = { mensagem(br.com.brq.agatha.domain.util.MENSAGEM_DADOS_NAO_ATUALIZADOS) }
                }
                else -> Log.i("TAG", "observerViewModel: Entrou no else")
            }
        })
    }

    private fun configuraAdapter() {
        home_recyclerView.adapter = adapter
    }

    private fun vaiParaActivityCambio(moeda: br.com.brq.agatha.domain.model.Moeda) {
        if (moeda.sell == null || moeda.buy == null) {
            mensagem(br.com.brq.agatha.domain.util.MENSAGEM_MOEDA_INVALIDA)
        } else {
            val intent = Intent(this@HomeMoedasActivity, CambioActivity::class.java)
            intent.putExtra(br.com.brq.agatha.domain.util.CHAVE_MOEDA, moeda)
            startActivity(intent)
        }
    }

    private fun configuraViewModel() {
        viewModel.buscaDaApi()
    }

    private fun setAdapterComBancoDeDados(lista: List<br.com.brq.agatha.domain.model.Moeda>) {
        adapter.atualiza(lista)
        mensagem(br.com.brq.agatha.domain.util.MENSAGEM_FALHA_API)
    }


}

