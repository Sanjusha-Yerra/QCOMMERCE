package com.crio.qcommerce.contract.parsers;

import java.io.File;
import java.util.List;

import com.crio.qcommerce.contract.dto.SalesData;
import com.crio.qcommerce.contract.exceptions.AnalyticsException;

public interface CsvParser {
  List<SalesData> parseFileToBean(File file) throws AnalyticsException;
}
