package br.com.brq.agatha.investimentos.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.brq.agatha.investimentos.database.InvestimentosDataBase
import br.com.brq.agatha.investimentos.repository.MoedaRepository

class MoedaViewModel(context: Context) : ViewModel() {
    private val dao = InvestimentosDataBase.getBatadaBase(context).getMoedaDao()
    private val repository: MoedaRepository = MoedaRepository(dao)

    var quandoVendaSucesso: (totalDeMoeda: Double) -> Unit = {}
    var quandoVendaFalha: (mensagem: String) -> Unit = {}

    fun getTotalMoeda(nameMoeda: String): LiveData<Double> {
        return repository.getTotalMoeda(nameMoeda)
    }

    fun setToltalMoedaCompra(nameMoeda: String, valorDaCompra: Double) {
        repository.setTotalMoedaAposCompra(nameMoeda, valorDaCompra)
    }

    fun setTotalMoedaVenda(nameMoeda: String, valorDaCompra: Double) {
        repository.setTotalMoedaAposVenda(nameMoeda, valorDaCompra)
    }

    fun validaTotalMoedaVenda(nameMoeda: String, valorDeVenda: String) {
        repository.quandoFalhaVenda = quandoVendaFalha
        repository.quandoSucessoVenda = quandoVendaSucesso
        repository.venda(nameMoeda, valorDeVenda)
    }

}