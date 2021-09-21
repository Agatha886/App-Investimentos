package br.com.brq.agatha.investimentos

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

fun AppCompatActivity.setTitulo(titulo: String) {
    val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
    setSupportActionBar(toolbar)
    val title = findViewById<TextView>(R.id.my_toolbar_title)
    title.text = titulo
}