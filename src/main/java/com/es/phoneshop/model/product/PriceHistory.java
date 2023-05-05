package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.Date;

public class PriceHistory {
    private Date priceChange;
    private BigDecimal price;

    public Date getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(Date priceChange) {
        this.priceChange = priceChange;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public PriceHistory(Date priceChange, BigDecimal price) {
        this.priceChange = priceChange;
        this.price = price;
    }
}
