package br.com.brq.agatha.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import br.com.brq.agatha.base.R.layout.*
import br.com.brq.agatha.domain.util.CHAVE_RESPOSTA_MENSAGEM

class RespostaFragment : Fragment() {

    private lateinit var respostaTxt: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(fragment_resposta, container, false)
        respostaTxt = view.findViewById(br.com.brq.agatha.base.R.id.resposta_mensagem)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mensagemSerializable = arguments?.getSerializable(CHAVE_RESPOSTA_MENSAGEM)
            ?: throw IllegalArgumentException("Mensagem Inválida")

        val resposta: String  = mensagemSerializable as String

        try {
            respostaTxt.text = resposta
        }catch (e: Exception){
            Log.e("ERRO MENSAGEM", "onViewCreated: ${e.message}")
        }
    }

}
