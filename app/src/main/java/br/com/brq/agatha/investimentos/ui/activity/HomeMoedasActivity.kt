package br.com.brq.agatha.investimentos.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.setTitulo
import br.com.brq.agatha.investimentos.ui.recyclerview.ListaMoedasAdpter
import kotlinx.android.synthetic.main.activity_moedas_home.*
import java.math.BigDecimal

class HomeMoedasActivity : AppCompatActivity() {

    val listaTeste: MutableList<Moeda> = mutableListOf<Moeda>()

    private val adapter: ListaMoedasAdpter by lazy {
        ListaMoedasAdpter(this@HomeMoedasActivity, listaTeste)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moedas_home)
        setTitulo("Home/Moeda")

        listaTeste.add(
            Moeda(
                name = "Dolar",
                buy = BigDecimal(5.251),
                abreviacao = "USD",
                sell = BigDecimal(5.2522),
                variation = "2.64%"
            )

        )
        home_recyclerView.adapter = adapter
        adapter.quandoMoedaClicado = {
            Toast.makeText(
                this@HomeMoedasActivity,
                it.abreviacao,
                Toast.LENGTH_LONG
            ).show()
        }

    }


}

