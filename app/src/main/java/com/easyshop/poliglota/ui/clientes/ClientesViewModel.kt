package com.easyshop.poliglota.ui.clientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyshop.poliglota.data.sql.Cliente
import com.easyshop.poliglota.service.ClienteService
import com.easyshop.poliglota.service.Resultado
import com.easyshop.poliglota.ui.Aviso
import com.easyshop.poliglota.ui.paraAviso
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Estado da tela de Clientes. A lista vem do SQL e se atualiza sozinha (Flow). */
class ClientesViewModel(private val service: ClienteService) : ViewModel() {

    val clientes: StateFlow<List<Cliente>> = service.observarClientes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _aviso = MutableStateFlow<Aviso?>(null)
    val aviso: StateFlow<Aviso?> = _aviso

    fun cadastrar(nome: String, email: String, aoSalvar: () -> Unit) {
        viewModelScope.launch {
            val resultado = service.cadastrar(nome, email)
            _aviso.value = resultado.paraAviso()
            if (resultado is Resultado.Sucesso) aoSalvar()
        }
    }

    fun excluir(cliente: Cliente) {
        viewModelScope.launch {
            service.excluir(cliente)
            _aviso.value = Aviso("Cliente \"${cliente.nomeCompleto}\" removido do SQL.", erro = false)
        }
    }
}
