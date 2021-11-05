package br.com.brq.agatha.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import br.com.brq.agatha.base.R
import br.com.brq.agatha.base.R.color.cinza
import br.com.brq.agatha.base.R.color.white
import br.com.brq.agatha.base.R.drawable.button_cambio
import br.com.brq.agatha.base.R.drawable.button_cambio_apagado
import br.com.brq.agatha.base.R.layout.cambio
import br.com.brq.agatha.domain.model.Moeda
import br.com.brq.agatha.domain.model.TipoTranferencia
import br.com.brq.agatha.domain.util.CHAVE_MOEDA
import br.com.brq.agatha.domain.util.ID_USUARIO
import br.com.brq.agatha.domain.util.formatoMoedaBrasileira
import br.com.brq.agatha.domain.util.formatoPorcentagem
import br.com.brq.agatha.investimentos.viewModel.CambioViewModel
import br.com.brq.agatha.investimentos.viewModel.RetornoStadeCompraEVenda
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.BigDecimal

class CambioFragment : Fragment() {
    private lateinit var btnComprar: Button
    private lateinit var btnVender: Button
    private lateinit var campoQuantidade: EditText
    private lateinit var moedaAbreviacaoENome: TextView
    private lateinit var moedaValorVenda: TextView
    private lateinit var moedaVariacao: TextView
    private lateinit var moedaValorCompra: TextView
    private lateinit var saldoUsuario: TextView
    private lateinit var totalMoeda: TextView
    private lateinit var textCompraInvalida: TextView
    private lateinit var textVendaInvalida: TextView

    private val moeda: Moeda by lazy {
        val moedaSerializable = arguments?.getSerializable(CHAVE_MOEDA)
            ?: throw IllegalArgumentException("Moeda Inválida")
        moedaSerializable as Moeda
    }

    var quandoCompraOuVendaSucesso: (mensagem: String, tipoTranferencia: TipoTranferencia) -> Unit =
        { _: String, _: TipoTranferencia -> }

    var quandoRecebidaMoedaInvalida: (mensagem: String?) -> Unit = {}

    private val viewModel by viewModel<CambioViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(cambio, container, false)
        btnComprar = view.findViewById(R.id.cambio_button_comprar)
        btnVender = view.findViewById(R.id.cambio_button_vender)
        campoQuantidade = view.findViewById(R.id.cambio_quantidade)
        moedaAbreviacaoENome = view.findViewById(R.id.cardView_cambio_abreviacao_nome_moeda)
        moedaValorVenda = view.findViewById(R.id.cardView_cambio_valor_venda_moeda)
        moedaValorCompra = view.findViewById(R.id.cardView_cambio_valor_compra_moeda)
        saldoUsuario = view.findViewById(R.id.cambio_saldo_disponivel)
        totalMoeda = view.findViewById(R.id.cambio_saldo_moeda)
        moedaVariacao = view.findViewById(R.id.cardView_cambio_variation_moeda)
        textCompraInvalida = view.findViewById(R.id.cardView_cambio_mensagem_compra_invalida)
        textVendaInvalida = view.findViewById(R.id.cardView_cambio_mensagem_venda_invalida)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.adicionaUsuario(Usuario(saldoDisponivel = BigDecimal(1000)))
        inicializaCampos()
        observerViewModel()
        estilizaBotaoOperacaoInvalida(btnComprar)
        estilizaBotaoOperacaoInvalida(btnVender)
        setCampoQuantidadeMoeda()
    }

    private fun observerViewModel() {
        viewModel.viewEventRetornoCompraEVenda.observe(viewLifecycleOwner, Observer {
            when (it) {
                is RetornoStadeCompraEVenda.SucessoCompra -> {
                    estilizaBotaoOperacaoValida(btnComprar)
                    setClickComprar(it.valorDeMoedaComprado)
                }
                is RetornoStadeCompraEVenda.FalhaCompra -> {
                    estilizaBotaoOperacaoInvalida(btnComprar)
                    Log.e("ERRO AO COMPRAR", "observerViewModel: ${it.mensagemErro}")
                }

                is RetornoStadeCompraEVenda.SucessoVenda -> {
                    estilizaBotaoOperacaoValida(btnVender)
                    setClickVender(it.totalMoedas, it.valorGastoNaVenda)
                }
                is RetornoStadeCompraEVenda.FalhaVenda -> {
                    estilizaBotaoOperacaoInvalida(btnVender)
                    Log.e("ERRO AO VENDER", "observerViewModel: ${it.mensagemErro}")
                }

                is RetornoStadeCompraEVenda.SemRetorno -> setBotoesQuandoInvalidos("Valor Nulo")
            }
        })

    }

    private fun setCampoQuantidadeMoeda() {
        campoQuantidade.doAfterTextChanged { valorDigitado ->
            val texto = valorDigitado.toString()
            if (texto.isBlank() || texto[0] == '0') {
                setBotoesQuandoInvalidos("Valor Inválido")
            } else {
                viewModel.compra(moeda, texto)
                viewModel.venda(moeda.name, texto)
            }
        }
    }

    private fun estilizaBotaoOperacaoInvalida(button: Button) {
        button.setBackgroundResource(button_cambio_apagado)
        button.setTextColor(resources.getColor(cinza))
        button.setOnClickListener { toastMensagem("Operação Inválida") }
    }

    private fun estilizaBotaoOperacaoValida(button: Button) {
        button.setBackgroundResource(button_cambio)
        button.setTextColor(resources.getColor(white))
    }

    private fun setClickVender(totalMoeda: Int, valorVenda: String) {
        btnVender.setOnClickListener {
            val novoTotalDeMoedas = setTotalDeMoedasAposVenda(totalMoeda)
            vaiParaFragmentSucessoAposVenda(novoTotalDeMoedas, valorVenda)
            limpaCampos()
        }
    }

    private fun setTotalDeMoedasAposVenda(totalMoeda: Int): LiveData<BigDecimal> {
        val saldoVenda =
        viewModel.setSaldoAposVenda(ID_USUARIO, moeda, campoQuantidade.text.toString())
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
        btnComprar.visibility = VISIBLE
        btnVender.visibility = VISIBLE
        btnComprar.setOnClickListener { toastMensagem(mensgem) }
        btnVender.setOnClickListener { toastMensagem(mensgem) }
    }

    private fun toastMensagem(mensgem: String) {
        Toast.makeText(
            requireContext(),
            mensgem,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setClickComprar(valorDeMoedaComprado: String) {
        btnComprar.setOnClickListener {
            setSaldoAposCompra(valorDeMoedaComprado)
            limpaCampos()
        }
    }

    private fun vaiParaFragmentSucessoAposCompra(
        valorDoSaldoDoUsuario: BigDecimal,
        quantidadeMoedaComprado: String
    ) {
        quandoCompraOuVendaSucesso(
            mensagemOperacaoSucesso(
                saldo = valorDoSaldoDoUsuario,
                "comprar ",
                quantidadeMoeda = quantidadeMoedaComprado
            ),
            TipoTranferencia.COMPRA
        )
    }

    private fun setSaldoAposCompra(valor: String) {
        viewModel.setToltalMoedaCompra(
            moeda.name,
            campoQuantidade.text.toString().toInt()
        )

        viewModel.setSaldoCompra(valor, moeda)
            .observe(viewLifecycleOwner, Observer { novoValorSaldo ->
                vaiParaFragmentSucessoAposCompra(novoValorSaldo, valor)
            })
    }

    private fun limpaCampos() {
        viewModel.setEventRetornoComoSem()
        campoQuantidade.setText("")
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

        configuraCampoNomeEAbreviacaoMoeda()
        setCampoVariation()
        configuraCampoVendaECompra()

        viewModel.getTotalMoeda(moeda.name).observe(viewLifecycleOwner, Observer {
            totalMoeda.text = (moeda.setMoedaSimbulo(it))
        })

        viewModel.getSaldoDisponivel().observe(viewLifecycleOwner, Observer {
            saldoUsuario.text = it.formatoMoedaBrasileira()
        })
    }

    private fun configuraCampoNomeEAbreviacaoMoeda() {
        val formatoNomeMoeda = moeda.abreviacao + " - " + moeda.name
        moedaAbreviacaoENome.text = formatoNomeMoeda
    }

    private fun configuraCampoVendaECompra() {
        if (moeda.buy == BigDecimal.ZERO) {
            moedaValorCompra.setTextColor(resources.getColor(R.color.red))
            textCompraInvalida.visibility = VISIBLE
        } else if (moeda.sell == BigDecimal.ZERO) {
            moedaValorVenda.setTextColor(resources.getColor(R.color.red))
            textVendaInvalida.visibility = VISIBLE
        }
        val formatoValorVenda = "Venda: " + moeda.sell?.formatoMoedaBrasileira()
        val formatoValorCompra = "Compra: " + moeda.buy?.formatoMoedaBrasileira()
        moedaValorVenda.text = formatoValorVenda
        moedaValorCompra.text = formatoValorCompra
    }

    private fun setCampoVariation() {
        moedaVariacao.text = moeda.variation.formatoPorcentagem()
        moedaVariacao.setTextColor(moeda.retornaCor(activity?.baseContext!!))
    }
}