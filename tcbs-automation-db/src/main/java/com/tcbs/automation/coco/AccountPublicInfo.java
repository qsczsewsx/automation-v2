package com.tcbs.automation.coco;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "ACCOUNT_PUBLIC_INFO")
public class AccountPublicInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "CUSTODY_ID")
  private String custodyId;
  @Column(name = "ACCNO")
  private String accno;
  @Column(name = "PUBLIC_IN")
  private String publicIn;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;

  @Step("fetch dtb record")
  public static List<AccountPublicInfo> getAccountPublicInfoByCustodyId(String custodyId) {
    Session session = CocoConnBridge.cocoConnection.getSession();

    Query<AccountPublicInfo> query = session.createQuery("select a.publicIn from AccountPublicInfo a where a.custodyId=:custodyId");
    query.setParameter("custodyId", custodyId);

    return query.getResultList();
  }

  @Step("delete records from dtb")
  public static void deleteAccountByCustodyId(String custodyId) {
    Session session = CocoConnBridge.cocoConnection.getSession();
    session.clear();
    session.beginTransaction();

    try {
      Query query = session.createQuery("delete from AccountPublicInfo a where a.custodyId=:custodyId");
      query.setParameter("custodyId", custodyId);
      query.executeUpdate();

      session.getTransaction().commit();
    } catch (Exception e) {
      // sth here
      System.out.println("error when clearing dtb");
    }
  }

}
