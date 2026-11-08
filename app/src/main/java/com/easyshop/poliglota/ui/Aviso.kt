package com.easyshop.poliglota.ui

import com.easyshop.poliglota.service.Resultado

/** Mensagem curta exibida abaixo do formulário. */
data class Aviso(val texto: String, val erro: Boolean)

fun Resultado.paraAviso(): Aviso = when (this) {
    is Resultado.Sucesso -> Aviso(mensagem, erro = false)
    is Resultado.Erro -> Aviso(mensagem, erro = true)
}
