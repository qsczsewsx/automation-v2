package com.tcbs.automation.coco.socialinvest;

import com.tcbs.automation.coco.CocoConnBridge;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CRITERIA_GROUP_INFO")
public class CriteriaGroupInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CRITERIA_GROUP_INFO_SEQUENCE")
  @SequenceGenerator(name = "CRITERIA_GROUP_INFO_SEQUENCE", sequenceName = "CRITERIA_GROUP_INFO_SEQUENCE", allocationSize = 1)
  @Column(name = "ID")
  private Long id;

  @Column(name = "GROUP_CODE")
  private String groupCode;

  @Column(name = "GROUP_NAME")
  private String groupName;

  @Column(name = "GROUP_RATE")
  private Double groupRate;

  @Column(name = "CREATED_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  @Column(name = "UPDATED_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedDate;

  public static Optional<CriteriaGroupInfo> findById(Long id) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    Query<CriteriaGroupInfo> query = session.createQuery(
      "from CriteriaGroupInfo c where c.id = :id"
    );
    query.setParameter("id", id);

    try {
      return Optional.of(query.getSingleResult());
    } catch (NoResultException ex) {
      return Optional.empty();
    } finally {
      CocoConnBridge.socialInvestConnection.closeSession();
    }
  }

  public static List<CriteriaGroupInfo> findAll() {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    Query<CriteriaGroupInfo> query = session.createQuery(
      "from CriteriaGroupInfo"
    );
    try {
      return query.getResultList();
    } catch (NoResultException ex) {
      return Collections.emptyList();
    } finally {
      CocoConnBridge.socialInvestConnection.closeSession();
    }
  }
}
