package br.com.brq.agatha.investimentos.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.brq.agatha.investimentos.database.dao.MoedaDao
import br.com.brq.agatha.investimentos.model.Moeda
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

open class MoedaRepository(protected val daoMoeda: MoedaDao){

    private val io = CoroutineScope(Dispatchers.IO)
    var quandoFalhaVenda: (mensagem: String) -> Unit = {}
    var quandoSucessoVenda: (totalDeMoeda: Double) -> Unit = {}


    fun getTotalMoeda(nameMoeda: String): LiveData<Double>{
        var liveDate = MutableLiveData<Double>()
        io.launch {
            val moeda = daoMoeda.buscaMoeda(nameMoeda)
            withContext(Dispatchers.Main){
                liveDate.value = moeda.totalDeMoeda
            }
        }

        return liveDate
    }

    fun modifica(moedaNova: Moeda){
        io.launch {
            var moeda = daoMoeda.buscaMoeda(moedaNova.name)
            moedaNova.id = moeda.id
            moedaNova.totalDeMoeda = moeda.totalDeMoeda
            daoMoeda.modifica(moedaNova)
        }
    }

    fun setTotalMoedaAposCompra(nameMoeda: String, valorDaCompra: Double){
        io.launch {
            val moeda = daoMoeda.buscaMoeda(nameMoeda)
            moeda.setTotalMoedaCompra(valorDaCompra)
            daoMoeda.modifica(moeda)
        }
    }

    fun setTotalMoedaAposVenda(nameMoeda: String, valorTotalAposVenda: Double){
        io.launch {
            val moeda = daoMoeda.buscaMoeda(nameMoeda)
            moeda.setTotalMoedaVenda(valorTotalAposVenda)
            daoMoeda.modifica(moeda)
        }
    }

    fun venda(nameMoeda: String, valorDeVenda: String) {
        io.launch {
            val moeda = daoMoeda.buscaMoeda(nameMoeda)
            val valorTotalMoeda = moeda.totalDeMoeda.minus(BigDecimal(valorDeVenda).toDouble())
            if (valorTotalMoeda >= 00.0) {
                withContext(Dispatchers.Main) {
                    quandoSucessoVenda(valorTotalMoeda)
                }
            } else {
                quandoFalhaVenda("Valor inválido")
            }
        }
    }
}