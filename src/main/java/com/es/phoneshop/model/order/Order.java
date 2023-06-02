package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.generic.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Order extends Cart implements Entity {
    private Long id;
    private String secureId;
    private BigDecimal subTotal;
    private BigDecimal deliveryCost;

    private String firstName;
    private String lastName;
    private String phone;

    private LocalDate deliveryDate;
    private String deliveryAddress;

    private PaymentMethod paymentMethod;

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(BigDecimal deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Order() {}
    public Order(BigDecimal subTotal, BigDecimal deliveryCost, String firstName, String lastName, String phone, LocalDate deliveryDate, String deliveryAddress, PaymentMethod paymentMethod) {
        this.subTotal = subTotal;
        this.deliveryCost = deliveryCost;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.deliveryDate = deliveryDate;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "Order{" +
                "subTotal=" + subTotal +
                ", deliveryCost=" + deliveryCost +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", deliveryDate=" + deliveryDate.toString() +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", paymentMethod=" + paymentMethod +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecureId() {
        return secureId;
    }

    public void setSecureId(String secureId) {
        this.secureId = secureId;
    }
}
