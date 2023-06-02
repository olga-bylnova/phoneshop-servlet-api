package com.es.phoneshop.util;

import com.es.phoneshop.exception.QuantityNotIntegerException;
import com.es.phoneshop.exception.OutOfStockException;

import java.text.ParseException;
import java.util.Map;

public class ErrorHandler {
    public static void handleError(Map<Long, String> errors, Long productId, Exception e) {
        if (e.getClass().equals(ParseException.class)) {
            errors.put(productId, "Not a number");
        } else if (e.getClass().equals(QuantityNotIntegerException.class)) {
            errors.put(productId, "Quantity should be an integer number");
        } else {
            errors.put(productId, "Out of stock, max available " + ((OutOfStockException) e).getStockAvailable());
        }
    }
}
