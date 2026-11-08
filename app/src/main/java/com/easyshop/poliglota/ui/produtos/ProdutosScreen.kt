package com.easyshop.poliglota.ui.produtos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.easyshop.poliglota.data.nosql.Produto
import com.easyshop.poliglota.ui.FaixaDoBanco
import com.easyshop.poliglota.ui.TextoAviso
import java.text.NumberFormat
import java.util.Locale

private val formatoReais: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

@Composable
fun ProdutosScreen(viewModel: ProdutosViewModel) {
    val produtos by viewModel.produtos.collectAsState()
    val aviso by viewModel.aviso.collectAsState()

    var nome by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var chave by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    val atributos = remember { mutableStateListOf<Pair<String, String>>() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FaixaDoBanco(
                titulo = "Banco NoSQL local: ObjectBox",
                detalhe = "Cada produto é um documento com atributos livres, sem schema fixo."
            )
        }
        item { Text("Novo produto", style = MaterialTheme.typography.titleMedium) }
        item {
            OutlinedTextField(
                value = nome, onValueChange = { nome = it },
                label = { Text("Nome") }, singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = preco, onValueChange = { preco = it },
                label = { Text("Preço (R$)") }, singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = categoria, onValueChange = { categoria = it },
                label = { Text("Categoria (opcional)") }, singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
          Column {
            Text("Atributos dinâmicos (opcional)", style = MaterialTheme.typography.labelLarge)
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = chave, onValueChange = { chave = it },
                    label = { Text("Atributo") }, singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(
                    value = valor, onValueChange = { valor = it },
                    label = { Text("Valor") }, singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                OutlinedButton(onClick = {
                    if (chave.isNotBlank() && valor.isNotBlank()) {
                        atributos.removeAll { it.first.equals(chave.trim(), ignoreCase = true) }
                        atributos.add(chave.trim() to valor.trim())
                        chave = ""
                        valor = ""
                    }
                }) { Text("+") }
            }
          }
        }
        items(atributos.toList(), key = { it.first }) { (k, v) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("• $k: $v", modifier = Modifier.weight(1f))
                IconButton(onClick = { atributos.remove(k to v) }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Remover atributo")
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    viewModel.cadastrar(nome, preco, categoria, atributos.toMap()) {
                        nome = ""; preco = ""; categoria = ""; chave = ""; valor = ""
                        atributos.clear()
                    }
                }) { Text("Salvar produto") }
                OutlinedButton(onClick = { viewModel.carregarExemplos() }) { Text("Carregar exemplos") }
            }
        }
        item { TextoAviso(aviso) }
        item {
            Column {
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                Text(
                    "Catálogo (${produtos.size}) — lido do ObjectBox",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        if (produtos.isEmpty()) {
            item { Text("Nenhum produto cadastrado.") }
        }
        items(produtos, key = { it.id }) { produto ->
            CartaoProduto(produto, aoExcluir = { viewModel.excluir(produto) })
        }
    }
}

@Composable
private fun CartaoProduto(produto: Produto, aoExcluir: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(produto.nome, style = MaterialTheme.typography.titleSmall)
                    Text(formatoReais.format(produto.preco), style = MaterialTheme.typography.bodyMedium)
                    produto.categoria?.let {
                        Text("Categoria: $it", style = MaterialTheme.typography.bodySmall)
                    }
                }
                IconButton(onClick = aoExcluir) {
                    Icon(Icons.Filled.Delete, contentDescription = "Excluir produto")
                }
            }
            val atributos = produto.atributos
            if (atributos.isNullOrEmpty()) {
                Text("Sem atributos extras", style = MaterialTheme.typography.bodySmall)
            } else {
                atributos.entries.sortedBy { it.key }.forEach { (k, v) ->
                    Text("• $k: $v", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
