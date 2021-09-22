package br.com.brq.agatha.investimentos.extension

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import br.com.brq.agatha.investimentos.R

fun AppCompatActivity.setTitulo(titulo: String) {
    val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
    setSupportActionBar(toolbar)
    val title = findViewById<TextView>(R.id.my_toolbar_title)
    title.text = titulo
}

fun AppCompatActivity.transacaoFragment(executa: FragmentTransaction.() -> Unit) {
    val transacao = supportFragmentManager.beginTransaction()
    executa(transacao)
    transacao.commit()
}