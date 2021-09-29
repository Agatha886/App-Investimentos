package br.com.brq.agatha.investimentos.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.constantes.CHAVE_MOEDA
import br.com.brq.agatha.investimentos.extension.formatoMoedaBrasileira
import br.com.brq.agatha.investimentos.extension.formatoPorcentagem
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.viewModel.MoedaViewModel
import br.com.brq.agatha.investimentos.viewModel.UsuarioViewModel
import kotlinx.android.synthetic.main.cambio.*
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import java.math.BigDecimal

class CambioFragment : Fragment() {

    private val moeda: Moeda by lazy {
        val moedaSerializable = arguments?.getSerializable(CHAVE_MOEDA)
            ?: throw IllegalArgumentException("Moeda Inválida")
        moedaSerializable as Moeda
    }

    var quandoDarIllegalArgumentException: (mensagem: String?) -> Unit = {}

    private val usuarioViewModel: UsuarioViewModel by lazy {
        UsuarioViewModel(requireContext())
    }

    private val moedaViewModel: MoedaViewModel by lazy {
        MoedaViewModel(requireContext())
    }

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

        cambio_quantidade.doAfterTextChanged { valorDigitado ->
            val texto = valorDigitado.toString()
            if (texto.isBlank()) {
                cambio_button_comprar.setOnClickListener {
                    Toast.makeText(requireContext(), "Valor inválido", Toast.LENGTH_LONG).show()
                }
            } else {
                calculaCompra(texto)
            }
        }

    }

    private fun calculaCompra(texto: String) {
        usuarioViewModel.calculaSaldoAposCompra(1, moeda, texto)
        usuarioViewModel.quandoSucesso = { saldo ->
            cambio_button_comprar.visibility = VISIBLE
            setClickComprar(saldo)
        }
        usuarioViewModel.quandoFalha = { erro ->
            cambio_button_comprar.visibility = INVISIBLE
            Log.e("VALOR INVÁLIDO", "calculaCompra: $erro", )
        }
    }

    private fun setClickComprar(valor: BigDecimal) {
        cambio_button_comprar.setOnClickListener {
            setDadosAposCompra(valor)
        }
    }

    private fun setDadosAposCompra(valor: BigDecimal) {
        usuarioViewModel.usuario(1).observe(viewLifecycleOwner, Observer {
            it.saldoDisponivel = valor
            usuarioViewModel.modificaUsuario(it)
        })
        moeda.setTotal(cambio_quantidade.text.toString())
        moedaViewModel.modifica(moeda)
    }

    private fun setvenda() {
        cambio_button_vender.setOnClickListener {
            Toast.makeText(activity?.baseContext!!, "Clicou no botão de venda", Toast.LENGTH_LONG)
                .show()
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
        cambio_saldo_moeda.text = "0 "
    }

    private fun setCampoVariation() {
        cardView_cambio_variation_moeda.text = moeda.variation.formatoPorcentagem()
        cardView_cambio_variation_moeda.setTextColor(moeda.retornaCor(activity?.baseContext!!))
    }

}