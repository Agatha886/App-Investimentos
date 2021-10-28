package br.com.brq.agatha.investimentos.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.util.CHAVE_MOEDA
import br.com.brq.agatha.investimentos.util.MENSAGEM_DADOS_NAO_ATUALIZADOS
import br.com.brq.agatha.investimentos.util.MENSAGEM_FALHA_API
import br.com.brq.agatha.investimentos.util.MENSAGEM_MOEDA_INVALIDA
import br.com.brq.agatha.investimentos.extension.mensagem
import br.com.brq.agatha.investimentos.extension.setMyActionBar
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.ui.adapter.ListaMoedasAdpter
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
                    adapter.quandoMoedaClicado = { mensagem(MENSAGEM_DADOS_NAO_ATUALIZADOS) }
                }
                else -> Log.i("TAG", "observerViewModel: Entrou no else")
            }
        })
    }

    private fun configuraAdapter() {
        home_recyclerView.adapter = adapter
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

