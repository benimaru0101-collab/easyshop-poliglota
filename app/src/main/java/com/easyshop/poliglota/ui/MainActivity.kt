package com.easyshop.poliglota.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.easyshop.poliglota.EasyShopApp
import com.easyshop.poliglota.ui.clientes.ClientesScreen
import com.easyshop.poliglota.ui.clientes.ClientesViewModel
import com.easyshop.poliglota.ui.produtos.ProdutosScreen
import com.easyshop.poliglota.ui.produtos.ProdutosViewModel

/** Camada de apresentação: duas abas, uma para cada banco. */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fabrica = FabricaViewModel((application as EasyShopApp).servicos)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var aba by rememberSaveable { mutableIntStateOf(0) }
                    val produtosVm: ProdutosViewModel = viewModel(factory = fabrica)
                    val clientesVm: ClientesViewModel = viewModel(factory = fabrica)

                    Column(Modifier.fillMaxSize()) {
                        Text(
                            "EasyShop — Persistência Poliglota",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
                        )
                        TabRow(selectedTabIndex = aba) {
                            Tab(selected = aba == 0, onClick = { aba = 0 }, text = { Text("Produtos (NoSQL)") })
                            Tab(selected = aba == 1, onClick = { aba = 1 }, text = { Text("Clientes (SQL)") })
                        }
                        when (aba) {
                            0 -> ProdutosScreen(produtosVm)
                            else -> ClientesScreen(clientesVm)
                        }
                    }
                }
            }
        }
    }
}
