package br.com.brq.agatha.investimentos.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.brq.agatha.investimentos.database.dao.MoedaDao
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.viewModel.base.CoroutinesContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class MoedaDbDataSource(private val daoMoeda: MoedaDao, coroutinesContextProvider: CoroutinesContextProvider){

    private val io = CoroutineScope(coroutinesContextProvider.io)

    fun buscaMoedaNoBando(nameMoeda: String): Moeda{
        return daoMoeda.buscaMoeda(nameMoeda)
    }

    fun buscaTodasMoedasNoBanco(): List<Moeda> {
        return daoMoeda.buscaTodasAsMoedas()
    }

    protected fun modifica(moedaNova: Moeda) {
        io.launch {
            val moeda = daoMoeda.buscaMoeda(moedaNova.name)
            moedaNova.id = moeda.id
            moedaNova.totalDeMoeda = moeda.totalDeMoeda
            daoMoeda.modifica(moedaNova)
        }
    }

    protected fun adiciona(moeda: Moeda) {
        io.launch {
            daoMoeda.adiciona(moeda)
        }
    }


    fun getTotalMoeda(nameMoeda: String): LiveData<Int> {
        val liveDate = MutableLiveData<Int>()
        io.launch {
            val moeda = daoMoeda.buscaMoeda(nameMoeda)
            liveDate.postValue(moeda.totalDeMoeda)
        }

        return liveDate
    }

    fun setTotalMoedaAposCompra(nameMoeda: String, valorDaCompra: Int) {
        io.launch {
            val moeda = daoMoeda.buscaMoeda(nameMoeda)
            moeda.setTotalMoedaCompra(valorDaCompra)
            daoMoeda.modifica(moeda)
        }
    }

    fun setTotalMoedaAposVenda(nameMoeda: String, valorTotalAposVenda: Int) {
        io.launch {
            val moeda = daoMoeda.buscaMoeda(nameMoeda)
            moeda.setTotalMoedaVenda(valorTotalAposVenda)
            daoMoeda.modifica(moeda)
        }
    }

}