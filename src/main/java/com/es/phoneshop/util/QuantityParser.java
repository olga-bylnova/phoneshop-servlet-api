package com.es.phoneshop.util;

import com.es.phoneshop.exception.QuantityNotIntegerException;
import jakarta.servlet.http.HttpServletRequest;

import java.text.NumberFormat;
import java.text.ParseException;

public class QuantityParser {
    public static int getQuantity(HttpServletRequest request, String quantityString) throws ParseException, QuantityNotIntegerException {
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        int intValue = format.parse(quantityString).intValue();
        double doubleValue = format.parse(quantityString).doubleValue();

        if (intValue != doubleValue) {
            throw new QuantityNotIntegerException();
        }
        return format.parse(quantityString).intValue();
    }
}
