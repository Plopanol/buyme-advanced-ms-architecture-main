package com.improvemyskills.productservice.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
public class ProductConfig {


	@Value("${product.stock.alert-threshold}")
	private int alertThreshold;

	@Value("${product.pricing.discount-rate}")
	private double discountRate;

	public int getAlertThreshold() {
		return alertThreshold;
	}

	public double getDiscountRate() {
		return discountRate;
	}
}