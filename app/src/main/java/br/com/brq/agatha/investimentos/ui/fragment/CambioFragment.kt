package br.com.brq.agatha.investimentos.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.constantes.CHAVE_MOEDA
import br.com.brq.agatha.investimentos.constantes.TipoTranferencia
import br.com.brq.agatha.investimentos.extension.formatoMoedaBrasileira
import br.com.brq.agatha.investimentos.extension.formatoPorcentagem
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.model.Usuario
import br.com.brq.agatha.investimentos.viewModel.CambioViewModel
import br.com.brq.agatha.investimentos.viewModel.RetornoStadeCompraEVenda
import kotlinx.android.synthetic.main.cambio.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.BigDecimal
import java.text.DecimalFormat

class CambioFragment : Fragment() {

    private val moeda: Moeda by lazy {
        val moedaSerializable = arguments?.getSerializable(CHAVE_MOEDA)
            ?: throw IllegalArgumentException("Moeda Inválida")
        moedaSerializable as Moeda
    }

    var quandoCompraOuVendaSucesso: (mensagem: String, tipoTranferencia: TipoTranferencia) -> Unit =
        { _: String, _: TipoTranferencia -> }

    var quandoRecebidaMoedaInvalida: (mensagem: String?) -> Unit = {}

    private val viewModel by viewModel<CambioViewModel>()

    private lateinit var saldoDoUsuario: BigDecimal

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.cambio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.adicionaUsuario(Usuario(saldoDisponivel = BigDecimal(1000)))
        inicializaCampos()
        observerViewModel()
        setBotoesQuandoInvalidos("Valor nulo")
        setCampoQuantidadeMoeda()
    }

    private fun observerViewModel() {
        viewModel.viewEventRetornoCompraEVenda.observe(viewLifecycleOwner, Observer {
            when (it) {
                is RetornoStadeCompraEVenda.SucessoCompra -> {
                    estilizaBotaoOperacaoValida(cambio_button_comprar)
                    setClickComprar(it.valorDeMoedaComprado)
                }
                is RetornoStadeCompraEVenda.FalhaCompra -> {
                    estilizaBotaoOperacaoInvalida(cambio_button_comprar)
                    Log.e("ERRO AO COMPRAR", "observerViewModel: ${it.mensagemErro}")
                }

                is RetornoStadeCompraEVenda.SucessoVenda -> {
                    estilizaBotaoOperacaoValida(cambio_button_vender)
                    setClickVender(it.totalMoedas, it.valorGastoNaVenda)
                }
                is RetornoStadeCompraEVenda.FalhaVenda -> {
                    estilizaBotaoOperacaoInvalida(cambio_button_vender)
                    Log.e("ERRO AO VENDER", "observerViewModel: ${it.mensagemErro}")
                }

                is RetornoStadeCompraEVenda.SemRetorno -> setBotoesQuandoInvalidos("Valor Nulo")
            }
        })

        viewModel.getSaldoDisponivel(1).observe(viewLifecycleOwner, Observer {
            saldoDoUsuario = it
        })

    }

    private fun setCampoQuantidadeMoeda() {
        cambio_quantidade.doAfterTextChanged { valorDigitado ->
            val texto = valorDigitado.toString()
            if (texto.isBlank() || texto[0] == '0') {
                setBotoesQuandoInvalidos("Valor Inválido")
            } else {
                viewModel.compra(1, moeda, texto)
                viewModel.venda(moeda.name, texto)
            }
        }
    }

    private fun estilizaBotaoOperacaoInvalida(button: Button) {
        button.setBackgroundResource(R.drawable.button_cambio_apagado)
        button.setTextColor(resources.getColor(R.color.cinza))
        button.setOnClickListener { toastMensagem("Operação Inválida") }
    }

    private fun estilizaBotaoOperacaoValida(button: Button) {
        button.setBackgroundResource(R.drawable.button_cambio)
        button.setTextColor(resources.getColor(R.color.white))
    }

    private fun setClickVender(totalMoeda: Double, valorVenda: String) {
        cambio_button_vender.setOnClickListener {
            val novoTotalDeMoedas = setTotalDeMoedasAposVenda(totalMoeda)
            vaiParaFragmentSucessoAposVenda(novoTotalDeMoedas, valorVenda)
            limpaCampos()
        }
    }

    private fun setTotalDeMoedasAposVenda(totalMoeda: Double): LiveData<BigDecimal> {
        val saldoVenda =
            viewModel.setSaldoVenda(1, moeda, cambio_quantidade.text.toString())
        viewModel.setTotalMoedaVenda(moeda.name, totalMoeda)
        return saldoVenda
    }

    private fun vaiParaFragmentSucessoAposVenda(
        saldoVenda: LiveData<BigDecimal>,
        valorVenda: String
    ) {
        saldoVenda.observe(viewLifecycleOwner, Observer {
            quandoCompraOuVendaSucesso(
                mensagemOperacaoSucesso(it, "vender ", valorVenda),
                TipoTranferencia.VENDA
            )
        })
    }

    private fun setBotoesQuandoInvalidos(mensgem: String) {
        cambio_button_comprar.visibility = VISIBLE
        cambio_button_vender.visibility = VISIBLE
        cambio_button_comprar.setOnClickListener { toastMensagem(mensgem) }
        cambio_button_vender.setOnClickListener { toastMensagem(mensgem) }
    }

    private fun toastMensagem(mensgem: String) {
        Toast.makeText(
            requireContext(),
            mensgem,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setClickComprar(valorDeMoedaComprado: String) {
        cambio_button_comprar.setOnClickListener {
            setSaldoAposCompra(BigDecimal(valorDeMoedaComprado))
            limpaCampos()
        }
    }

    private fun vaiParaFragmentSucessoAposCompra(valorDoSaldoDoUsuario: BigDecimal, quantidadeMoedaComprado: String) {
        quandoCompraOuVendaSucesso(
            mensagemOperacaoSucesso(saldo = valorDoSaldoDoUsuario, "comprar ", quantidadeMoeda = quantidadeMoedaComprado),
            TipoTranferencia.COMPRA
        )
    }

    private fun setSaldoAposCompra(valor: BigDecimal) {
        viewModel.setToltalMoedaCompra(
            moeda.name,
            cambio_quantidade.text.toString().toDouble()
        )
        viewModel.setSaldoCompra(1, valor).observe(viewLifecycleOwner, Observer { novoValorSaldo ->
            vaiParaFragmentSucessoAposCompra(novoValorSaldo, valor.toString())
        })
    }

    private fun limpaCampos() {
        viewModel.setEventRetornoComoSem()
        cambio_quantidade.setText("")
    }

    private fun mensagemOperacaoSucesso(
        saldo: BigDecimal,
        nomeOperacao: String,
        quantidadeMoeda: String
    ): String {
        val saldoFormatado = saldo.formatoMoedaBrasileira()
        val resposta = StringBuilder()
        resposta.append("Parabéns! \n Você acabou de ").append(nomeOperacao)
            .append(quantidadeMoeda).append(" ").append(moeda.abreviacao)
            .append(" - ")
            .append(moeda.name).append(", totalizando \n").append(saldoFormatado)
        return resposta.toString()
    }

    private fun inicializaCampos() {
        try {
            setCampos()
        } catch (e: Exception) {
            quandoRecebidaMoedaInvalida(e.message)
            Log.e("ERRO MOEDA", "onViewCreated: ${e.message}")
        }
    }

    private fun setCampos() {
        val decimalFormat = DecimalFormat("#0.00")
        val formatoNomeMoeda = moeda.abreviacao + " - " + moeda.name
        val formatoValorVenda = "Venda: " + moeda.setMoedaSimbulo(moeda.sell ?: BigDecimal.ZERO)
        val formatoValorCompra = "Compra: " + moeda.setMoedaSimbulo(moeda.buy ?: BigDecimal.ZERO)

        cardView_cambio_abreviacao_nome_moeda.text = formatoNomeMoeda
        setCampoVariation()
        cardView_cambio_valor_venda_moeda.text = formatoValorVenda
        cardView_cambio_valor_compra_moeda.text = formatoValorCompra

        viewModel.getTotalMoeda(moeda.name).observe(viewLifecycleOwner, Observer {
            val formatoTotalMoeda = decimalFormat.format(it)
            cambio_saldo_moeda.text = ("$formatoTotalMoeda ${moeda.name} ")
        })

        viewModel.getSaldoDisponivel(1).observe(viewLifecycleOwner, Observer {
            cambio_saldo_disponivel.text = it.formatoMoedaBrasileira()
        })
    }

    private fun setCampoVariation() {
        cardView_cambio_variation_moeda.text = moeda.variation.formatoPorcentagem()
        cardView_cambio_variation_moeda.setTextColor(moeda.retornaCor(activity?.baseContext!!))
    }
}