package br.com.brq.agatha.investimentos.ui.recyclerview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.R.color.*
import br.com.brq.agatha.investimentos.extension.formatoPorcentagem
import br.com.brq.agatha.investimentos.model.Moeda
import kotlinx.android.synthetic.main.item_card_moedas.view.*
import java.math.BigDecimal


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

    fun adiciona(novaLista:Moeda?) {
        if (novaLista != null) {
            listaDeMoedas.add(novaLista)
        } else {
            Log.e("NULL", "MOEDA NULA")
        }

        notifyItemRangeInserted(0, listaDeMoedas.size)
    }

    inner class ListaMoedasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var moeda: Moeda

        init {
            itemView.setOnClickListener {
                if (::moeda.isInitialized) {
                    quandoMoedaClicado(moeda)
                }
            }
        }

        fun vincula(moeda: Moeda) {
            this.moeda = moeda
            itemView.cardView_nome_moeda.text = moeda.setAbreviacao()
            setCampoValor(moeda, itemView.cardView_cotacao_moeda)
        }

    }

    fun setCampoValor(moeda: Moeda, campoValor: TextView){
        campoValor.text = moeda.variation.formatoPorcentagem()
        val valor = moeda.variation

        when {
            valor< BigDecimal.ZERO -> {
                campoValor.setTextColor(ContextCompat.getColor(context, red))
            }
            valor == BigDecimal.ZERO -> {
                campoValor.setTextColor(ContextCompat.getColor(context, white))
            }
            else -> {
                campoValor.setTextColor(ContextCompat.getColor(context, verde))
            }
        }

    }
}