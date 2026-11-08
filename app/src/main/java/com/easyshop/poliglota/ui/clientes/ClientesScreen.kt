package com.easyshop.poliglota.ui.clientes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.easyshop.poliglota.ui.FaixaDoBanco
import com.easyshop.poliglota.ui.TextoAviso

@Composable
fun ClientesScreen(viewModel: ClientesViewModel) {
    val clientes by viewModel.clientes.collectAsState()
    val aviso by viewModel.aviso.collectAsState()

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FaixaDoBanco(
                titulo = "Banco SQL local: SQLite (Room)",
                detalhe = "Tabela clientes com chave primária e e-mail único."
            )
        }
        item { Text("Novo cliente", style = MaterialTheme.typography.titleMedium) }
        item {
            OutlinedTextField(
                value = nome, onValueChange = { nome = it },
                label = { Text("Nome completo") }, singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("E-mail") }, singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Button(onClick = {
                viewModel.cadastrar(nome, email) {
                    nome = ""
                    email = ""
                }
            }) { Text("Salvar cliente") }
        }
        item { TextoAviso(aviso) }
        item {
            Column {
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                Text(
                    "Clientes (${clientes.size}) — lidos do SQLite",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        if (clientes.isEmpty()) {
            item { Text("Nenhum cliente cadastrado.") }
        }
        items(clientes, key = { it.id }) { cliente ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("#${cliente.id}  ${cliente.nomeCompleto}", style = MaterialTheme.typography.titleSmall)
                        Text(cliente.email, style = MaterialTheme.typography.bodyMedium)
                    }
                    IconButton(onClick = { viewModel.excluir(cliente) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Excluir cliente")
                    }
                }
            }
        }
    }
}
