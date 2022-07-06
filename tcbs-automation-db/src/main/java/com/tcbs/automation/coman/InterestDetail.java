package com.tcbs.automation.coman;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "INTEREST_DETAIL")
public class InterestDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "INTEREST_ID")
  private String interestId;
  @Column(name = "BANK_ID")
  private String bankId;
  @Column(name = "INTEREST")
  private String interest;


  @Step
  public static List<InterestDetail> getListInterestByInterestId(String interestId) {
    Query<InterestDetail> query = Connection.comanDbConnection.getSession()
      .createQuery("from InterestDetail a where a.interestId=:interestId order by id", InterestDetail.class);
    query.setParameter("interestId", interestId);

    return query.getResultList();
  }

  public static void deleteInterestDetail(String interestId) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InterestDetail> query = session.createQuery(
      "DELETE FROM InterestDetail a WHERE a.interestId = :interestId"
    );
    query.setParameter("interestId", interestId);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
