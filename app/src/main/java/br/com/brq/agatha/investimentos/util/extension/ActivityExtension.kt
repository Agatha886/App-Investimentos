package br.com.brq.agatha.investimentos.util.extension

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import br.com.brq.agatha.investimentos.R

fun AppCompatActivity.setMyActionBar(titulo: String, buttonVoltar: Boolean, setOnClickButtonVoltar: () -> Unit ={}) {
    val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
    setSupportActionBar(toolbar)
    val title = findViewById<TextView>(R.id.my_toolbar_title)
    val button = findViewById<Button>(R.id.my_toolbar_button_voltar)

    title.text = titulo
    if(buttonVoltar){
        button.visibility = VISIBLE
        button.setOnClickListener {
            setOnClickButtonVoltar()
        }
    }else{
        button.visibility = INVISIBLE
    }
}

fun AppCompatActivity.transacaoFragment(executa: FragmentTransaction.() -> Unit) {
    val transacao = supportFragmentManager.beginTransaction()
    executa(transacao)
    transacao.commit()
}

fun AppCompatActivity.mensagem(texto: String) {
    Toast.makeText(
        this,
        texto,
        Toast.LENGTH_SHORT
    ).show()
}