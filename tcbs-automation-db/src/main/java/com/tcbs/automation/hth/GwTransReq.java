package com.tcbs.automation.hth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "GW_TRANS_REQ")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GwTransReq {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "MSG_TYPE")
  private String msgType;
  @Column(name = "GW_TXN_ID")
  private String gwTxnId;
  @Column(name = "REQUEST")
  private String request;
  @Column(name = "REQUEST_XML")
  private String requestXml;
  @Column(name = "RESPONSE")
  private String response;
  @Column(name = "RESPONSE_XML")
  private String responseXml;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "GWID")
  private String gwid;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;

}
