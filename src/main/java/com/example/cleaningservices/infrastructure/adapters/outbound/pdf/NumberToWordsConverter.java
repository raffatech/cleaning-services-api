package com.example.cleaningservices.infrastructure.adapters.outbound.pdf;

import java.math.BigDecimal;

// Converte valor numérico para extenso em português
// Exemplo: 80.00 → "oitenta reais"
public class NumberToWordsConverter {

    private static final String[] UNITS = {
        "", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove",
        "dez", "onze", "doze", "treze", "quatorze", "quinze", "dezesseis",
        "dezessete", "dezoito", "dezenove"
    };

    private static final String[] TENS = {
        "", "", "vinte", "trinta", "quarenta", "cinquenta",
        "sessenta", "setenta", "oitenta", "noventa"
    };

    private static final String[] HUNDREDS = {
        "", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos",
        "seiscentos", "setecentos", "oitocentos", "novecentos"
    };

    public static String convert(BigDecimal value) {
        long intPart = value.longValue();
        int centsPart = value.remainder(BigDecimal.ONE)
                .multiply(BigDecimal.valueOf(100))
                .abs()
                .intValue();

        String result = convertInteger(intPart);
        result += intPart == 1 ? " real" : " reais";

        if (centsPart > 0) {
            result += " e " + convertInteger(centsPart);
            result += centsPart == 1 ? " centavo" : " centavos";
        }

        return result;
    }

    private static String convertInteger(long number) {
        if (number == 0) return "zero";
        if (number == 100) return "cem";
        if (number < 20) return UNITS[(int) number];
        if (number < 100) {
            String result = TENS[(int) (number / 10)];
            if (number % 10 != 0) result += " e " + UNITS[(int) (number % 10)];
            return result;
        }
        if (number < 1000) {
            String result = HUNDREDS[(int) (number / 100)];
            if (number % 100 != 0) result += " e " + convertInteger(number % 100);
            return result;
        }
        if (number < 1000000) {
            long thousands = number / 1000;
            long remainder = number % 1000;
            String result = thousands == 1 ? "mil" : convertInteger(thousands) + " mil";
            if (remainder != 0) result += " e " + convertInteger(remainder);
            return result;
        }
        return String.valueOf(number);
    }
}

