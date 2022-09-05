package com.crio.qcommerce.contract.services;

import java.io.File;

import com.crio.qcommerce.contract.analytics.SaleAnalytics;

public enum ServiceBuilder {
  INSTANCE;

  public SaleAnalytics getServiceAnalytics(String provider, File file) {
    SaleAnalytics saleAnalytics = null;

    if (provider.equals("amazon")) {
      saleAnalytics = new AmazonService(file);
    } else if (provider.equals("flipkart")) {
      saleAnalytics = new FlipkartService(file);
    } else if (provider.equals("ebay")) {
      saleAnalytics = new EbayService(file);
    }

    return saleAnalytics;
  }
}
