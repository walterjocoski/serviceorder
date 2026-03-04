package com.rfpiscinas.serviceorder.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * VisualTransformation que exibe dígitos armazenados como DD/MM/YYYY.
 *
 * O campo armazena APENAS dígitos (máx 8). Esta classe injeta as barras
 * na posição correta para exibição, sem alterar o estado real.
 * O cursor se move corretamente porque o OffsetMapping é preciso.
 *
 * Estado: "30011993"  →  Exibido: "30/01/1993"
 * Estado: "3001"      →  Exibido: "30/01"
 * Estado: "30"        →  Exibido: "30/"  (NÃO — exibe "30", barra só aparece ao continuar)
 */
class DateMaskTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text  // já são só dígitos (garantido pelo onValueChange)
        val out = buildString {
            digits.forEachIndexed { i, ch ->
                if (i == 2 || i == 4) append('/')
                append(ch)
            }
        }

        /**
         * originalToTransformed(i): posição do i-ésimo dígito no texto visual
         *   dígito 0,1   → visual 0,1       (antes da primeira barra)
         *   dígito 2,3   → visual 3,4       (+1 pela barra em pos 2)
         *   dígito 4..7  → visual 6,7,8,9   (+2 pelas duas barras)
         *
         * transformedToOriginal(i): posição visual → dígito correspondente
         *   visual 0,1   → dígito 0,1
         *   visual 2     → barra → mapeia para dígito 2 (próximo dígito)
         *   visual 3,4   → dígito 2,3
         *   visual 5     → barra → mapeia para dígito 4
         *   visual 6..9  → dígito 4..7
         */
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = when {
                offset <= 2 -> offset
                offset <= 4 -> offset + 1
                else        -> offset + 2
            }

            override fun transformedToOriginal(offset: Int): Int = when {
                offset <= 2 -> offset
                offset <= 5 -> offset - 1
                else        -> offset - 2
            }.coerceIn(0, digits.length)
        }

        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}
