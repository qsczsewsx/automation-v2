package com.tcbs.automation.enumerable.bond;

public enum BondOrderStatus {
  DONE("Done"),
  INQUEUE("InQueue"),
  ORDER_NOT_SUCCESSFUL("OrderNotSuccessful"),
  HOLD_MONEY_SUCCESSFUL("HoldMoneySuccessful"),
  MONEY_TRANSFERRED_TO_ISSUER("MoneyTransferredToIssuer"),
  TO_BE_SIGNED("ToBeSigned"),
  MATCHED("Matched"),
  DOING("doing"),
  WAITING("waiting"),
  CANCEL("cancel"),
  WAITING_FOR_CONFIRMED("WaitingForConfirmed"),
  WAITING_FOR_CONFIRM_RM_SERVED("WaitingForConfirmRMServed"),
  WAITING_FOR_CHECKING("WaitingForChecking"),
  WAITING_FOR_PAYMENT_BUY("WaitingForPaymentBuy"),
  WAITING_FOR_PAYMENT_SELL("WaitingForPaymentSell"),
  WAITING_FOR_TRANSFER("WaitingForTransfer"),
  WAITING_FOR_MATCHED("WaitingForMatched"),
  WAITING_FOR_HOLD_MONEY("WaitingForHoldMoney"),
  ALL("all");

  private String value;

  BondOrderStatus(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return this.getValue();
  }
}