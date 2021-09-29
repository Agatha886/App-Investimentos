package br.com.brq.agatha.investimentos.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import br.com.brq.agatha.investimentos.database.InvestimentosDataBase
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.repository.MoedaRepository

class MoedaViewModel(context: Context): ViewModel() {
    private val dao = InvestimentosDataBase.getBatadaBase(context).getMoedaDao()
    private val repository: MoedaRepository = MoedaRepository(dao)

    fun modifica(moeda: Moeda){
        repository.modifica(moeda)
    }
}