package br.com.brq.agatha.investimentos.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
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
import br.com.brq.agatha.investimentos.model.Usuario
import br.com.brq.agatha.investimentos.viewModel.MoedaViewModel
import br.com.brq.agatha.investimentos.viewModel.UsuarioViewModel
import kotlinx.android.synthetic.main.cambio.*
import java.math.BigDecimal
import java.text.DecimalFormat

class CambioFragment : Fragment() {

    private val moeda: Moeda by lazy {
        val moedaSerializable = arguments?.getSerializable(CHAVE_MOEDA)
            ?: throw IllegalArgumentException("Moeda Inválida")
        moedaSerializable as Moeda
    }

    var quandoCompraOuVendaSucesso: (mensagem: String, tituloAppBar: String) -> Unit = {_:String, _:String ->}

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
        setCliqueBotoesQuandoNulo()
        setCampoQuantidadeMoeda()
        usuarioViewModel.adicionaUsuario(Usuario(saldoDisponivel = BigDecimal(1000)))
    }

    private fun setCampoQuantidadeMoeda() {
        cambio_quantidade.doAfterTextChanged { valorDigitado ->
            val texto = valorDigitado.toString()
            if (texto.isBlank()) {
                setCliqueBotoesQuandoNulo()
            } else {
                calculaCompra(texto)
                configuraVenda(texto)
            }
        }
    }

    private fun configuraVenda(valorDeVenda: String) {
        moedaViewModel.quandoVendaSucesso = { totalMoeda ->
            cambio_button_vender.visibility = VISIBLE
            setCliqueBotaoVender(totalMoeda)
        }
        moedaViewModel.quandoVendaFalha = { erro ->
            cambio_button_vender.visibility = INVISIBLE
            Log.e("VALOR INVÁLIDO", "Venda não autorizada, motivo: $erro")
        }

        moedaViewModel.validaTotalMoedaVenda(moeda.name, valorDeVenda)
    }

    private fun setCliqueBotaoVender(totalMoeda: Double) {
        cambio_button_vender.setOnClickListener {
            val saldoVenda = usuarioViewModel.setSaldoVenda(1, moeda, cambio_quantidade.text.toString())
            moedaViewModel.calculaToltalMoedaVenda(moeda.name, totalMoeda)
            saldoVenda.observe(viewLifecycleOwner, Observer {
                quandoCompraOuVendaSucesso(mensagemOperacaoSucesso(it, "vender "), "Vender")
            })
        }
    }

    private fun setCliqueBotoesQuandoNulo() {
        cambio_button_comprar.visibility = VISIBLE
        cambio_button_vender.visibility = VISIBLE
        cambio_button_comprar.setOnClickListener { mensagemQuandoNulo() }
        cambio_button_vender.setOnClickListener { mensagemQuandoNulo() }
    }

    private fun mensagemQuandoNulo() {
        Toast.makeText(
            requireContext(),
            "Valor nulo",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun calculaCompra(texto: String) {
        configuraSucessoEFalhaCompra()
        usuarioViewModel.validaSaldoUsuarioCompra(1, moeda, texto)
    }


    private fun configuraSucessoEFalhaCompra() {
        usuarioViewModel.quandoCompraSucesso = { saldoRestante ->
            cambio_button_comprar.visibility = VISIBLE
            setClickComprar(saldoRestante)
        }

        usuarioViewModel.quandoCompraFalha = { erro ->
            cambio_button_comprar.visibility = INVISIBLE
            Log.e("VALOR INVÁLIDO", "Compra não autorizada, motivo: $erro")
        }
    }

    private fun setClickComprar(valor: BigDecimal) {
        cambio_button_comprar.setOnClickListener {
            usuarioViewModel.setSaldoCompra(1, valor)
            moedaViewModel.calculaToltalMoedaCompra(moeda.name, cambio_quantidade.text.toString().toDouble())
            quandoCompraOuVendaSucesso(mensagemOperacaoSucesso(valor, "comprar "), "Comprar")
        }
    }

    private fun mensagemOperacaoSucesso(saldo: BigDecimal, nomeOperacao: String): String {
        val saldoFormatado = saldo.formatoMoedaBrasileira()
        val resposta = StringBuilder()
        resposta.append("Parabéns! \n Você acabou de ").append(nomeOperacao)
            .append(cambio_quantidade.text.toString()).append(" ").append(moeda.abreviacao)
            .append(" - ")
            .append(moeda.name).append(", totalizando \n").append(saldoFormatado)

        return resposta.toString()
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
        val decimalFormat = DecimalFormat("#0.00")
        val formatoNomeMoeda = moeda.abreviacao + " - " + moeda.name
        val formatoValorVenda = "Venda: " + moeda.sell?.formatoMoedaBrasileira()
        val formatoValorCompra = "Compra: " + moeda.buy?.formatoMoedaBrasileira()

        cardView_cambio_abreviacao_nome_moeda.text = formatoNomeMoeda
        setCampoVariation()
        cardView_cambio_valor_venda_moeda.text = formatoValorVenda
        cardView_cambio_valor_compra_moeda.text = formatoValorCompra

        moedaViewModel.getTotalMoeda(moeda.name).observe(viewLifecycleOwner, Observer {
            val formatoTotalMoeda = decimalFormat.format(it)
            cambio_saldo_moeda.text = "$formatoTotalMoeda "
        })

        usuarioViewModel.getSaldoDisponivel(1).observe(viewLifecycleOwner, Observer {
            cambio_saldo_disponivel.text = it.formatoMoedaBrasileira()
        })
    }

    private fun setCampoVariation() {
        cardView_cambio_variation_moeda.text = moeda.variation.formatoPorcentagem()
        cardView_cambio_variation_moeda.setTextColor(moeda.retornaCor(activity?.baseContext!!))
    }

}