package com.rfpiscinas.serviceorder.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.rfpiscinas.serviceorder.data.model.ServiceOrder
import java.io.File
import java.io.FileOutputStream

object PdfReportGenerator {

    private const val PAGE_WIDTH = 595 // A4
    private const val PAGE_HEIGHT = 842 // A4
    private const val MARGIN = 40f
    private const val LINE_HEIGHT = 18f

    private val titlePaint = Paint().apply {
        textSize = 20f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        color = android.graphics.Color.rgb(0, 119, 190) // Azul RF Piscinas
    }

    private val subtitlePaint = Paint().apply {
        textSize = 14f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        color = android.graphics.Color.rgb(0, 107, 125) // Azul petróleo
    }

    private val headerPaint = Paint().apply {
        textSize = 12f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        color = android.graphics.Color.BLACK
    }

    private val textPaint = Paint().apply {
        textSize = 11f
        typeface = Typeface.DEFAULT
        color = android.graphics.Color.rgb(50, 50, 50)
    }

    private val smallPaint = Paint().apply {
        textSize = 10f
        typeface = Typeface.DEFAULT
        color = android.graphics.Color.rgb(100, 100, 100)
    }

    private val linePaint = Paint().apply {
        color = android.graphics.Color.rgb(200, 200, 200)
        strokeWidth = 1f
    }

    fun generateOrderReport(context: Context, order: ServiceOrder): Uri {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        var y = MARGIN + 20f

        // Header
        y = drawHeader(canvas, y)

        // Separator
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, linePaint)
        y += 15f

        // Order Info
        canvas.drawText("ORDEM DE SERVIÇO #${order.id}", MARGIN, y, subtitlePaint)
        y += 25f

        canvas.drawText("Status: ${order.status.displayName}", MARGIN, y, headerPaint)
        y += LINE_HEIGHT

        canvas.drawText("Prestador: ${order.employeeName}", MARGIN, y, textPaint)
        y += LINE_HEIGHT + 5f

        // Client section
        canvas.drawText("DADOS DO CLIENTE", MARGIN, y, subtitlePaint)
        y += LINE_HEIGHT + 2f
        canvas.drawText("Nome: ${order.clientName}", MARGIN + 10f, y, textPaint)
        y += LINE_HEIGHT
        canvas.drawText("Endereço: ${order.clientAddress}", MARGIN + 10f, y, textPaint)
        y += LINE_HEIGHT + 5f

        // Dates
        canvas.drawText("PERÍODO", MARGIN, y, subtitlePaint)
        y += LINE_HEIGHT + 2f
        canvas.drawText("Início: ${order.startDateTime}", MARGIN + 10f, y, textPaint)
        y += LINE_HEIGHT
        if (order.endDateTime != null) {
            canvas.drawText("Finalização: ${order.endDateTime}", MARGIN + 10f, y, textPaint)
            y += LINE_HEIGHT
        }
        y += 10f

        // Separator
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, linePaint)
        y += 15f

        // Services
        canvas.drawText("SERVIÇOS EXECUTADOS", MARGIN, y, subtitlePaint)
        y += LINE_HEIGHT + 5f

        for ((index, item) in order.items.withIndex()) {
            canvas.drawText("${index + 1}. ${item.service.name} (${item.service.type})", MARGIN + 10f, y, headerPaint)
            y += LINE_HEIGHT

            if (item.observations.isNotBlank()) {
                canvas.drawText("   Observações: ${item.observations}", MARGIN + 10f, y, smallPaint)
                y += LINE_HEIGHT
            }

            if (item.products.isNotEmpty()) {
                canvas.drawText("   Produtos utilizados:", MARGIN + 10f, y, smallPaint)
                y += LINE_HEIGHT
                for (product in item.products) {
                    val unitAbbrev = product.product.unitMeasure.abbreviation
                    canvas.drawText(
                        "     • ${product.product.name}: ${product.quantity} $unitAbbrev",
                        MARGIN + 10f, y, textPaint
                    )
                    y += LINE_HEIGHT
                }
            }
            y += 5f

            // Check if we need a new page
            if (y > PAGE_HEIGHT - 60f) {
                document.finishPage(page)
                val newPageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 2).create()
                val newPage = document.startPage(newPageInfo)
                y = MARGIN + 20f
            }
        }

        // Footer
        y = PAGE_HEIGHT - 60f
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, linePaint)
        y += 15f
        canvas.drawText("RF Piscinas - Sistema de Ordem de Serviço", MARGIN, y, smallPaint)
        y += LINE_HEIGHT
        canvas.drawText("Relatório gerado automaticamente", MARGIN, y, smallPaint)

        document.finishPage(page)

        return savePdfAndGetUri(context, document, "OS_${order.id}")
    }

    fun generatePeriodReport(
        context: Context,
        orders: List<ServiceOrder>,
        startDate: String,
        endDate: String
    ): Uri {
        val document = PdfDocument()
        var pageNumber = 1
        var pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
        var page = document.startPage(pageInfo)
        var canvas = page.canvas
        var y = MARGIN + 20f

        // Header
        y = drawHeader(canvas, y)

        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, linePaint)
        y += 15f

        canvas.drawText("RELATÓRIO POR PERÍODO", MARGIN, y, subtitlePaint)
        y += LINE_HEIGHT + 2f
        canvas.drawText("De: $startDate  até  $endDate", MARGIN + 10f, y, textPaint)
        y += LINE_HEIGHT
        canvas.drawText("Total de ordens: ${orders.size}", MARGIN + 10f, y, headerPaint)
        y += LINE_HEIGHT + 10f

        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, linePaint)
        y += 15f

        for ((index, order) in orders.withIndex()) {
            // Check if we need a new page
            if (y > PAGE_HEIGHT - 120f) {
                document.finishPage(page)
                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
                page = document.startPage(pageInfo)
                canvas = page.canvas
                y = MARGIN + 20f
            }

            canvas.drawText("OS #${order.id} - ${order.clientName}", MARGIN, y, headerPaint)
            y += LINE_HEIGHT
            canvas.drawText("Prestador: ${order.employeeName}  |  Status: ${order.status.displayName}", MARGIN + 10f, y, textPaint)
            y += LINE_HEIGHT
            canvas.drawText("Início: ${order.startDateTime}", MARGIN + 10f, y, smallPaint)
            if (order.endDateTime != null) {
                canvas.drawText("Fim: ${order.endDateTime}", MARGIN + 250f, y, smallPaint)
            }
            y += LINE_HEIGHT
            canvas.drawText("Serviços: ${order.items.joinToString(", ") { it.service.name }}", MARGIN + 10f, y, smallPaint)
            y += LINE_HEIGHT + 8f

            canvas.drawLine(MARGIN + 20f, y, PAGE_WIDTH - MARGIN - 20f, y, linePaint)
            y += 10f
        }

        // Footer
        if (y > PAGE_HEIGHT - 60f) {
            document.finishPage(page)
            pageNumber++
            pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
            page = document.startPage(pageInfo)
            canvas = page.canvas
            y = MARGIN + 20f
        }
        y = maxOf(y, PAGE_HEIGHT - 60f)
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, linePaint)
        y += 15f
        canvas.drawText("RF Piscinas - Sistema de Ordem de Serviço", MARGIN, y, smallPaint)

        document.finishPage(page)

        return savePdfAndGetUri(context, document, "Relatorio_${startDate}_${endDate}")
    }

    private fun drawHeader(canvas: Canvas, startY: Float): Float {
        var y = startY
        canvas.drawText("RF PISCINAS", MARGIN, y, titlePaint)
        y += 22f
        canvas.drawText("Limpeza e Manutenção de Piscinas", MARGIN, y, smallPaint)
        y += LINE_HEIGHT + 10f
        return y
    }

    private fun savePdfAndGetUri(context: Context, document: PdfDocument, fileName: String): Uri {
        val pdfDir = File(context.cacheDir, "reports")
        if (!pdfDir.exists()) pdfDir.mkdirs()

        val file = File(pdfDir, "$fileName.pdf")
        FileOutputStream(file).use { out ->
            document.writeTo(out)
        }
        document.close()

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }
}
