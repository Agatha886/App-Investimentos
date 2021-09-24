package br.com.brq.agatha.investimentos.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.constantes.CHAVE_MOEDA
import br.com.brq.agatha.investimentos.extension.setMyActionBar
import br.com.brq.agatha.investimentos.extension.transacaoFragment
import br.com.brq.agatha.investimentos.fragment.CambioFragment
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.model.Usuario
import br.com.brq.agatha.investimentos.viewModel.UsuarioViewModel
import java.io.Serializable
import java.math.BigDecimal

@Suppress("DEPRECATION")
class CambioActivity : AppCompatActivity() {

    private val viewModel:UsuarioViewModel by lazy {
        UsuarioViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambio)
        setMyActionBar("Câmbio", true, setOnClickButtonVoltar = {
            voltaParaTelaDeMoedas()
        })
        iniciaComFragmentCambio()
//        viewModel.adicionaUsuario(Usuario(
//            saldoDisponivel = BigDecimal(1000)))
    }

    private fun iniciaComFragmentCambio() {
        val cambioFragment = CambioFragment()
        setArgumentsDadosMoedas(cambioFragment)
        transacaoFragment {
            replace(R.id.activity_cambio_container, cambioFragment)
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is CambioFragment -> {
                configuraFragmentsCambio(fragment)
            }
        }
    }

    private fun configuraFragmentsCambio(fragment: CambioFragment) {
        fragment.quandoDarIllegalArgumentException = {
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
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun setArgumentsDadosMoedas(cambioFragment: CambioFragment) {
        if (intent.hasExtra(CHAVE_MOEDA)) {
            val serializableExtra: Serializable? = intent.getSerializableExtra(CHAVE_MOEDA)
            if (serializableExtra != null) {
                val moeda = serializableExtra as Moeda
                val dados = Bundle()
                dados.putSerializable(CHAVE_MOEDA, moeda)
                cambioFragment.arguments = dados
            }
        }
    }
}