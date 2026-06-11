package com.example.cleaningservices.infrastructure.adapters.outbound.pdf;

import com.example.cleaningservices.application.ports.outbound.ReceiptPdfPort;
import com.example.cleaningservices.domain.model.Emitter;
import com.example.cleaningservices.domain.model.Receipt;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class PdfGeneratorAdapter implements ReceiptPdfPort {

    public byte[] generate(Receipt receipt, Emitter emitter) {
        try {
            // valida se o emitente tem template configurado
            if (emitter.getTemplatePath() == null || emitter.getTemplatePath().isBlank()) {
                throw new RuntimeException("Emitente '" + emitter.getCompanyName()
                        + "' não possui template de recibo configurado. Configure o campo 'templatePath'.");
            }

            // carrega o template do Canva baseado no emitente
            InputStream templateStream = getClass().getResourceAsStream(
                    "/static/templates/" + emitter.getTemplatePath());

            if (templateStream == null) {
                throw new RuntimeException("Arquivo de template não encontrado: " + emitter.getTemplatePath()
                        + ". Verifique se o arquivo existe em src/main/resources/static/templates/");
            }

            // lê o conteúdo do template e fecha o stream imediatamente (evita vazamento de recurso)
            byte[] templateBytes = templateStream.readAllBytes();
            templateStream.close();

            PDDocument document = Loader.loadPDF(templateBytes);
            PDPage page = document.getPage(0);

            float pageHeight = page.getMediaBox().getHeight(); // 306pt

            PDPageContentStream content = new PDPageContentStream(
                    document, page, PDPageContentStream.AppendMode.APPEND, true, true);

            PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            // ─── Nº do recibo ─────────────────────────────────────────
            writeText(content, font, 9, String.valueOf(receipt.getReceiptNumber()),
                    255, pageHeight - 30);

            // ─── Valor R$ (formato brasileiro: 80,00) ─────────────────
            String valorFormatado = receipt.getValue()
                    .setScale(2, java.math.RoundingMode.HALF_UP)
                    .toPlainString()
                    .replace(".", ",");
            writeText(content, font, 9, valorFormatado,
                    276, pageHeight - 59);

            // ─── Recebemos de ─────────────────────────────────────────
            writeText(content, font, 9, receipt.getClientName(),
                    92, pageHeight - 97);

            // ─── Valor por extenso ────────────────────────────────────
            String extenso = NumberToWordsConverter.convert(receipt.getValue());
            writeText(content, font, 9, extenso,
                    92, pageHeight - 134);

            // ─── Emitente (após o label "Emitente :") ─────────────────
            writeText(content, font, 8, emitter.getCompanyName(),
                    71, pageHeight - 243);

            // ─── Assinatura (após o label "Assinatura :") ─────────────
            writeText(content, font, 8, emitter.getSignerName(),
                    71, pageHeight - 259);

            // ─── CPF/CNPJ (após o label "CPF/CNPJ:") ─────────────────
            writeText(content, font, 8, emitter.getCpfCnpj(),
                    71, pageHeight - 273);

            // ─── Data: dia, mês, ano na linha "São Paulo , _ de _ de _"
            DateTimeFormatter dayFmt   = DateTimeFormatter.ofPattern("dd");
            DateTimeFormatter monthFmt = DateTimeFormatter.ofPattern("MMMM", Locale.forLanguageTag("pt-BR"));
            DateTimeFormatter yearFmt  = DateTimeFormatter.ofPattern("yyyy");

            writeText(content, font, 8, receipt.getDate().format(dayFmt),
                    195, pageHeight - 290);
            writeText(content, font, 8, receipt.getDate().format(monthFmt),
                    265, pageHeight - 290);
            writeText(content, font, 8, receipt.getDate().format(yearFmt),
                    343, pageHeight - 290);

            content.close();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF receipt from template", e);
        }
    }


    // escreve texto no PDF nas coordenadas x, y
    private void writeText(PDPageContentStream content, PDType1Font font,
                           float fontSize, String text, float x, float y) throws Exception {
        content.beginText();
        content.setFont(font, fontSize);
        content.newLineAtOffset(x, y);
        content.showText(text != null ? text : "");
        content.endText();
    }
}
