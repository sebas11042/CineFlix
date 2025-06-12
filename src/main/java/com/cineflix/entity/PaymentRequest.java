package com.cineflix.entity;

public class PaymentRequest {
    private String currency;
    private double amount;
    private String description;
    private String cancelUrl;
    private String successUrl;

    // Constructor vacío
    public PaymentRequest() {}

    // Constructor con parámetros
    public PaymentRequest(String currency, double amount, String description, String cancelUrl, String successUrl) {
        this.currency = currency;
        this.amount = amount;
        this.description = description;
        this.cancelUrl = cancelUrl;
        this.successUrl = successUrl;
    }

    // GETTERS y SETTERS
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }
}
