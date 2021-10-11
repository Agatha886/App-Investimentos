package br.com.brq.agatha.investimentos.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.brq.agatha.investimentos.database.InvestimentosDataBase
import br.com.brq.agatha.investimentos.model.Finance
import br.com.brq.agatha.investimentos.model.Moeda
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class MoedaRepository(context: Context) {

    private val io = CoroutineScope(Dispatchers.IO)
    private val daoMoeda = InvestimentosDataBase.getBatadaBase(context).getMoedaDao()

    fun buscaMoeda(nameMoeda: String): Moeda{
        return daoMoeda.buscaMoeda(nameMoeda)
    }

    fun buscaMoedas(): List<Moeda> {
        return daoMoeda.buscaTodasAsMoedas()
    }

    private fun modifica(moedaNova: Moeda) {
        io.launch {
            val moeda = daoMoeda.buscaMoeda(moedaNova.name)
            moedaNova.id = moeda.id
            moedaNova.totalDeMoeda = moeda.totalDeMoeda
            daoMoeda.modifica(moedaNova)
        }
    }

    private fun adiciona(moeda: Moeda) {
        daoMoeda.adiciona(moeda)
    }

     fun modificaTotasAsMoedasNoBanco(finance: Finance?) {
        finance?.results?.currencies?.usd?.let { modifica(it) }
        finance?.results?.currencies?.jpy?.let { modifica(it) }
        finance?.results?.currencies?.gbp?.let { modifica(it) }
        finance?.results?.currencies?.eur?.let { modifica(it) }
        finance?.results?.currencies?.cny?.let { modifica(it) }
        finance?.results?.currencies?.cad?.let { modifica(it) }
        finance?.results?.currencies?.btc?.let { modifica(it) }
        finance?.results?.currencies?.aud?.let { modifica(it) }
        finance?.results?.currencies?.ars?.let { modifica(it) }
    }

    fun adicionaTodasAsMoedasNoBanco(finance: Finance?) {
        finance?.results?.currencies?.usd?.let { adiciona(it) }
        finance?.results?.currencies?.jpy?.let { adiciona(it) }
        finance?.results?.currencies?.gbp?.let { adiciona(it) }
        finance?.results?.currencies?.eur?.let { adiciona(it) }
        finance?.results?.currencies?.cny?.let { adiciona(it) }
        finance?.results?.currencies?.cad?.let { adiciona(it) }
        finance?.results?.currencies?.btc?.let { adiciona(it) }
        finance?.results?.currencies?.aud?.let { adiciona(it) }
        finance?.results?.currencies?.ars?.let { adiciona(it) }
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