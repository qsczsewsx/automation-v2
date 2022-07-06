package com.tcbs.automation.coco.socialinvest;

import com.tcbs.automation.coco.CocoConnBridge;
import com.tcbs.automation.constants.coco.Constants;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CRITERIA_INFO")
public class CriteriaInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CRITERIA_INFO_SEQUENCE")
  @SequenceGenerator(name = "CRITERIA_INFO_SEQUENCE", sequenceName = "CRITERIA_INFO_SEQUENCE", allocationSize = 1)
  @Column(name = "ID")
  private Long id;

  @Column(name = "CRITERIA_CODE")
  @Enumerated(EnumType.STRING)
  private Constants.CriteriaEnum criteriaCode;

  @Column(name = "CRITERIA_NAME")
  private String criteriaName;

  @Column(name = "CRITERIA_RATE")
  private Double criteriaRate;

  @Column(name = "CRITERIA_GROUP_ID")
  private Long groupId;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "CRITERIA_GROUP_ID", insertable = false, updatable = false)
  private CriteriaGroupInfo criteriaGroupInfo;

  @Column(name = "CREATED_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  @Column(name = "UPDATED_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedDate;

  public static List<CriteriaInfo> findByCriteriaGroupInfo(CriteriaGroupInfo group) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    Query<CriteriaInfo> query = session.createQuery(
      "from CriteriaInfo c where c.criteriaGroupInfo = :group"
    );
    query.setParameter("group", group);
    List<CriteriaInfo> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return res;
  }
}
