package br.com.brq.agatha.investimentos.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.brq.agatha.base.R.id.*
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.domain.model.Moeda
import br.com.brq.agatha.domain.util.formatoPorcentagem

class ListaMoedasAdpter(
    private val context: Context) :
    RecyclerView.Adapter<ListaMoedasAdpter.ListaMoedasViewHolder>() {

    private val listaDeMoedas: MutableList<Moeda> = mutableListOf()
    var quandoMoedaClicado: (moeda: Moeda) -> Unit = { _: Moeda -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaMoedasViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_card_moedas, parent, false)
        return ListaMoedasViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListaMoedasViewHolder, position: Int) {
        val moeda = listaDeMoedas[position]
        holder.vincula(moeda)
    }

    override fun getItemCount(): Int {
        return listaDeMoedas.size
    }

    fun atualiza(moedasNovas: List<Moeda>?) {
        notifyItemRangeRemoved(0, listaDeMoedas.size)
        listaDeMoedas.clear()
        if (moedasNovas != null) {
            listaDeMoedas.addAll(moedasNovas)
        } else {
            Log.e("NULL MOEDA", "Moedas Nulas")
        }

       atualizaAoAdicionar()
    }

    private fun atualizaAoAdicionar() {
        notifyItemRangeInserted(0, listaDeMoedas.size)
    }

    inner class ListaMoedasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var moeda: Moeda
        private lateinit var nomeMoeda: TextView
        private lateinit var variacaoMoeda: TextView

        init {
            itemView.setOnClickListener {
                if (::moeda.isInitialized) {
                    quandoMoedaClicado(moeda)
                }
            }
        }

        fun vincula(moeda: Moeda) {
            this.moeda = moeda
            nomeMoeda = itemView.findViewById(cardView_home_nome_moeda)
            nomeMoeda.text = moeda.setAbreviacao()
            setCampoVariation(moeda)
        }

        private fun setCampoVariation(moeda: Moeda) {
            variacaoMoeda = itemView.findViewById(cardView_home_variation_moeda)
            variacaoMoeda.text = moeda.variation.formatoPorcentagem()
            variacaoMoeda.setTextColor(moeda.retornaCor(context))
        }

    }
}