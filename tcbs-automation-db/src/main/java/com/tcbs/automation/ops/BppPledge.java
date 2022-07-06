package com.tcbs.automation.ops;

import com.tcbs.automation.enumerable.orion.hercules.ContractStatus;
import com.tcbs.automation.enumerable.orion.hercules.CreatedChannel;
import com.tcbs.automation.enumerable.orion.hercules.UnBlockPurpose;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BPP_PLEDGE")
public class BppPledge {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPP_PLEDGE_SEQ")
  @SequenceGenerator(sequenceName = "BPP_PLEDGE_SEQ", allocationSize = 1, name = "BPP_PLEDGE_SEQ")
  private Integer id;

  @Column(name = "PLEDGE_CODE")
  private String pledgeCode;

  @Column(name = "RECEIVER")
  private String receiver;

  @Column(name = "RECEIVER_CITAD_CODE")
  private String receiverCitadCode;

  @Column(name = "CONTACT")
  private String contact;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "PHONE")
  private String phone;

  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "EXECUTED_DATE")
  @Temporal(TemporalType.DATE)
  private Date executedDate;

  @Column(name = "EXECUTED_FEE")
  private Integer executedFee;

  @Column(name = "CREATED_DATE")
  @Temporal(TemporalType.DATE)
  private Date createdDate;

  @Column(name = "UPDATED_DATE")
  @Temporal(TemporalType.DATE)
  private Date updatedDate;

  @Column(name = "REQUESTED_DATE")
  @Temporal(TemporalType.DATE)
  private Date requestedDate;

  @Column(name = "PLEDGE_DATE")
  @Temporal(TemporalType.DATE)
  private Date pledgeDate;

  @Column(name = "MAKER")
  private String maker;

  @Column(name = "CHECKER")
  private String checker;

  @Column(name = "RM_TCBS_ID")
  private String rmTcbsID;

  @Column(name = "CREATED_CHANNEL")
  @Enumerated(EnumType.STRING)
  private CreatedChannel createdChannel;

  @OneToMany(targetEntity = BppContract.class, mappedBy = "block", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<BppContract> listBlockContracts;

  @OneToMany(targetEntity = BppContract.class, mappedBy = "unblock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<BppContract> listUnblockContracts;

  @Column(name = "REJECTED_REASON")
  private String rejectedReason;

  @Column(name = "ORG_BOND_TYPE")
  private String orgBondType;

  @Column(name = "UNBLOCK_PURPOSE")
  @Enumerated(EnumType.STRING)
  private UnBlockPurpose unblockPurpose;

  public static void deletePledgeById(Integer pledgeId) {
    OPS.opsConnection.getSession().clear();
    OPS.opsConnection.getSession().beginTransaction();

    Query query = OPS.opsConnection.getSession().createQuery(
      "delete from BppPledge p where p.id=:pledgeId");

    query.setParameter("pledgeId", pledgeId);
    query.executeUpdate();

    OPS.opsConnection.getSession().getTransaction().commit();
  }

  public static List<BppPledge> findByTcbsId(String tcbsId) {
    Session session = null;
    List<BppPledge> bppPledgeList = new ArrayList<>();
    try {
      session = OPS.opsConnection.getSession();
      session.clear();

      Query<BppPledge> query = session.createQuery(
        "SELECT c FROM BppPledge c WHERE c.tcbsId =:tcbsId AND c.createdChannel IN (com.tcbs.automation.enumerable.orion.hercules.CreatedChannel.THEO_SAN_PHAM)", BppPledge.class);
      query.setParameter("tcbsId", tcbsId);

      bppPledgeList = query.getResultList();
    } finally {
//            session.close();
    }

    return bppPledgeList;
  }

  public static Integer save(BppPledge bppPledge) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Integer generatedId = (Integer) session.save(bppPledge);
    session.getTransaction().commit();

    return generatedId;
  }

  public static void updateRmTcbsId(Integer id, String rmTcbsId) {
    if (id == null) {
      return;
    }
    ContractStatus contractStatus = null;
    Session session = OPS.opsConnection.getSession();
    session.beginTransaction();

    BppPledge bppPledge = session.get(BppPledge.class, id);
    bppPledge.setRmTcbsID(rmTcbsId);

    session.getTransaction().commit();
  }

  public static BppPledge getById(Integer id) {
    if (id == null) {
      return new BppPledge();
    }
    Session session = OPS.opsConnection.getSession();
    session.clear();

    return session.get(BppPledge.class, id);
  }

  public static void updateExecuteDate(Integer id, Date executedDate) {
    if (id == null) {
      return;
    }
    Session session = OPS.opsConnection.getSession();
    session.clear();
    session.beginTransaction();
    BppPledge pledge = session.get(BppPledge.class, id);
    pledge.setExecutedDate(executedDate);
    session.getTransaction().commit();
  }
}
