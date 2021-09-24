package br.com.brq.agatha.investimentos.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import br.com.brq.agatha.investimentos.constantes.NOME_BANCO
import br.com.brq.agatha.investimentos.database.InvestimentosDataBase
import br.com.brq.agatha.investimentos.model.Finance
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.repository.ListaMoedasRepository

class ListaDeMoedasViewModel(private val context: Context): ViewModel() {

    private val dao = Room.databaseBuilder(context, InvestimentosDataBase::class.java, NOME_BANCO).fallbackToDestructiveMigration().build().getMoedaDao()
    private val repository: ListaMoedasRepository = ListaMoedasRepository(dao)
    var quandoFalha: () -> Unit = {}

    fun retornaMoedas(quanSucesso:(finance: LiveData<Finance>) -> Unit = {},
                      quandoConexaoFalha: (lista: LiveData<List<Moeda>>) -> Unit = {} ){
        repository.finance()
        repository.quandoSucesso = quanSucesso
        repository.quandoConexaoFalha = quandoConexaoFalha
    }


}