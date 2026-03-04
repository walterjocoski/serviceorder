package com.rfpiscinas.serviceorder.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Utilitários para campo de telefone brasileiro.
 *
 * O estado armazena APENAS dígitos (máx 11).
 * PhoneMaskTransformation exibe a máscara visualmente:
 *   10 dígitos → (XX) XXXX-XXXX   (telefone fixo)
 *   11 dígitos → (XX) XXXXX-XXXX  (celular)
 */
object PhoneUtils {
    fun filterDigits(input: String): String =
        input.filter { it.isDigit() }.take(11)

    /** Converte dígitos para formato de exibição. */
    fun digitsToDisplay(digits: String): String {
        val d = digits.filter { it.isDigit() }.take(11)
        return when {
            d.length <= 2  -> if (d.isEmpty()) "" else "(${d}"
            d.length <= 6  -> "(${d.take(2)}) ${d.drop(2)}"
            d.length <= 10 -> "(${d.take(2)}) ${d.drop(2).take(4)}-${d.drop(6)}"
            else           -> "(${d.take(2)}) ${d.drop(2).take(5)}-${d.drop(7)}"
        }
    }
}

/**
 * VisualTransformation para telefone brasileiro.
 * O campo armazena apenas dígitos; esta classe injeta a máscara para exibição.
 *
 * Mapeamento de offset:
 *   Fixo  (10 dígitos): (XX) XXXX-XXXX
 *     visual: 0='(' 1=d0 2=d1 3=')' 4=' ' 5=d2 6=d3 7=d4 8=d5 9='-' 10=d6..13=d9
 *   Celular (11 dígitos): (XX) XXXXX-XXXX
 *     visual: 0='(' 1=d0 2=d1 3=')' 4=' ' 5=d2..9=d6 10='-' 11=d7..14=d10
 */
class PhoneMaskTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        val out = buildString {
            digits.forEachIndexed { i, ch ->
                when {
                    i == 0 -> { append('('); append(ch) }
                    i == 1 -> { append(ch); append(')'); append(' ') }
                    i == 6 && digits.length <= 10 -> { append('-'); append(ch) }
                    i == 7 && digits.length == 11 -> { append('-'); append(ch) }
                    else -> append(ch)
                }
            }
        }

        val isCelular = digits.length == 11

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = when {
                offset == 0 -> 0
                offset == 1 -> 1
                offset <= 2 -> offset + 3  // após ')' e ' '
                isCelular -> when {
                    offset <= 7 -> offset + 3
                    else        -> offset + 4  // após '-'
                }
                else -> when {
                    offset <= 6 -> offset + 3
                    else        -> offset + 4
                }
            }.coerceAtMost(out.length)

            override fun transformedToOriginal(offset: Int): Int = when {
                offset <= 1 -> offset
                offset <= 4 -> 2
                isCelular -> when {
                    offset <= 10 -> offset - 3
                    offset <= 11 -> 7
                    else         -> offset - 4
                }
                else -> when {
                    offset <= 9  -> offset - 3
                    offset <= 10 -> 6
                    else         -> offset - 4
                }
            }.coerceIn(0, digits.length)
        }

        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}
