package com.rfpiscinas.serviceorder.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.rfpiscinas.serviceorder.data.model.ServiceOrder
import java.io.File
import java.io.FileOutputStream

object PdfReportGenerator {

    // ── Dimensões A4 em pontos (72 dpi) ─────────────────────────────────
    private const val W = 595
    private const val H = 842
    private const val MARGIN = 40f
    private const val CONTENT_W = W - MARGIN * 2

    // ── Alturas de linha ─────────────────────────────────────────────────
    private const val LH_SM = 16f
    private const val LH_MD = 20f
    private const val LH_LG = 24f

    // ── Cores ────────────────────────────────────────────────────────────
    private val C_PRIMARY   = Color.rgb(0, 90, 160)      // azul profundo
    private val C_SECONDARY = Color.rgb(0, 150, 136)     // verde-água
    private val C_DARK      = Color.rgb(30, 30, 30)
    private val C_MID       = Color.rgb(80, 80, 80)
    private val C_LIGHT     = Color.rgb(140, 140, 140)
    private val C_RULE      = Color.rgb(210, 210, 210)
    private val C_HEADER_BG = Color.rgb(0, 90, 160)
    private val C_SECT_BG   = Color.rgb(240, 247, 255)
    private val C_WHITE     = Color.WHITE

    // ── Pincéis ──────────────────────────────────────────────────────────
    private fun paint(size: Float, color: Int, bold: Boolean = false) = Paint().apply {
        textSize = size; this.color = color
        typeface = if (bold) Typeface.create(Typeface.DEFAULT, Typeface.BOLD) else Typeface.DEFAULT
        isAntiAlias = true
    }

    private val pTitle   = paint(22f, C_WHITE,     bold = true)
    private val pSubtit  = paint(13f, C_WHITE,     bold = false)
    private val pOsNum   = paint(18f, C_PRIMARY,   bold = true)
    private val pSectHdr = paint(10f, C_WHITE,     bold = true)
    private val pLabel   = paint(9f,  C_LIGHT,     bold = false)
    private val pValue   = paint(11f, C_DARK,      bold = false)
    private val pValueB  = paint(11f, C_DARK,      bold = true)
    private val pSvc     = paint(11f, C_DARK,      bold = true)
    private val pSvcType = paint(9f,  C_MID,       bold = false)
    private val pProd    = paint(10f, C_MID,       bold = false)
    private val pFooter  = paint(8f,  C_LIGHT,     bold = false)
    private val pPage    = paint(8f,  C_LIGHT,     bold = false)

    private fun rectPaint(color: Int) = Paint().apply {
        this.color = color; style = Paint.Style.FILL; isAntiAlias = true
    }
    private fun strokePaint(color: Int, w: Float = 1f) = Paint().apply {
        this.color = color; style = Paint.Style.STROKE; strokeWidth = w; isAntiAlias = true
    }

    // ════════════════════════════════════════════════════════════════════
    // ENTRY POINT: gera um PDF com uma OS por página
    // ════════════════════════════════════════════════════════════════════
    fun generateMultiOrderReport(context: Context, orders: List<ServiceOrder>): Uri {
        val document = PdfDocument()
        orders.forEachIndexed { index, order ->
            val pageInfo = PdfDocument.PageInfo.Builder(W, H, index + 1).create()
            val page = document.startPage(pageInfo)
            drawOrderPage(page.canvas, order, index + 1, orders.size)
            document.finishPage(page)
        }
        val timestamp = System.currentTimeMillis()
        return savePdf(context, document, "Relatorio_$timestamp")
    }

    // Mantém o método antigo para compatibilidade com a tela de OS individual
    fun generateOrderReport(context: Context, order: ServiceOrder): Uri =
        generateMultiOrderReport(context, listOf(order))

    // ════════════════════════════════════════════════════════════════════
    // DESENHO DE UMA PÁGINA
    // ════════════════════════════════════════════════════════════════════
    private fun drawOrderPage(canvas: Canvas, order: ServiceOrder, pageNum: Int, totalPages: Int) {
        var y = 0f

        // ── Cabeçalho azul ──────────────────────────────────────────────
        val headerH = 90f
        canvas.drawRect(0f, 0f, W.toFloat(), headerH, rectPaint(C_HEADER_BG))

        // Logo / empresa
        canvas.drawText("RF PISCINAS", MARGIN, 42f, pTitle)
        canvas.drawText("Limpeza e Manutenção de Piscinas", MARGIN, 60f, pSubtit)

        // Número da OS alinhado à direita
        val osLabel = "OS #${order.id}"
        val osLabelW = pOsNum.measureText(osLabel)
        // Fundo branco arredondado para o número da OS
        val osBox = RectF(W - MARGIN - osLabelW - 16f, 28f, W - MARGIN + 8f, 58f)
        canvas.drawRoundRect(osBox, 6f, 6f, rectPaint(C_WHITE))
        canvas.drawText(osLabel, W - MARGIN - osLabelW - 8f, 48f, pOsNum)

        // Status badge
        val statusLabel = order.status.displayName.uppercase()
        val statusW = pSectHdr.measureText(statusLabel)
        val statusBox = RectF(W - MARGIN - statusW - 16f, 62f, W - MARGIN + 8f, 82f)
        val statusColor = when (order.status.name) {
            "COMPLETED" -> Color.rgb(46, 125, 50)
            "IN_PROGRESS" -> Color.rgb(245, 124, 0)
            "CANCELLED" -> Color.rgb(183, 28, 28)
            else -> Color.rgb(100, 100, 100)
        }
        canvas.drawRoundRect(statusBox, 4f, 4f, rectPaint(statusColor))
        canvas.drawText(statusLabel, W - MARGIN - statusW - 8f, 76f, pSectHdr)

        y = headerH + 18f

        // ── Seção: Dados do Cliente ──────────────────────────────────────
        y = drawSectionHeader(canvas, y, "DADOS DO CLIENTE")
        y += 4f
        canvas.drawRect(MARGIN, y, W - MARGIN, y + 56f, rectPaint(C_SECT_BG))
        canvas.drawRect(MARGIN, y, W - MARGIN, y + 56f, strokePaint(C_RULE))
        y += 14f
        canvas.drawText("Cliente", MARGIN + 10f, y, pLabel)
        canvas.drawText("Endereço", MARGIN + CONTENT_W / 2, y, pLabel)
        y += LH_SM - 2f
        canvas.drawText(order.clientName, MARGIN + 10f, y, pValueB)
        // Endereço pode ser longo — trunca se necessário
        val addr = truncate(order.clientAddress, 52)
        canvas.drawText(addr, MARGIN + CONTENT_W / 2, y, pValue)
        y += LH_SM
        canvas.drawText("Prestador", MARGIN + 10f, y, pLabel)
        y += LH_SM - 2f
        canvas.drawText(order.employeeName, MARGIN + 10f, y, pValue)
        y += 14f

        // ── Seção: Período ───────────────────────────────────────────────
        y = drawSectionHeader(canvas, y, "PERÍODO DE EXECUÇÃO")
        y += 4f
        canvas.drawRect(MARGIN, y, W - MARGIN, y + 42f, rectPaint(C_SECT_BG))
        canvas.drawRect(MARGIN, y, W - MARGIN, y + 42f, strokePaint(C_RULE))
        y += 14f
        canvas.drawText("Início", MARGIN + 10f, y, pLabel)
        val halfW = CONTENT_W / 2
        canvas.drawText("Finalização", MARGIN + halfW, y, pLabel)
        y += LH_SM - 2f
        canvas.drawText(DateUtils.display(order.startDateTime), MARGIN + 10f, y, pValue)
        canvas.drawText(
            if (order.endDateTime != null) DateUtils.display(order.endDateTime) else "Em andamento",
            MARGIN + halfW, y, pValue
        )
        y += 14f

        // ── Seção: Serviços Executados ───────────────────────────────────
        y = drawSectionHeader(canvas, y, "SERVIÇOS EXECUTADOS")
        y += 8f

        for ((idx, item) in order.items.withIndex()) {
            // Linha alternada de fundo
            val rowH = if (item.products.isEmpty()) 36f else 36f + item.products.size * LH_SM
            val rowBg = if (idx % 2 == 0) C_SECT_BG else C_WHITE
            canvas.drawRect(MARGIN, y - 4f, W - MARGIN, y + rowH - 8f, rectPaint(rowBg))

            // Número do serviço
            val numLabel = "${idx + 1}"
            canvas.drawCircle(MARGIN + 14f, y + 6f, 11f, rectPaint(C_PRIMARY))
            val numW = pSectHdr.measureText(numLabel)
            canvas.drawText(numLabel, MARGIN + 14f - numW / 2, y + 10f, pSectHdr)

            // Nome e tipo
            canvas.drawText(item.service.name, MARGIN + 32f, y + 10f, pSvc)
            canvas.drawText(item.service.type, MARGIN + 32f, y + 22f, pSvcType)

            // Produtos (sem observações)
            if (item.products.isNotEmpty()) {
                var py = y + 32f
                val prodLabel = "Produtos: " + item.products.joinToString("  ·  ") {
                    "${it.product.name}: ${it.quantity} ${it.product.unitMeasure.abbreviation}"
                }
                canvas.drawText(truncate(prodLabel, 85), MARGIN + 32f, py, pProd)
                y += item.products.size * LH_SM
            }

            y += 36f

            // Linha separadora leve entre serviços
            if (idx < order.items.size - 1) {
                canvas.drawLine(MARGIN + 20f, y - 6f, W - MARGIN - 20f, y - 6f, strokePaint(C_RULE))
            }
        }

        // ── Rodapé ───────────────────────────────────────────────────────
        val footerY = H - 30f
        canvas.drawLine(MARGIN, footerY - 14f, W - MARGIN, footerY - 14f, strokePaint(C_RULE))
        canvas.drawText("RF Piscinas — Sistema de Ordem de Serviço", MARGIN, footerY, pFooter)
        val pageLabel = "Página $pageNum / $totalPages"
        val pageLabelW = pPage.measureText(pageLabel)
        canvas.drawText(pageLabel, W - MARGIN - pageLabelW, footerY, pPage)
    }

    // ── Cabeçalho de seção ───────────────────────────────────────────────
    private fun drawSectionHeader(canvas: Canvas, y: Float, title: String): Float {
        canvas.drawRect(MARGIN, y, W - MARGIN, y + 20f, rectPaint(C_SECONDARY))
        canvas.drawText(title, MARGIN + 8f, y + 14f, pSectHdr)
        return y + 20f
    }

    // ── Trunca string longa com reticências ──────────────────────────────
    private fun truncate(text: String, maxChars: Int): String =
        if (text.length <= maxChars) text else text.substring(0, maxChars - 1) + "…"

    // ── Salva o PDF e retorna URI via FileProvider ────────────────────────
    private fun savePdf(context: Context, document: PdfDocument, fileName: String): Uri {
        val dir = File(context.cacheDir, "reports").also { if (!it.exists()) it.mkdirs() }
        val file = File(dir, "$fileName.pdf")
        FileOutputStream(file).use { document.writeTo(it) }
        document.close()
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }
}
