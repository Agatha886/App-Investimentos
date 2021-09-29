package br.com.brq.agatha.investimentos.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.brq.agatha.investimentos.database.InvestimentosDataBase.Companion.getBatadaBase
import br.com.brq.agatha.investimentos.model.Finance
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.repository.ListaMoedasRepository

class ListaDeMoedasViewModel(context: Context): ViewModel() {

    private val dao = getBatadaBase(context).getMoedaDao()
    private val repository: ListaMoedasRepository = ListaMoedasRepository(dao)

    fun buscaDadosDaApi(quanSucesso:(finance: LiveData<Finance>) -> Unit = {},
                        quandoConexaoFalha: (lista: LiveData<List<Moeda>>) -> Unit = {} ){
        repository.finance()
        repository.quandoSucesso = quanSucesso
        repository.quandoConexaoFalha = quandoConexaoFalha
    }

}