package com.crio.qcommerce.contract.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crio.qcommerce.contract.parsers.CsvParser;
import com.crio.qcommerce.contract.dto.SalesData;
import com.crio.qcommerce.contract.exceptions.AnalyticsException;
import com.crio.qcommerce.contract.insights.SaleAggregate;
import com.crio.qcommerce.contract.insights.SaleAggregateByMonth;
import com.crio.qcommerce.contract.analytics.SaleAnalytics;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class AmazonService implements CsvParser, SaleAnalytics {
  private File file;

  protected AmazonService(File file) {
    this.file = file;
  }

  @Override
  public List<SalesData> parseFileToBean(File file) throws AnalyticsException {
    List<SalesData> salesData = new ArrayList<>();
    CsvToBean<SalesData> salesBean = null;

    try {
      Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

      salesBean = new CsvToBeanBuilder<SalesData>(reader).withType(SalesData.class).withThrowExceptions(false)
          .withProfile("amazon").build();

      salesData = salesBean.parse();
      List<CsvException> exceptions = salesBean.getCapturedExceptions();

      reader.close();

      for (CsvException exception : exceptions) {
        throw exception;
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (CsvRequiredFieldEmptyException e) {
      throw new AnalyticsException(e.getMessage());
    } catch (CsvException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return salesData;
  }

  @Override
  public SaleAggregate getSaleAggregateByYear(int year) throws AnalyticsException {
    List<SalesData> salesData = parseFileToBean(file);
    Double totalSales = 0.0;
    SaleAggregate saleAggregate = new SaleAggregate();
    Map<Integer, Double> monthSales = new HashMap<>();
    List<SaleAggregateByMonth> aggregateByMonths = new ArrayList<>();

    for (SalesData saleData : salesData) {
      LocalDate date = LocalDate.parse(saleData.getDate());
      String transactionStatus = saleData.getTransactionStatus();

      if (transactionStatus.equals("shipped") && date.getYear() == year) {
        Double amount = saleData.getAmount();
        Integer month = date.getMonth().getValue();
        totalSales += amount;

        if (monthSales.containsKey(month))
          monthSales.put(month, monthSales.get(month) + amount);
        else
          monthSales.put(month, amount);
      }
    }

    for (Map.Entry<Integer, Double> entry : monthSales.entrySet()) {
      aggregateByMonths.add(new SaleAggregateByMonth(entry.getKey(), entry.getValue()));
    }

    Comparator<SaleAggregateByMonth> comparator = Comparator.comparing(SaleAggregateByMonth::getMonth);

    Collections.sort(aggregateByMonths, comparator);

    saleAggregate.setTotalSales(totalSales);
    saleAggregate.setAggregateByMonths(aggregateByMonths);

    return saleAggregate;
  }

}
