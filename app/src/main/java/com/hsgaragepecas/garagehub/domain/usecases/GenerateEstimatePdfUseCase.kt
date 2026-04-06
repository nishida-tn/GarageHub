package com.hsgaragepecas.garagehub.domain.usecases

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.hsgaragepecas.garagehub.data.model.EstimateFullDto
import com.hsgaragepecas.garagehub.data.model.EstimateItemDto
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

/**
 * Use case for generating a PDF file for an estimate.
 *
 * @property context The application context.
 */
class GenerateEstimatePdfUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    /**
     * Executes the PDF generation.
     *
     * @param estimate The estimate data.
     * @param items The list of items in the estimate.
     * @return The URI of the generated PDF file.
     */
    operator fun invoke(estimate: EstimateFullDto, items: List<EstimateItemDto>): Uri {
        val fileName = "orcamento_${estimate.id}.pdf"
        val file = File(context.cacheDir, fileName)
        val document = Document(PageSize.A4)

        try {
            PdfWriter.getInstance(document, FileOutputStream(file))
            document.open()

            // Header
            val titleFont = Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD)
            val header = Paragraph("Orçamento #${estimate.id}", titleFont)
            header.alignment = Element.ALIGN_CENTER
            document.add(header)
            document.add(Paragraph(" "))

            // Client Info
            val subTitleFont = Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD)
            document.add(Paragraph("Dados do Cliente", subTitleFont))
            document.add(Paragraph("Nome: ${estimate.clientName ?: "N/A"}"))
            document.add(Paragraph("Telefone: ${estimate.clientTel ?: "N/A"}"))
            document.add(Paragraph("Endereço: ${estimate.clientAddress ?: ""}, ${estimate.clientNumber ?: ""}"))
            document.add(Paragraph("Cidade: ${estimate.clientCity ?: ""} - ${estimate.clientUf ?: ""}"))
            document.add(Paragraph(" "))

            // Vehicle Info
            document.add(Paragraph("Dados do Veículo", subTitleFont))
            document.add(Paragraph("Placa: ${estimate.vehiclePlate ?: "N/A"}"))
            document.add(Paragraph("Modelo: ${estimate.vehicleBrand ?: ""} ${estimate.vehicleModel ?: ""}"))
            document.add(Paragraph("Ano: ${estimate.vehicleYearFab ?: ""}/${estimate.vehicleYearMod ?: ""}"))
            document.add(Paragraph(" "))

            // Items Table
            document.add(Paragraph("Itens e Serviços", subTitleFont))
            document.add(Paragraph(" "))
            val table = PdfPTable(4)
            table.widthPercentage = 100f
            table.setWidths(floatArrayOf(4f, 1f, 2f, 2f))

            val cellFont = Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD)
            table.addCell(PdfPCell(Phrase("Descrição", cellFont)))
            table.addCell(PdfPCell(Phrase("Qtd", cellFont)))
            table.addCell(PdfPCell(Phrase("Unitário", cellFont)))
            table.addCell(PdfPCell(Phrase("Total", cellFont)))

            var totalParts = 0.0
            var totalServices = 0.0

            items.forEach { item ->
                table.addCell(item.partName ?: "")
                table.addCell(item.quantity.toString())
                table.addCell(currencyFormat.format(item.unitPrice ?: 0.0))
                table.addCell(currencyFormat.format(item.totalValue ?: 0.0))

                val itemPartTotal = (item.unitPrice ?: 0.0) * item.quantity
                val itemServiceTotal = (item.valueT ?: 0.0) + (item.valueRi ?: 0.0) + (item.valueR ?: 0.0) + (item.valueP ?: 0.0)

                totalParts += itemPartTotal
                totalServices += itemServiceTotal
            }
            document.add(table)
            document.add(Paragraph(" "))

            // Summary
            val summaryFont = Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD)
            document.add(Paragraph("Resumo", summaryFont))
            document.add(Paragraph("Total Peças: ${currencyFormat.format(totalParts)}"))
            document.add(Paragraph("Total Serviços: ${currencyFormat.format(totalServices)}"))
            document.add(Paragraph("Valor Total: ${currencyFormat.format(totalParts + totalServices)}", summaryFont))

            document.close()
        } catch (e: Exception) {
            e.printStackTrace()
            if (document.isOpen) document.close()
        }

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}
