package com.tcbs.automation.tcinvest.ifund.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("unchecked")
@Table(name = "INV_INTERNAL_USER_UNDERLYING_PRODUCT")
public class InvInternalUserUnderlyingProduct {
  public static Session session;
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "CUSTOMER_ID")
  private String customerId;
  @Column(name = "UNDERLYING_PRODUCT_ID")
  private int underlyingProductId;
  @Column(name = "UNDERLYING_PRODUCT_TABLE_NAME")
  private String underlyingProductTableName;
  @Column(name = "DESCRIPTION")
  private String description;

  public List<InvInternalUserUnderlyingProduct> getListFundNotAllowedToBuy(String customerId) {
    Query<InvInternalUserUnderlyingProduct> query = session.createQuery("from InvInternalUserUnderlyingProduct where customerId=:customerId");
    query.setParameter("customerId", customerId);
    List<InvInternalUserUnderlyingProduct> result = query.getResultList();
    return result;
  }
}
