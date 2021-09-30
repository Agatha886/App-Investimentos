package br.com.brq.agatha.investimentos.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.brq.agatha.investimentos.database.dao.MoedaDao
import br.com.brq.agatha.investimentos.model.Finance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

open class MoedaRepository(val daoMoeda: MoedaDao){

    private val io = CoroutineScope(Dispatchers.IO)
    var quandoFalhaVenda: (mensagem: String) -> Unit = {}
    var quandoSucessoVenda: (totalDeMoeda: Double) -> Unit = {}


    fun totalMoeda(nameMoeda: String): LiveData<Double>{
        var liveDate = MutableLiveData<Double>()
        io.launch {
            val moeda = daoMoeda.buscaMoeda(nameMoeda)
            withContext(Dispatchers.Main){
                liveDate.value = moeda.totalDeMoeda
            }
        }

        return liveDate
    }

    fun setTotalMoedaAposCompra(nameMoeda: String, valorDaCompra: Double){
        io.launch {
            val moeda = daoMoeda.buscaMoeda(nameMoeda)
            moeda.setTotalMoedaCompra(valorDaCompra)
            daoMoeda.modifica(moeda)
        }
    }

    fun setTotalMoedaAposVenda(nameMoeda: String, valorDaCompra: Double){
        io.launch {
            val moeda = daoMoeda.buscaMoeda(nameMoeda)
            moeda.setTotalMoedaVenda(valorDaCompra)
            daoMoeda.modifica(moeda)
        }
    }

    protected fun modificaListaMoedas(finance: Finance?){
        Log.i("TAG", "modificaListaMoedas: Pasou no modifica ${finance}")
        finance?.results?.currencies?.usd?.let { daoMoeda.modifica(it) }
        finance?.results?.currencies?.jpy?.let { daoMoeda.modifica(it) }
        finance?.results?.currencies?.gbp?.let { daoMoeda.modifica(it) }
        finance?.results?.currencies?.eur?.let { daoMoeda.modifica(it) }
        finance?.results?.currencies?.cny?.let { daoMoeda.modifica(it) }
        finance?.results?.currencies?.cad?.let { daoMoeda.modifica(it) }
        finance?.results?.currencies?.btc?.let { daoMoeda.modifica(it) }
        finance?.results?.currencies?.aud?.let { daoMoeda.modifica(it) }
        finance?.results?.currencies?.ars?.let { daoMoeda.modifica(it) }
    }

    protected fun adicionaListaMoedas(finance: Finance?){
        finance?.results?.currencies?.usd?.let { daoMoeda.adiciona(it) }
        finance?.results?.currencies?.jpy?.let { daoMoeda.adiciona(it) }
        finance?.results?.currencies?.gbp?.let { daoMoeda.adiciona(it) }
        finance?.results?.currencies?.eur?.let { daoMoeda.adiciona(it) }
        finance?.results?.currencies?.cny?.let { daoMoeda.adiciona(it) }
        finance?.results?.currencies?.cad?.let { daoMoeda.adiciona(it) }
        finance?.results?.currencies?.btc?.let { daoMoeda.adiciona(it) }
        finance?.results?.currencies?.aud?.let { daoMoeda.adiciona(it) }
        finance?.results?.currencies?.ars?.let { daoMoeda.adiciona(it) }
    }

    fun venda(nameMoeda: String, valorDeVenda: String) {
        io.launch {
            val moeda = daoMoeda.buscaMoeda(nameMoeda)
            val valorTotalMoeda = moeda.totalDeMoeda.minus(BigDecimal(valorDeVenda).toDouble())
            if (valorTotalMoeda > 00.0) {
                withContext(Dispatchers.Main) {
                    quandoSucessoVenda(valorTotalMoeda)
                    Log.i("TAG", "venda: $valorTotalMoeda")
                }
            } else {
                quandoFalhaVenda("Valor inválido")
            }
        }
    }
}