package com.crio.qcommerce.contract.dto;

import com.opencsv.bean.CsvBindByName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SalesData {
  @CsvBindByName(column = "amount", profiles = { "amazon", "flipkart", "ebay" }, required = true)
  private double amount;

  @CsvBindByName(column = "date", profiles = "amazon", required = true)
  @CsvBindByName(column = "transaction_date", profiles = { "ebay", "flipkart" })
  private String date;

  @CsvBindByName(column = "transaction_id", profiles = { "amazon", "flipkart" })
  @CsvBindByName(column = "txn_id", profiles = "ebay")
  private int transactionId;

  @CsvBindByName(column = "external_transaction_id", profiles = "flipkart")
  @CsvBindByName(column = "ext_txn_id", profiles = "amazon")
  private String externalTransactionId;

  @CsvBindByName(column = "user_id", profiles = { "amazon", "flipkart" })
  @CsvBindByName(column = "username", profiles = "ebay")
  private String userId;

  @CsvBindByName(column = "transaction_status", profiles = { "ebay", "flipkart" })
  @CsvBindByName(column = "status", profiles = "amazon")
  private String transactionStatus;

  @Override
  public String toString() {
    String formatedText = String.format(
        "Amount = %f%nDate = %s%nTransaction_id = %d%nExternal_transaction_id = %s%nUser_id = %s%nTransaction_status = %s%n",
        amount, date, transactionId, externalTransactionId, userId, transactionStatus);
    return formatedText;
  }
}
