package com.tcbs.automation.coco.socialinvest;

import com.tcbs.automation.coco.CocoConnBridge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "ACCOUNT")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Long id;
  @NotNull
  @Column(name = "TCBSID")
  private String tcbsid;
  @Column(name = "CUSTODY_ID")
  private String custodyId;
  @Column(name = "ACCOUNT_NO")
  private String accountNo;
  @Column(name = "ACCOUNT_TYPE")
  private String accountType;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;
  @Column(name = "REGULAR_ACCOUNT_NO")
  private String regularAccountNo;
  @Column(name = "OPENACC_REFERENCEID")
  private String openAccRefId;

  public static AccountEntity save(AccountEntity account) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    session.beginTransaction();
    session.save(account);
    session.getTransaction().commit();
    return account;
  }
}