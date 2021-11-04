package br.com.brq.agatha.base.repository

import br.com.brq.agatha.base.database.dao.MoedaDao
import br.com.brq.agatha.domain.model.Moeda

open class MoedaDbDataSource(private val daoMoeda: MoedaDao) {

    suspend fun buscaMoedaNoBando(nameMoeda: String): Moeda {
        return daoMoeda.buscaMoeda(nameMoeda)
    }

    fun buscaTodasMoedasNoBanco(): List<Moeda> {
        return daoMoeda.buscaTodasAsMoedas()
    }

    protected suspend fun modifica(moedaNova: Moeda) {
        val moeda = daoMoeda.buscaMoeda(moedaNova.name)
        moedaNova.id = moeda.id
        moedaNova.totalDeMoeda = moeda.totalDeMoeda
        daoMoeda.modifica(moedaNova)

    }

    protected suspend fun adiciona(moeda: Moeda) {
        daoMoeda.adiciona(moeda)

    }

    suspend fun setTotalMoedaAposCompra(nameMoeda: String, valorDaCompra: Int) {
        val moeda = daoMoeda.buscaMoeda(nameMoeda)
        moeda.setTotalMoedaCompra(valorDaCompra)
        daoMoeda.modifica(moeda)

    }

    suspend fun setTotalMoedaAposVenda(nameMoeda: String, valorTotalAposVenda: Int) {
        val moeda = daoMoeda.buscaMoeda(nameMoeda)
        moeda.setTotalMoedaVenda(valorTotalAposVenda)
        daoMoeda.modifica(moeda)

    }

}