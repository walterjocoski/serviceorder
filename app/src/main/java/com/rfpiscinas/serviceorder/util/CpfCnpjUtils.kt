package com.rfpiscinas.serviceorder.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Utilitários para CPF e CNPJ com máscara e validação de dígitos verificadores.
 *
 * O estado armazena APENAS dígitos (máx 14).
 * CpfCnpjMaskTransformation exibe a máscara visualmente:
 *   ≤11 dígitos → CPF:  XXX.XXX.XXX-XX
 *   12–14 dígitos → CNPJ: XX.XXX.XXX/XXXX-XX
 */
object CpfCnpjUtils {

    fun filterDigits(input: String): String =
        input.filter { it.isDigit() }.take(14)

    /** True se os dígitos representam um CPF ou CNPJ matematicamente válido. */
    fun isValid(digits: String): Boolean {
        val d = digits.filter { it.isDigit() }
        return when (d.length) {
            11   -> isValidCpf(d)
            14   -> isValidCnpj(d)
            else -> false
        }
    }

    fun errorMessage(digits: String): String? {
        val d = digits.filter { it.isDigit() }
        return when {
            d.isEmpty()        -> null  // campo vazio — validação pelo isNotBlank do formulário
            d.length < 11      -> "CPF incompleto (${d.length}/11 dígitos)"
            d.length == 11     -> if (isValidCpf(d)) null else "CPF inválido"
            d.length < 14      -> "CNPJ incompleto (${d.length}/14 dígitos)"
            d.length == 14     -> if (isValidCnpj(d)) null else "CNPJ inválido"
            else               -> "Número inválido"
        }
    }

    // ── Validação CPF ─────────────────────────────────────────────────────
    private fun isValidCpf(cpf: String): Boolean {
        if (cpf.length != 11) return false
        if (cpf.all { it == cpf[0] }) return false  // ex: 000.000.000-00

        val d1 = (0..8).sumOf { (cpf[it] - '0') * (10 - it) }.let { sum ->
            val rem = sum % 11
            if (rem < 2) 0 else 11 - rem
        }
        val d2 = (0..9).sumOf { (cpf[it] - '0') * (11 - it) }.let { sum ->
            val rem = sum % 11
            if (rem < 2) 0 else 11 - rem
        }
        return d1 == (cpf[9] - '0') && d2 == (cpf[10] - '0')
    }

    // ── Validação CNPJ ────────────────────────────────────────────────────
    private fun isValidCnpj(cnpj: String): Boolean {
        if (cnpj.length != 14) return false
        if (cnpj.all { it == cnpj[0] }) return false

        val weights1 = intArrayOf(5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
        val weights2 = intArrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)

        fun calcDigit(digits: String, weights: IntArray): Int {
            val sum = weights.indices.sumOf { (digits[it] - '0') * weights[it] }
            val rem = sum % 11
            return if (rem < 2) 0 else 11 - rem
        }

        val d1 = calcDigit(cnpj, weights1)
        val d2 = calcDigit(cnpj, weights2)
        return d1 == (cnpj[12] - '0') && d2 == (cnpj[13] - '0')
    }
}

/**
 * VisualTransformation para CPF/CNPJ.
 * O campo armazena apenas dígitos; esta classe injeta a máscara visualmente.
 *
 * CPF  (11 dígitos): XXX.XXX.XXX-XX
 * CNPJ (14 dígitos): XX.XXX.XXX/XXXX-XX
 * Intermediário: aplica máscara CPF enquanto len < 12, CNPJ quando len >= 12.
 */
class CpfCnpjMaskTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        val isCnpj = digits.length > 11

        val out = if (isCnpj) formatCnpj(digits) else formatCpf(digits)

        val offsetMapping = if (isCnpj) cnpjOffsetMapping(digits, out)
                            else        cpfOffsetMapping(digits, out)

        return TransformedText(AnnotatedString(out), offsetMapping)
    }

    // CPF: XXX.XXX.XXX-XX
    // Pontos nas posições visuais 3 e 7, hífen na 11
    // Original → Transformado:
    //   0,1,2 → 0,1,2   | 3 → 4 (+1 ponto)   | 4,5 → 5,6
    //   6 → 8 (+2 pontos)| 7,8 → 9,10          | 9 → 12 (+2 pts +1 hífen)
    //   10 → 13
    private fun formatCpf(d: String): String = buildString {
        d.forEachIndexed { i, ch ->
            if (i == 3 || i == 6) append('.')
            if (i == 9) append('-')
            append(ch)
        }
    }

    private fun cpfOffsetMapping(digits: String, out: String) = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int = when {
            offset <= 3 -> offset
            offset <= 6 -> offset + 1
            offset <= 9 -> offset + 2
            else        -> offset + 3
        }.coerceAtMost(out.length)

        override fun transformedToOriginal(offset: Int): Int = when {
            offset <= 3 -> offset
            offset == 4 -> 3   // ponto
            offset <= 7 -> offset - 1
            offset == 8 -> 6   // ponto
            offset <= 11 -> offset - 2
            offset == 12 -> 9  // hífen
            else         -> offset - 3
        }.coerceIn(0, digits.length)
    }

    // CNPJ: XX.XXX.XXX/XXXX-XX
    // Ponto em 2, ponto em 6, barra em 10, hífen em 15
    private fun formatCnpj(d: String): String = buildString {
        d.forEachIndexed { i, ch ->
            if (i == 2 || i == 5) append('.')
            if (i == 8) append('/')
            if (i == 12) append('-')
            append(ch)
        }
    }

    private fun cnpjOffsetMapping(digits: String, out: String) = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int = when {
            offset <= 2  -> offset
            offset <= 5  -> offset + 1
            offset <= 8  -> offset + 2
            offset <= 12 -> offset + 3
            else         -> offset + 4
        }.coerceAtMost(out.length)

        override fun transformedToOriginal(offset: Int): Int = when {
            offset <= 2  -> offset
            offset == 3  -> 2    // ponto
            offset <= 6  -> offset - 1
            offset == 7  -> 5    // ponto
            offset <= 10 -> offset - 2
            offset == 11 -> 8    // barra
            offset <= 15 -> offset - 3
            offset == 16 -> 12   // hífen
            else         -> offset - 4
        }.coerceIn(0, digits.length)
    }
}
