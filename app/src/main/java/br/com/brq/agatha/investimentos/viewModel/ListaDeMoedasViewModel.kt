package br.com.brq.agatha.investimentos.viewModel

import androidx.lifecycle.LiveData
import br.com.brq.agatha.investimentos.model.Finance
import br.com.brq.agatha.investimentos.repository.ListaMoedasRepository

class ListaDeMoedasViewModel {
    private val repository: ListaMoedasRepository = ListaMoedasRepository()
    var quandoFalha: () -> Unit = {}

    fun retornaFinance(): LiveData<Finance?> {
         repository.quandoConexaoFalha = quandoFalha
        return repository.finance()
    }

}