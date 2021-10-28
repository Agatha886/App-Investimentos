package br.com.brq.agatha.investimentos.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.util.CHAVE_MOEDA
import br.com.brq.agatha.investimentos.util.CHAVE_RESPOSTA_MENSAGEM
import br.com.brq.agatha.investimentos.model.TipoTranferencia
import br.com.brq.agatha.investimentos.extension.setMyActionBar
import br.com.brq.agatha.investimentos.extension.transacaoFragment
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.ui.fragment.CambioFragment
import br.com.brq.agatha.investimentos.ui.fragment.RespostaFragment
import java.io.Serializable

@Suppress("DEPRECATION")
class
CambioActivity() : AppCompatActivity() {

    private var setTituloAppBar: (tipoTransferencia: TipoTranferencia) -> String = { tipoTranferencia ->
        if(tipoTranferencia == TipoTranferencia.COMPRA){"Compra"}else if(tipoTranferencia== TipoTranferencia.VENDA){"Venda"}else{"Câmbio"}
    }

    private var tipoTransferencia = TipoTranferencia.INDEFINIDO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambio)
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
            is RespostaFragment ->{
                setMyActionBar(setTituloAppBar(tipoTransferencia), true, setOnClickButtonVoltar = {
                    onBackPressed()
                })
            }
        }
    }

    private fun iniciaComFragmentCambio() {
        val cambioFragment = CambioFragment()
        setArgumentsDadosMoedas(cambioFragment)
        transacaoFragment {
            replace(R.id.activity_cambio_container, cambioFragment, "CAMBIO")
        }
    }

    private fun setArgumentsDadosMoedas(cambioFragment: CambioFragment) {
        if (intent.hasExtra(CHAVE_MOEDA)) {
            val serializableExtra: Serializable? = intent.getSerializableExtra(CHAVE_MOEDA)
            if (serializableExtra != null) {
                val moedaRecebida = serializableExtra as Moeda
                val moedaBundle = Bundle()
                moedaBundle.putSerializable(CHAVE_MOEDA, moedaRecebida)
                cambioFragment.arguments = moedaBundle
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        }else{
            setMyActionBar("Câmbio", true, setOnClickButtonVoltar = {
                voltaParaTelaDeMoedas()
            })
        }
    }

    private fun configuraFragmentsCambio(fragment: CambioFragment) {
        setAcaoQuandoMoedaInvalida(fragment)
        vaiParaFragmentRespostaQuandoCompraOuVenda(fragment)
    }

    private fun vaiParaFragmentRespostaQuandoCompraOuVenda(fragment: CambioFragment) {
        val respostaFragment = RespostaFragment()
        fragment.quandoCompraOuVendaSucesso = { mensagem, tipoTranferencia ->
            replaceParaFragmentSucesso(mensagem, respostaFragment)
            tipoTransferencia = tipoTranferencia
        }
    }

    private fun replaceParaFragmentSucesso(
        mensagem: String,
        respostaFragment: RespostaFragment
    ) {
        val dados = Bundle()
        dados.putString(CHAVE_RESPOSTA_MENSAGEM, mensagem)
        respostaFragment.arguments = dados
        transacaoFragment {
            replace(R.id.activity_cambio_container, respostaFragment, "RESPOSTA")
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
        val intent = Intent(this, HomeMoedasActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
    }

}