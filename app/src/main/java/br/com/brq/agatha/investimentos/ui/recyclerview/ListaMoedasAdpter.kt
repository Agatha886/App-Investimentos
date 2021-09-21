package br.com.brq.agatha.investimentos.ui.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.formatoMoedaBrasileira
import br.com.brq.agatha.investimentos.model.Moeda
import kotlinx.android.synthetic.main.item_card_moedas.view.*


class ListaMoedasAdpter(
    private val context: Context,
    private val listaDeMoedas: MutableList<Moeda>
) :
    RecyclerView.Adapter<ListaMoedasAdpter.ListaMoedasViewHolder>() {

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


    inner class ListaMoedasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var moeda: Moeda

        init {
            itemView.setOnClickListener {
                if (::moeda.isInitialized) {
                    quandoMoedaClicado(moeda)
                }
            }
        }

        fun vincula(moeda: Moeda) {
            this.moeda = moeda
            itemView.cardView_nome_moeda.text = moeda.abreviacao
            itemView.cardView_cotacao_moeda.text = moeda.variation
        }

    }
}