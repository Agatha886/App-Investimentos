package br.com.brq.agatha.investimentos.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.constantes.CHAVE_RESPOSTA_MENSAGEM
import br.com.brq.agatha.investimentos.constantes.CHAVE_RESPOSTA_TITULO
import kotlinx.android.synthetic.main.fragment_resposta.*

class RespostaFragment : Fragment() {

     val tituloAppBar: String by lazy {
        val mensagemSerializable = arguments?.getSerializable(CHAVE_RESPOSTA_TITULO)
            ?:"Título Inválido"
        mensagemSerializable as String
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_resposta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mensagemSerializable = arguments?.getSerializable(CHAVE_RESPOSTA_MENSAGEM)
            ?: throw IllegalArgumentException("Mensagem Inválida")

        val resposta: String  = mensagemSerializable as String

        try {
            resposta_mensagem.text = resposta
        }catch (e: Exception){
            Log.e("ERRO MENSAGEM", "onViewCreated: ${e.message}")
        }
    }

}
