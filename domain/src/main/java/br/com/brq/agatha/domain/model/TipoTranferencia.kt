package br.com.brq.agatha.domain.model

import java.io.Serializable

enum class TipoTranferencia:Serializable {
    COMPRA,
    VENDA,
    INDEFINIDO
}