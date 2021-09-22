package br.com.brq.agatha.investimentos.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.extension.setTitulo
import br.com.brq.agatha.investimentos.extension.transacaoFragment
import br.com.brq.agatha.investimentos.fragment.CambioFragment

class CambioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambio)
        setTitulo("Câmbio")

        val cambioFragment = CambioFragment()
        transacaoFragment {
            replace(R.id.activity_cambio_container, cambioFragment)
        }

    }
}