package com.crio.qcommerce.contract.analytics;

import com.crio.qcommerce.contract.exceptions.AnalyticsException;
import com.crio.qcommerce.contract.insights.SaleAggregate;

public interface SaleAnalytics {
  SaleAggregate getSaleAggregateByYear(int year) throws AnalyticsException;
}
