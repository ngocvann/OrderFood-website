package com.foodgo.service;

import com.foodgo.model.Order;
import com.foodgo.model.PaymentResponse;
import com.stripe.exception.StripeException;

public interface PaymentService {
	
	public PaymentResponse generatePaymentLink(Order order) throws StripeException;

}
