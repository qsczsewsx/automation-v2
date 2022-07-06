package com.tcbs.automation.coco.socialinvest;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MESSAGE_REPLY")
public class MessageReplyEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "TCBSID")
  private String tcbsId;

  @Column(name = "CUSTODY_ID")
  private String custodyId;

  @Column(name = "MESSAGE")
  private String message;

  @Column(name = "RATING_ID")
  private Long ratingId;

  @ManyToOne
  @JoinColumn(name = "RATING_ID", insertable = false, updatable = false)
  // prevent bi-direction creating loop in toString()
  @ToString.Exclude
  private CustomerRatingEntity rating;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "UPDATED_TIME")
  private Date updatedTime;
}
