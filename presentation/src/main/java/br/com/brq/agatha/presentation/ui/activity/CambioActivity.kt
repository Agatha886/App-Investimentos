package br.com.brq.agatha.presentation.ui.activity

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.brq.agatha.base.R.id.activity_cambio_container
import br.com.brq.agatha.base.R.layout.activity_cambio
import br.com.brq.agatha.base.util.QuandoSucessoCompraOuVenda
import br.com.brq.agatha.base.util.setMyActionBar
import br.com.brq.agatha.base.util.transacaoFragment
import br.com.brq.agatha.domain.model.Moeda
import br.com.brq.agatha.domain.model.TipoTranferencia
import br.com.brq.agatha.domain.util.CHAVE_MOEDA
import br.com.brq.agatha.domain.util.CHAVE_RESPOSTA_MENSAGEM
import br.com.brq.agatha.presentation.ui.fragment.CambioFragment
import br.com.brq.agatha.presentation.ui.fragment.RespostaFragment
import java.io.Serializable

@Suppress("DEPRECATION")
class
CambioActivity : AppCompatActivity() {

    private var setTituloAppBar: (tipoTransferencia: TipoTranferencia) -> String =
        { tipoTranferencia ->
            when (tipoTranferencia) {
                TipoTranferencia.COMPRA -> {
                    "Compra"
                }
                TipoTranferencia.VENDA -> {
                    "Venda"
                }
                else -> {
                    "Câmbio"
                }
            }
        }

    private var tipoTransferencia = TipoTranferencia.INDEFINIDO

    private var retornoCompraEVenda = MutableLiveData<QuandoSucessoCompraOuVenda>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_cambio)
        iniciaComFragmentCambio()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is CambioFragment -> {
                setMyActionBar("Câmbio", true, setOnClickButtonVoltar = {
                    voltaParaTelaDeMoedas()
                })
                configuraFragmentsCambio(fragment)
            }
            is RespostaFragment -> {
                setMyActionBar(setTituloAppBar(tipoTransferencia), true, setOnClickButtonVoltar = {
                    onBackPressed()
                })
            }
        }
    }

    private fun iniciaComFragmentCambio() {
        val cambioFragment = CambioFragment(retornoCompraEVenda)
        setArgumentsDadosMoedas(cambioFragment)
        transacaoFragment {
            replace(activity_cambio_container, cambioFragment, "CAMBIO")
        }
    }

    private fun setArgumentsDadosMoedas(cambioFragment: CambioFragment) {
        if (intent.hasExtra(CHAVE_MOEDA)) {
            val serializableExtra: Serializable? =
                intent.getSerializableExtra(CHAVE_MOEDA)
            if (serializableExtra != null) {
                val moedaRecebida = serializableExtra as Moeda
                val moedaBundle = Bundle()
                moedaBundle.putSerializable(
                    CHAVE_MOEDA,
                    moedaRecebida
                )
                cambioFragment.arguments = moedaBundle
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            setMyActionBar("Câmbio", true, setOnClickButtonVoltar = {
                voltaParaTelaDeMoedas()
            })
        }
    }

    private fun configuraFragmentsCambio(fragment: CambioFragment) {
        setAcaoQuandoMoedaInvalida(fragment)
        vaiParaFragmentRespostaQuandoCompraOuVenda()
    }

    private fun vaiParaFragmentRespostaQuandoCompraOuVenda() {
        val respostaFragment = RespostaFragment()
        retornoCompraEVenda.observe(this, Observer {
            tipoTransferencia = when(it){
                is QuandoSucessoCompraOuVenda.compraSucesso -> {
                    replaceParaFragmentSucesso(it.mensagem, respostaFragment)
                    TipoTranferencia.COMPRA
                }
                is QuandoSucessoCompraOuVenda.vendaSucesso -> {
                    replaceParaFragmentSucesso(it.mensagem, respostaFragment)
                    TipoTranferencia.VENDA
                }
            }
        })
    }

    private fun replaceParaFragmentSucesso(
        mensagem: String,
        respostaFragment: RespostaFragment
    ) {
        val dados = Bundle()
        dados.putString(CHAVE_RESPOSTA_MENSAGEM, mensagem)
        respostaFragment.arguments = dados
        transacaoFragment {
            replace(activity_cambio_container, respostaFragment, "RESPOSTA")
            addToBackStack("CAMBIO")
        }
    }

    private fun setAcaoQuandoMoedaInvalida(fragment: CambioFragment) {
        fragment.quandoRecebidaMoedaInvalida = {
            voltaParaTelaDeMoedas()
            Toast.makeText(
                this,
                it,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun voltaParaTelaDeMoedas() {
        val intent = Intent()
        intent.component = ComponentName(
            "br.com.brq.agatha.investimentos",
            "br.com.brq.agatha.investimentos.ui.HomeMoedasActivity"
        )
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
        finish()
    }

}