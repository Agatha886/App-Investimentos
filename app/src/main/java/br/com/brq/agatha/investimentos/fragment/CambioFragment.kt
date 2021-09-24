package br.com.brq.agatha.investimentos.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.constantes.CHAVE_MOEDA
import br.com.brq.agatha.investimentos.extension.formatoMoedaBrasileira
import br.com.brq.agatha.investimentos.extension.formatoPorcentagem
import br.com.brq.agatha.investimentos.model.Moeda
import kotlinx.android.synthetic.main.cambio.*

class CambioFragment : Fragment() {

    private val moeda: Moeda by lazy {
       val moedaSerializable = arguments?.getSerializable(CHAVE_MOEDA)
           ?: throw IllegalArgumentException("Moeda Inválida")
        moedaSerializable as Moeda
    }

    var quandoDarIllegalArgumentException: (mensagem: String?) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.cambio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializaCampos()
        cambio_button_comprar.setOnClickListener {
            Toast.makeText(activity?.baseContext!!, "Clicou no botão de compra", Toast.LENGTH_LONG).show()
        }

        cambio_button_vender.setOnClickListener {
            Toast.makeText(activity?.baseContext!!, "Clicou no botão de venda", Toast.LENGTH_LONG).show()
        }
    }

    private fun inicializaCampos() {
        try {
            setCampos()
        } catch (e: Exception) {
            when (e) {
                is java.lang.IllegalArgumentException -> {
                    quandoDarIllegalArgumentException(e.message)
                }
            }
            Log.e("ERRO MOEDA", "onViewCreated: ${e.message}")
        }
    }


    private fun setCampos() {
        val formatoNomeMoeda = moeda.abreviacao + " - " + moeda.name
        val formatoValorVenda = "Venda: " + moeda.sell?.formatoMoedaBrasileira()
        val formatoValorCompra = "Compra: " + moeda.buy?.formatoMoedaBrasileira()

        cardView_cambio_abreviacao_nome_moeda.text = formatoNomeMoeda
        setCampoVariation()
        cardView_cambio_valor_venda_moeda.text = formatoValorVenda
        cardView_cambio_valor_compra_moeda.text = formatoValorCompra
    }

    private fun setCampoVariation() {
        cardView_cambio_variation_moeda.text = moeda.variation.formatoPorcentagem()
        cardView_cambio_variation_moeda.setTextColor(moeda.retornaCor(activity?.baseContext!!))
    }

}