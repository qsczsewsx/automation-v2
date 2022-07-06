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
@Table(name = "INV_INTERNAL_USER_INFO")
public class InvInternalUserInfo {
  public static Session session;
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "CUSTOMER_ID_TYPE")
  private String customerIdType;
  @Column(name = "CUSTOMER_ID")
  private String customerId;
  @Column(name = "CUSTOMER_FULL_NAME")
  private String customerFullName;

  public static boolean isInternalUserFund(String customerId) {
    Query<InvInternalUserInfo> query = session.createQuery("from InvInternalUserInfo where customerId=:customerId");
    query.setParameter("customerId", customerId);
    List<InvInternalUserInfo> result = query.getResultList();
    if (result != null && result.size() > 0) {
      return true;
    } else {
      return false;
    }
  }
}
