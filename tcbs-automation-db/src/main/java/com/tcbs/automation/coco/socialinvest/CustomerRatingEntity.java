package com.tcbs.automation.coco.socialinvest;

import com.tcbs.automation.coco.CocoConnBridge;
import com.tcbs.automation.constants.coco.Constants;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "CUSTOMER_RATING")
public class CustomerRatingEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "TCBSID")
  private String tcbsid;

  @Column(name = "CUSTODY_ID")
  private String custodyId;

  @Column(name = "RATING_TCBSID")
  private String ratingTcbsid;

  @Column(name = "SCORE")
  private int score;

  @Column(name = "CRITICAL")
  private String critical;

  @CreationTimestamp
  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @UpdateTimestamp
  @Column(name = "UPDATED_TIME")
  private Date updatedTime;

  @Column(name = "RATING_ROLE")
  @Enumerated(EnumType.STRING)
  private Constants.RatingRole ratingRole;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "rating")
  private List<MessageReplyEntity> replyMessages;

  public static List<CustomerRatingEntity> findRatingByTcbsId(String tcbsId) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    Query<CustomerRatingEntity> query = session.createQuery(
      "from CustomerRatingEntity t " +
        "where t.tcbsid = :tcbsid"
    );
    query.setParameter("tcbsid", tcbsId);
    List<CustomerRatingEntity> result = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return result;
  }
}