package br.com.brq.agatha.investimentos.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.domain.util.CHAVE_RESPOSTA_MENSAGEM
import kotlinx.android.synthetic.main.fragment_resposta.*

class RespostaFragment : Fragment() {

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
