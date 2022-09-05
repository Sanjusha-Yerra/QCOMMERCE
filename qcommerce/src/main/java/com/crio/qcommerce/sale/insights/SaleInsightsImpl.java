package com.crio.qcommerce.sale.insights;

import java.io.File;
import java.io.IOException;

import com.crio.qcommerce.contract.analytics.SaleAnalytics;
import com.crio.qcommerce.contract.services.ServiceBuilder;
import com.crio.qcommerce.contract.exceptions.AnalyticsException;
import com.crio.qcommerce.contract.resolver.DataProvider;
import com.crio.qcommerce.contract.insights.SaleInsights;
import com.crio.qcommerce.contract.insights.SaleAggregate;

public class SaleInsightsImpl implements SaleInsights {

  public SaleInsightsImpl() {
  }

  @Override
  public SaleAggregate getSaleInsights(DataProvider dataProvider, int year) throws IOException, AnalyticsException {
    String provider = dataProvider.getProvider();
    File file = dataProvider.resolveFile();

    SaleAnalytics saleAnalytics = ServiceBuilder.INSTANCE.getServiceAnalytics(provider, file);

    SaleAggregate saleAggregate = saleAnalytics.getSaleAggregateByYear(year);

    return saleAggregate;
  }

}
