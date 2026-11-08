package com.easyshop.poliglota.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/** Faixa que mostra em qual banco a tela está gravando. */
@Composable
fun FaixaDoBanco(titulo: String, detalhe: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(12.dp)
    ) {
        Text(titulo, style = MaterialTheme.typography.titleSmall)
        Text(detalhe, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun TextoAviso(aviso: Aviso?) {
    if (aviso == null) return
    Text(
        text = aviso.texto,
        color = if (aviso.erro) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.bodyMedium
    )
}
