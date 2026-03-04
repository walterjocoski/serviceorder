package com.rfpiscinas.serviceorder.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Utilitário centralizado de datas.
 *
 * Formato de exibição e armazenamento: DD/MM/YYYY HH:mm:ss
 * Formato de data simples (startDate de funcionário): DD/MM/YYYY
 *
 * Comparação de strings no filtro de período:
 *   Como DD/MM/YYYY não é ordenável lexicograficamente, convertemos para
 *   yyyy-MM-dd antes de comparar (apenas internamente nos filtros).
 */
object DateUtils {

    val DATETIME_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

    val DATE_ONLY_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // Formato interno usado apenas para comparação de range nos filtros
    private val SORT_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    /** Retorna data/hora atual no formato padrão do sistema: DD/MM/YYYY HH:mm:ss */
    fun now(): String = LocalDateTime.now().format(DATETIME_FORMATTER)

    /** Retorna data atual no formato DD/MM/YYYY */
    fun today(): String = LocalDate.now().format(DATE_ONLY_FORMATTER)

    /**
     * Converte uma string DD/MM/YYYY HH:mm:ss para formato sortável yyyy-MM-dd HH:mm:ss.
     * Retorna null se a string for inválida.
     */
    fun toSortable(datetime: String?): String? {
        if (datetime.isNullOrBlank()) return null
        return try {
            val dt = LocalDateTime.parse(datetime, DATETIME_FORMATTER)
            dt.format(SORT_FORMATTER)
        } catch (e: DateTimeParseException) {
            // Tenta apenas data DD/MM/YYYY
            try {
                val d = LocalDate.parse(datetime, DATE_ONLY_FORMATTER)
                d.atStartOfDay().format(SORT_FORMATTER)
            } catch (e2: DateTimeParseException) {
                null
            }
        }
    }

    /**
     * Compara duas datas no formato DD/MM/YYYY HH:mm:ss.
     * Retorna true se [a] >= [b].
     */
    fun isAfterOrEqual(a: String, b: String): Boolean {
        val sa = toSortable(a) ?: return false
        val sb = toSortable(b) ?: return false
        return sa >= sb
    }

    /**
     * Compara duas datas no formato DD/MM/YYYY HH:mm:ss.
     * Retorna true se [a] <= [b].
     */
    fun isBeforeOrEqual(a: String, b: String): Boolean {
        val sa = toSortable(a) ?: return false
        val sb = toSortable(b) ?: return false
        return sa <= sb
    }

    /**
     * Normaliza uma data digitada (DD/MM/YYYY) para início do dia: DD/MM/YYYY 00:00:00
     */
    fun startOfDay(date: String): String {
        return try {
            val d = LocalDate.parse(date.trim(), DATE_ONLY_FORMATTER)
            d.atStartOfDay().format(DATETIME_FORMATTER)
        } catch (e: DateTimeParseException) {
            date
        }
    }

    /**
     * Normaliza uma data digitada (DD/MM/YYYY) para fim do dia: DD/MM/YYYY 23:59:59
     */
    fun endOfDay(date: String): String {
        return try {
            val d = LocalDate.parse(date.trim(), DATE_ONLY_FORMATTER)
            d.atTime(23, 59, 59).format(DATETIME_FORMATTER)
        } catch (e: DateTimeParseException) {
            date
        }
    }

    /**
     * Formata uma string DD/MM/YYYY HH:mm:ss para exibição amigável: DD/MM/YYYY às HH:mm
     */
    fun display(datetime: String?): String {
        if (datetime.isNullOrBlank()) return "—"
        return try {
            val dt = LocalDateTime.parse(datetime, DATETIME_FORMATTER)
            "${dt.format(DATE_ONLY_FORMATTER)} às ${dt.hour.toString().padStart(2,'0')}:${dt.minute.toString().padStart(2,'0')}"
        } catch (e: DateTimeParseException) {
            datetime
        }
    }

    // ── Funções para máscara de data (uso com DateMaskTransformation) ─────────

    /**
     * Filtra a entrada de um campo de data: mantém apenas dígitos, limita a 8.
     * O estado do campo deve armazenar SOMENTE dígitos.
     * A exibição com barras é feita por DateMaskTransformation.
     *
     * Uso: onValueChange = { field = DateUtils.filterDateDigits(it) }
     */
    fun filterDateDigits(input: String): String =
        input.filter { it.isDigit() }.take(8)

    /**
     * Converte dígitos puros para string DD/MM/YYYY (ou parcial).
     * Ex: "30011993" → "30/01/1993"  |  "3001" → "30/01"
     */
    fun digitsToDisplay(digits: String): String {
        val d = digits.filter { it.isDigit() }.take(8)
        return buildString {
            d.forEachIndexed { i, ch ->
                if (i == 2 || i == 4) append('/')
                append(ch)
            }
        }
    }

    /**
     * Converte uma string DD/MM/YYYY (display) de volta para 8 dígitos puros.
     * Ex: "30/01/1993" → "30011993"  |  "" → ""
     */
    fun displayToDigits(display: String): String =
        display.filter { it.isDigit() }.take(8)

}