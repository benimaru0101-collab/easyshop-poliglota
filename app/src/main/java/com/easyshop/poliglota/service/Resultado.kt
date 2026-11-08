package com.easyshop.poliglota.service

/** Resposta da camada de negócio para a interface. */
sealed class Resultado {
    data class Sucesso(val mensagem: String) : Resultado()
    data class Erro(val mensagem: String) : Resultado()
}
