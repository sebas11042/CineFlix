package com.cineflix.service;

import com.cineflix.entity.PaymentRequest;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

@Service
public class PaypalService {

    @Autowired
    private APIContext apiContext;

    public Payment createPayment(PaymentRequest request) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(request.getCurrency());

        // Formatear el monto correctamente con dos decimales
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00", symbols);
        String formattedAmount = decimalFormat.format(request.getAmount());

        amount.setTotal(formattedAmount); // Monto corregido

        Transaction transaction = new Transaction();
        transaction.setDescription(request.getDescription());
        transaction.setAmount(amount);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(Arrays.asList(transaction));

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(request.getCancelUrl());
        redirectUrls.setReturnUrl(request.getSuccessUrl());
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecution);
    }

}
