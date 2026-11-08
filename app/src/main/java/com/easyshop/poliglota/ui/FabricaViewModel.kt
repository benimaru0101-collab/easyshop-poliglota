package com.easyshop.poliglota.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.easyshop.poliglota.di.Servicos
import com.easyshop.poliglota.ui.clientes.ClientesViewModel
import com.easyshop.poliglota.ui.produtos.ProdutosViewModel

/** Entrega a cada ViewModel apenas o serviço de que ele precisa. */
class FabricaViewModel(private val servicos: Servicos) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(ProdutosViewModel::class.java) ->
            ProdutosViewModel(servicos.produtoService) as T
        modelClass.isAssignableFrom(ClientesViewModel::class.java) ->
            ClientesViewModel(servicos.clienteService) as T
        else -> throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}
