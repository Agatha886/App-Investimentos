package br.com.brq.agatha.investimentos.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.brq.agatha.investimentos.database.InvestimentosDataBase
import br.com.brq.agatha.investimentos.model.Finance
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.retrofit.MoedasRetrofit
import br.com.brq.agatha.investimentos.viewModel.RetornoStadeApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

open class MoedaDbDataSource(context: Context){

    private val io = CoroutineScope(Dispatchers.IO)
    private val daoMoeda = InvestimentosDataBase.getBatadaBase(context).getMoedaDao()

    fun buscaMoeda(nameMoeda: String): Moeda{
        return daoMoeda.buscaMoeda(nameMoeda)
    }

    fun buscaMoedasNoBanco(): List<Moeda> {
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


    fun getTotalMoeda(nameMoeda: String): LiveData<Double> {
        val liveDate = MutableLiveData<Double>()
        io.launch {
            val moeda = daoMoeda.buscaMoeda(nameMoeda)
            withContext(Dispatchers.Main) {
                liveDate.value = moeda.totalDeMoeda
            }
        }

        return liveDate
    }

    fun setTotalMoedaAposCompra(nameMoeda: String, valorDaCompra: Double) {
        io.launch {
            val moeda = daoMoeda.buscaMoeda(nameMoeda)
            moeda.setTotalMoedaCompra(valorDaCompra)
            daoMoeda.modifica(moeda)
        }
    }

    fun setTotalMoedaAposVenda(nameMoeda: String, valorTotalAposVenda: Double) {
        io.launch {
            val moeda = daoMoeda.buscaMoeda(nameMoeda)
            moeda.setTotalMoedaVenda(valorTotalAposVenda)
            daoMoeda.modifica(moeda)
        }
    }

}