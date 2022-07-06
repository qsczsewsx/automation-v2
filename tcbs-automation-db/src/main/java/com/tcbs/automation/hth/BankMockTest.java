package com.tcbs.automation.hth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "BANK_MOCK_TEST")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankMockTest {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private BigDecimal id;
  @Column(name = "MSG_TYPE")
  private String msgType;
  @Column(name = "GW_TXN_ID")
  private String gwTxnId;
  @Column(name = "RESPONSE")
  private String response;

  public static BankMockTest mockBatchInquiry(String gwTxnId, String response) {
    BankMockTest bankMockTest = new BankMockTest();
    bankMockTest.setMsgType("RY");
    bankMockTest.setGwTxnId(gwTxnId);
    bankMockTest.setResponse(response);
    deleteByGwTxnId(gwTxnId);
    return bankMockTest;
  }

  public static BankMockTest mockTransInquiry(String gwTxnId, String response) {
    BankMockTest bankMockTest = new BankMockTest();
    bankMockTest.setMsgType("RF");
    bankMockTest.setGwTxnId(gwTxnId);
    bankMockTest.setResponse(response);
    deleteByGwTxnId(gwTxnId);
    return bankMockTest;
  }

  public static void deleteByGwTxnId(String gwTxnId) {
    Session session = HthDb.h2hConnection.getSession();
    session.beginTransaction();
    Query query = session.createQuery("delete from BankMockTest where gwTxnId=:gwTxnId");
    query.setParameter("gwTxnId", gwTxnId).executeUpdate();
    session.getTransaction().commit();
  }

  public void insert() {
    Session session = HthDb.h2hConnection.getSession();
    session.beginTransaction();
    session.save(this);
    session.getTransaction().commit();
  }
}
