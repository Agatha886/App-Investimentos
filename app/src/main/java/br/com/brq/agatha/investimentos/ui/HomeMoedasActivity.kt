package br.com.brq.agatha.investimentos.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.com.brq.agatha.base.util.*
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.presentation.ui.activity.CambioActivity
import br.com.brq.agatha.presentation.adapter.ListaMoedasAdpter
import br.com.brq.agatha.investimentos.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.BigDecimal

@Suppress("UNCHECKED_CAST")
class HomeMoedasActivity : AppCompatActivity() {

    private lateinit var homeSwipe : SwipeRefreshLayout
    private lateinit var homeRecyclerView: RecyclerView

    private val adapter: ListaMoedasAdpter by lazy {
        ListaMoedasAdpter(this@HomeMoedasActivity)
    }

    private val viewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moedas_home)
        setMyActionBar("Home/Moeda", false)
        inicializaCampos()
        configuraAdapter()
        observerViewModel()
        configuraViewModel()
        configuraSwipe()
    }

    private fun inicializaCampos() {
        homeSwipe = findViewById(br.com.brq.agatha.base.R.id.home_swipe)
        homeRecyclerView = findViewById(br.com.brq.agatha.base.R.id.home_recyclerView)
    }

    private fun configuraSwipe() {
        homeSwipe.setOnRefreshListener {
            viewModel.buscaDaApi()
        }

        viewModel.quandoFinaliza = {
            homeSwipe.isRefreshing = false
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
        homeRecyclerView.adapter = adapter
    }

    private fun vaiParaActivityCambio(moeda: br.com.brq.agatha.domain.model.Moeda) {
        if (moeda.sell == null && moeda.buy == null) {
            mensagem(MENSAGEM_MOEDA_INVALIDA)
        } else {
            if (moeda.sell == null) {
                moeda.sell = BigDecimal.ZERO
            }else if (moeda.buy == null){
                moeda.buy = BigDecimal.ZERO
            }
            val intent = Intent(this@HomeMoedasActivity, CambioActivity::class.java)
            intent.putExtra(CHAVE_MOEDA, moeda)
            startActivity(intent)
        }
    }

    private fun configuraViewModel() {
        viewModel.buscaDaApi()
    }

    private fun setAdapterComBancoDeDados(lista: List<br.com.brq.agatha.domain.model.Moeda>) {
        adapter.atualiza(lista)
        mensagem(MENSAGEM_FALHA_API)
    }


}

