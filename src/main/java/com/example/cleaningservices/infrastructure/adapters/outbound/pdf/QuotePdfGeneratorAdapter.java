package com.example.cleaningservices.infrastructure.adapters.outbound.pdf;

import com.example.cleaningservices.application.ports.outbound.QuotePdfPort;
import com.example.cleaningservices.domain.model.Emitter;
import com.example.cleaningservices.domain.model.Quote;
import com.example.cleaningservices.domain.model.QuoteItem;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Gera o PDF do orçamento do zero (sem template)
// Necessário porque o número de itens é dinâmico — não dá para usar template fixo
@Component
public class QuotePdfGeneratorAdapter implements QuotePdfPort {

    // cores do layout
    private static final Color COR_CABECALHO    = new Color(30,  30,  30);   // quase preto — título empresa
    private static final Color COR_TEXTO        = new Color(60,  60,  60);   // cinza escuro — texto geral
    private static final Color COR_SUBTEXTO     = new Color(100, 100, 100);  // cinza médio — info secundária
    private static final Color COR_TABELA_HEAD  = new Color(230, 230, 230);  // cinza claro — fundo do cabeçalho da tabela
    private static final Color COR_LINHA_PAR    = new Color(248, 248, 248);  // quase branco — linha par da tabela
    private static final Color COR_BORDA        = new Color(200, 200, 200);  // borda suave
    private static final Color COR_DESTAQUE     = new Color(20,  80,  160);  // azul — valor final

    // margens e dimensões
    private static final float MARGEM           = 40f;
    private static final float LARGURA_PAGINA   = PDRectangle.A4.getWidth();   // 595pt
    private static final float ALTURA_PAGINA    = PDRectangle.A4.getHeight();  // 842pt
    private static final float AREA_UTIL        = LARGURA_PAGINA - 2 * MARGEM; // 515pt

    // larguras das colunas da tabela (total = AREA_UTIL)
    private static final float COL_DESCRICAO    = 255f;
    private static final float COL_VALOR        = 90f;
    private static final float COL_QTD          = 70f;
    private static final float COL_TOTAL        = 100f;
    private static final float ALTURA_LINHA_TAB = 20f;

    public byte[] generate(Quote quote, Emitter emitter) {
        try (PDDocument doc = new PDDocument()) {

            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            PDType1Font fontRegular  = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDType1Font fontBold     = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            PDPageContentStream cs = new PDPageContentStream(doc, page);

            float y = ALTURA_PAGINA - MARGEM;

            // CABEÇALHO
            y = desenharCabecalho(cs, fontBold, fontRegular, emitter, quote, y, doc);

            // linha separadora
            y -= 8;
            desenharLinha(cs, MARGEM, LARGURA_PAGINA - MARGEM, y, COR_BORDA);
            y -= 14;

            // ══════════════════════════════════════════════════
            // INFO DO ORÇAMENTO — número, data, cliente, mensagem
            // ══════════════════════════════════════════════════
            y = desenharInfoOrcamento(cs, fontBold, fontRegular, quote, y);
            y -= 14;

            // ══════════════════════════════════════════════════
            // TABELA DE ITENS
            // ══════════════════════════════════════════════════
            y = desenharTabela(cs, fontBold, fontRegular, quote, y);
            y -= 16;

            // ══════════════════════════════════════════════════
            // RODAPÉ — condições + formas de pagamento + totais
            // ══════════════════════════════════════════════════
            desenharRodape(cs, fontBold, fontRegular, quote, y);

            cs.close();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.save(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF do orçamento", e);
        }
    }

    // ── CABEÇALHO ────────────────────────────────────────────────────────────
    private float desenharCabecalho(PDPageContentStream cs, PDType1Font fontBold,
                                    PDType1Font fontRegular, Emitter emitter,
                                    Quote quote, float y, PDDocument doc) throws Exception {

        float xTexto = MARGEM;

        // tenta carregar e desenhar a logo se existir
        if (emitter.getLogoPath() != null && !emitter.getLogoPath().isBlank()) {
            try (InputStream logoStream = getClass().getResourceAsStream("/static/logos/" + emitter.getLogoPath())) {
                if (logoStream != null) {
                    PDImageXObject logo = PDImageXObject.createFromByteArray(doc, logoStream.readAllBytes(), emitter.getLogoPath());
                    float logoAltura = 40f;
                    float logoLargura = logoAltura * ((float) logo.getWidth() / logo.getHeight());
                    cs.drawImage(logo, MARGEM, y - logoAltura, logoLargura, logoAltura);
                    xTexto = MARGEM + logoLargura + 10; // texto começa após a logo
                }
            } catch (Exception e) {
                // se não conseguir carregar a logo, continua sem ela
                System.out.println("[Quote PDF] Logo não encontrada: " + emitter.getLogoPath());
            }
        }

        // nome da empresa (grande e negrito)
        escrever(cs, fontBold, 15, COR_CABECALHO, emitter.getCompanyName().toUpperCase(), xTexto, y);

        // número do orçamento no canto direito
        String nroLabel = "N\u00BA " + quote.getQuoteNumber();
        float nroX = LARGURA_PAGINA - MARGEM - calcularLarguraTexto(fontBold, 14, nroLabel);
        escrever(cs, fontBold, 14, COR_CABECALHO, nroLabel, nroX, y);

        y -= 18;

        // telefone e e-mail na mesma linha
        String contato = "";
        if (emitter.getPhone() != null && !emitter.getPhone().isBlank()) contato += emitter.getPhone();
        if (emitter.getEmail() != null && !emitter.getEmail().isBlank()) {
            if (!contato.isBlank()) contato += " - ";
            contato += emitter.getEmail();
        }
        if (!contato.isBlank()) {
            escrever(cs, fontRegular, 9, COR_SUBTEXTO, contato, xTexto, y);
            y -= 13;
        }

        // endereço
        if (emitter.getAddress() != null && !emitter.getAddress().isBlank()) {
            escrever(cs, fontRegular, 9, COR_SUBTEXTO, emitter.getAddress(), xTexto, y);
            y -= 13;
        }

        return y;
    }

    // ── INFO DO ORÇAMENTO ─────────────────────────────────────────────────────
    private float desenharInfoOrcamento(PDPageContentStream cs, PDType1Font fontBold,
                                        PDType1Font fontRegular, Quote quote, float y) throws Exception {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale.forLanguageTag("pt-BR"));

        escreverPar(cs, fontBold, fontRegular, 9, COR_TEXTO, "Orçamento nº: ", String.valueOf(quote.getQuoteNumber()), MARGEM, y);
        y -= 13;
        escreverPar(cs, fontBold, fontRegular, 9, COR_TEXTO, "Data: ", quote.getDate().format(fmt), MARGEM, y);
        y -= 13;
        escreverPar(cs, fontBold, fontRegular, 9, COR_TEXTO, "Cliente: ", quote.getClientName(), MARGEM, y);
        y -= 13;

        if (quote.getMessage() != null && !quote.getMessage().isBlank()) {
            escrever(cs, fontRegular, 9, COR_SUBTEXTO, quote.getMessage(), MARGEM, y);
            y -= 13;
        }

        return y;
    }

    // ── TABELA ────────────────────────────────────────────────────────────────
    private float desenharTabela(PDPageContentStream cs, PDType1Font fontBold,
                                 PDType1Font fontRegular, Quote quote, float y) throws Exception {
        float xD  = MARGEM;
        float xV  = xD + COL_DESCRICAO;
        float xQ  = xV + COL_VALOR;
        float xT  = xQ + COL_QTD;

        // ── cabeçalho da tabela ──
        float altCabTab = ALTURA_LINHA_TAB + 2;
        desenharRetangulo(cs, xD, y - altCabTab, AREA_UTIL, altCabTab, COR_TABELA_HEAD);
        desenharBordaLinha(cs, xD, y, AREA_UTIL, altCabTab);

        float yCab = y - altCabTab + 6;
        escrever(cs, fontBold, 8, COR_CABECALHO, "DESCRIÇÃO",   xD + 4,  yCab);
        escrever(cs, fontBold, 8, COR_CABECALHO, "VALOR",        xV + 4,  yCab);
        escrever(cs, fontBold, 8, COR_CABECALHO, "QTD",          xQ + 4,  yCab);
        escrever(cs, fontBold, 8, COR_CABECALHO, "TOTAL",        xT + 4,  yCab);

        y -= altCabTab;

        // ── linhas dos itens ──
        int idx = 0;
        for (QuoteItem item : quote.getItems()) {
            // quebra a descrição em linhas de no máx 42 chars
            List<String> linhas = quebrarTexto(item.getDescription(), 42);
            float altLinha = Math.max(ALTURA_LINHA_TAB, linhas.size() * 11f + 8f);

            Color fundo = (idx % 2 == 0) ? Color.WHITE : COR_LINHA_PAR;
            desenharRetangulo(cs, xD, y - altLinha, AREA_UTIL, altLinha, fundo);
            desenharBordaLinha(cs, xD, y, AREA_UTIL, altLinha);

            // texto da descrição (pode ter múltiplas linhas)
            float yTexto = y - 12;
            for (String linha : linhas) {
                escrever(cs, fontRegular, 8, COR_TEXTO, linha, xD + 4, yTexto);
                yTexto -= 11;
            }

            // valor, qtd e total centralizados verticalmente
            float yMid = y - altLinha / 2 - 4;
            escrever(cs, fontRegular, 8, COR_TEXTO, formatarMoeda(item.getValue()),    xV + 4, yMid);
            escrever(cs, fontRegular, 8, COR_TEXTO, item.getQuantity() + " " + (item.getUnit() != null ? item.getUnit() : "un"), xQ + 4, yMid);
            escrever(cs, fontBold,    8, COR_TEXTO, formatarMoeda(item.getTotal()),    xT + 4, yMid);

            y -= altLinha;
            idx++;
        }

        // borda final da tabela
        desenharLinha(cs, xD, xD + AREA_UTIL, y, COR_BORDA);

        return y;
    }

    // ── RODAPÉ ────────────────────────────────────────────────────────────────
    private void desenharRodape(PDPageContentStream cs, PDType1Font fontBold,
                                PDType1Font fontRegular, Quote quote, float y) throws Exception {
        float xEsquerda = MARGEM;
        float xDireita  = MARGEM + AREA_UTIL * 0.55f;

        // condições de pagamento
        if (quote.getPaymentConditions() != null && !quote.getPaymentConditions().isBlank()) {
            escreverPar(cs, fontBold, fontRegular, 9, COR_TEXTO,
                    "Condições de pagamento: ", quote.getPaymentConditions(), xEsquerda, y);
            y -= 14;
        }

        // formas de pagamento — exibe tudo em uma linha simples
        if (quote.getPaymentMethods() != null && !quote.getPaymentMethods().isBlank()) {
            escreverPar(cs, fontBold, fontRegular, 9, COR_TEXTO,
                    "Formas de pagamento: ", quote.getPaymentMethods(), xEsquerda, y);
        }

        // totais no lado direito
        float yTotais = y + 14; // alinha com condições de pagamento

        escreverPar(cs, fontBold, fontRegular, 9, COR_TEXTO,
                "Valor total: ", formatarMoeda(quote.getSubtotal()), xDireita, yTotais);
        yTotais -= 14;

        if (quote.getDiscountPercent() != null && quote.getDiscountPercent() > 0) {
            String descValor = "- " + formatarMoeda(quote.getDiscountAmount())
                    + " (" + (int) Math.round(quote.getDiscountPercent()) + "%)";
            escreverPar(cs, fontBold, fontRegular, 9, COR_TEXTO,
                    "Desconto: ", descValor, xDireita, yTotais);
            yTotais -= 14;
        }

        // valor final em azul e maior
        escreverPar(cs, fontBold, fontBold, 11, COR_DESTAQUE,
                "Valor final: ", formatarMoeda(quote.getFinalValue()), xDireita, yTotais);
    }

    // ── UTILITÁRIOS ──────────────────────────────────────────────────────────

    private void escrever(PDPageContentStream cs, PDType1Font font, float size,
                          Color cor, String texto, float x, float y) throws Exception {
        if (texto == null || texto.isBlank()) return;
        cs.setNonStrokingColor(cor);
        cs.beginText();
        cs.setFont(font, size);
        cs.newLineAtOffset(x, y);
        cs.showText(sanitizar(texto));
        cs.endText();
    }

    // escreve "label" em negrito e "valor" em regular na mesma linha
    private void escreverPar(PDPageContentStream cs, PDType1Font fontLabel, PDType1Font fontValor,
                              float size, Color cor, String label, String valor, float x, float y) throws Exception {
        escrever(cs, fontLabel, size, cor, label, x, y);
        float xValor = x + calcularLarguraTexto(fontLabel, size, label);
        escrever(cs, fontValor, size, cor, valor, xValor, y);
    }

    private void desenharLinha(PDPageContentStream cs, float x1, float x2, float y, Color cor) throws Exception {
        cs.setStrokingColor(cor);
        cs.setLineWidth(0.5f);
        cs.moveTo(x1, y);
        cs.lineTo(x2, y);
        cs.stroke();
    }

    private void desenharRetangulo(PDPageContentStream cs, float x, float y,
                                   float largura, float altura, Color cor) throws Exception {
        cs.setNonStrokingColor(cor);
        cs.addRect(x, y, largura, altura);
        cs.fill();
    }

    private void desenharBordaLinha(PDPageContentStream cs, float x, float y,
                                    float largura, float altura) throws Exception {
        cs.setStrokingColor(COR_BORDA);
        cs.setLineWidth(0.3f);
        cs.addRect(x, y - altura, largura, altura);
        cs.stroke();
    }

    private float calcularLarguraTexto(PDType1Font font, float size, String texto) throws Exception {
        return font.getStringWidth(sanitizar(texto)) / 1000 * size;
    }

    // quebra texto em linhas com limite de caracteres
    private List<String> quebrarTexto(String texto, int maxChars) {
        List<String> linhas = new ArrayList<>();
        if (texto == null) return linhas;
        String[] palavras = texto.split(" ");
        StringBuilder linha = new StringBuilder();
        for (String palavra : palavras) {
            if (linha.length() + palavra.length() + 1 > maxChars) {
                if (!linha.isEmpty()) linhas.add(linha.toString().trim());
                linha = new StringBuilder(palavra);
            } else {
                if (!linha.isEmpty()) linha.append(" ");
                linha.append(palavra);
            }
        }
        if (!linha.isEmpty()) linhas.add(linha.toString().trim());
        return linhas;
    }

    // formata valor como "R$ 1.234,56"
    private String formatarMoeda(BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        String s = valor.setScale(2, RoundingMode.HALF_UP).toPlainString();
        String[] partes = s.split("\\.");
        String inteiro = partes[0].replaceAll("(\\d)(?=(\\d{3})+$)", "$1.");
        return "R$ " + inteiro + "," + partes[1];
    }

    // remove caracteres não suportados pelas fontes Type1 do PDFBox
    private String sanitizar(String texto) {
        if (texto == null) return "";
        return texto
                .replace("ã", "a").replace("Ã", "A")
                .replace("õ", "o").replace("Õ", "O")
                .replace("á", "a").replace("Á", "A")
                .replace("é", "e").replace("É", "E")
                .replace("í", "i").replace("Í", "I")
                .replace("ó", "o").replace("Ó", "O")
                .replace("ú", "u").replace("Ú", "U")
                .replace("â", "a").replace("Â", "A")
                .replace("ê", "e").replace("Ê", "E")
                .replace("î", "i").replace("Î", "I")
                .replace("ô", "o").replace("Ô", "O")
                .replace("û", "u").replace("Û", "U")
                .replace("à", "a").replace("À", "A")
                .replace("ç", "c").replace("Ç", "C")
                .replace("ñ", "n").replace("Ñ", "N")
                .replace("—", "-").replace("–", "-")
                .replace("\u00A0", " ");
    }
}

