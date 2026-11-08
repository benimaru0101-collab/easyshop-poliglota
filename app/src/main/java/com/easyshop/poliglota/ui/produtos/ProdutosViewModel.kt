package com.easyshop.poliglota.ui.produtos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyshop.poliglota.data.nosql.Produto
import com.easyshop.poliglota.service.ProdutoService
import com.easyshop.poliglota.service.Resultado
import com.easyshop.poliglota.ui.Aviso
import com.easyshop.poliglota.ui.paraAviso
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/** Estado da tela de Produtos. Não acessa banco: só chama o ProdutoService. */
class ProdutosViewModel(private val service: ProdutoService) : ViewModel() {

    private val _produtos = MutableStateFlow<List<Produto>>(emptyList())
    val produtos: StateFlow<List<Produto>> = _produtos

    private val _aviso = MutableStateFlow<Aviso?>(null)
    val aviso: StateFlow<Aviso?> = _aviso

    init {
        recarregar()
    }

    fun cadastrar(
        nome: String,
        preco: String,
        categoria: String,
        atributos: Map<String, String>,
        aoSalvar: () -> Unit
    ) {
        viewModelScope.launch {
            val resultado = service.cadastrar(nome, preco, categoria, atributos)
            _aviso.value = resultado.paraAviso()
            if (resultado is Resultado.Sucesso) {
                aoSalvar()
                recarregar()
            }
        }
    }

    fun carregarExemplos() {
        viewModelScope.launch {
            _aviso.value = service.carregarExemplos().paraAviso()
            recarregar()
        }
    }

    fun excluir(produto: Produto) {
        viewModelScope.launch {
            service.excluir(produto.id)
            _aviso.value = Aviso("Produto \"${produto.nome}\" removido do NoSQL.", erro = false)
            recarregar()
        }
    }

    private fun recarregar() {
        viewModelScope.launch {
            _produtos.value = service.listar()
        }
    }
}
