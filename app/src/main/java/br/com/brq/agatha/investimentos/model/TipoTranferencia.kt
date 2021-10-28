package br.com.brq.agatha.investimentos.model

import java.io.Serializable

enum class TipoTranferencia:Serializable {
    COMPRA,
    VENDA,
    INDEFINIDO
}