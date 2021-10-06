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
import androidx.lifecycle.Observer
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.constantes.CHAVE_MOEDA
import br.com.brq.agatha.investimentos.constantes.TipoTranferencia
import br.com.brq.agatha.investimentos.extension.formatoMoedaBrasileira
import br.com.brq.agatha.investimentos.extension.formatoPorcentagem
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.viewModel.CambioViewModel
import br.com.brq.agatha.investimentos.viewModel.RetornoStade
import kotlinx.android.synthetic.main.cambio.*
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

    private val viewModel: CambioViewModel by lazy {
        CambioViewModel(requireContext())
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
        observerViewModel()
        setCliqueBotoesQuandoInvalidos("Valor nulo")

        RetornoStade.eventRetorno.observe(viewLifecycleOwner, Observer {
            when (it) {
                is RetornoStade.FalhaApi -> {
                    setCliqueBotoesQuandoInvalidos("Dados não atualizados!!")
                }
                else -> {
                    setCampoQuantidadeMoeda()
                }
            }
        })

    }

    private fun observerViewModel() {
        RetornoStade.eventRetorno.observe(viewLifecycleOwner, Observer {
            when (it) {
                is RetornoStade.SucessoCompra -> {
                    estilizaBotaoValido(cambio_button_comprar)
                    setClickComprar(it.saldo)
                }
                is RetornoStade.FalhaCompra -> {
                    estilizaBotaoInvalido(cambio_button_comprar)
                    Log.e("ERRO AO COMPRAR", "observerViewModel: ${it.mensagemErro}")
                }
            }
        })

        RetornoStade.eventRetorno.observe(viewLifecycleOwner, Observer {
            when (it) {
                is RetornoStade.SucessoVenda -> {
                    estilizaBotaoValido(cambio_button_vender)
                    setCliqueBotaoVender(it.totalMoedas)
                }
                is RetornoStade.FalhaVenda -> {
                    estilizaBotaoInvalido(cambio_button_vender)
                    Log.e("ERRO AO VENDER", "observerViewModel: ${it.mensagemErro}")
                }
            }
        })
    }

    private fun setCampoQuantidadeMoeda() {
        cambio_quantidade.doAfterTextChanged { valorDigitado ->
            val texto = valorDigitado.toString()
            if (texto.isBlank() || texto[0] == '0') {
                setCliqueBotoesQuandoInvalidos("Valor Inválido")
            } else {
                viewModel.compra(1, moeda, valorDigitado.toString())
                viewModel.venda(moeda.name, valorDigitado.toString())
            }
        }
    }

    private fun estilizaBotaoInvalido(button: Button) {
        button.setBackgroundResource(R.drawable.button_cambio_apagado)
        button.setTextColor(resources.getColor(R.color.cinza))
        button.setOnClickListener { toastMensagem("Operação Inválida") }
    }

    private fun estilizaBotaoValido(button: Button) {
        button.setBackgroundResource(R.drawable.button_cambio)
        button.setTextColor(resources.getColor(R.color.white))
    }

    private fun setCliqueBotaoVender(totalMoeda: Double) {
        cambio_button_vender.setOnClickListener {
            val saldoVenda = viewModel.setSaldoVenda(1, moeda, cambio_quantidade.text.toString())
            viewModel.setTotalMoedaVenda(moeda.name, totalMoeda)
            saldoVenda.observe(viewLifecycleOwner, Observer {
                quandoCompraOuVendaSucesso(
                    mensagemOperacaoSucesso(it, "vender "),
                    TipoTranferencia.VENDA
                )
            })
        }
    }

    private fun setCliqueBotoesQuandoInvalidos(mensgem: String) {
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

    private fun setClickComprar(valor: BigDecimal) {
        cambio_button_comprar.setOnClickListener {
            viewModel.setSaldoCompra(1, valor)
            viewModel.setToltalMoedaCompra(moeda.name, cambio_quantidade.text.toString().toDouble())
            quandoCompraOuVendaSucesso(
                mensagemOperacaoSucesso(valor, "comprar "),
                TipoTranferencia.COMPRA
            )
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
                    quandoRecebidaMoedaInvalida(e.message)
                }
            }
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
