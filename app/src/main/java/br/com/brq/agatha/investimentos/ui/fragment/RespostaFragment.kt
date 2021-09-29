package br.com.brq.agatha.investimentos.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.brq.agatha.investimentos.R
import br.com.brq.agatha.investimentos.constantes.CHAVE_RESPOSTA
import kotlinx.android.synthetic.main.fragment_resposta.*

class RespostaFragment : Fragment() {

    private val resposta: String by lazy {
        val mensagemSerializable = arguments?.getSerializable(CHAVE_RESPOSTA)
            ?: throw IllegalArgumentException("Mensagem Inválida")
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
        try {
           resposta_mensagem.text = resposta
        }catch (e: Exception){
            Log.e("ERRO MENSAGEM", "onViewCreated: ${e.message}")
        }
    }
}