package com.tcbs.automation.conditionalorder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CONDITION")
public class AniccaCondition {

  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "DEF_TYPE")
  private String defType;
  @Column(name = "DEF_EXPRESSION")
  private String defExpression;
  @NotNull
  @Column(name = "STATUS")
  private String status;
  @Column(name = "META_DATA")
  private String metaData;
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;
  @Column(name = "START_AT")
  private String startAt;
  @Column(name = "END_AT")
  private String endAt;

  @Step
  public static AniccaCondition getConditionById(String id) {
    Query<AniccaCondition> query = TheConditionalOrder.aniccaDbConnection.getSession()
      .createQuery("from AniccaCondition a where a.id=:id", AniccaCondition.class);
    query.setParameter("id", id);

    return query.getSingleResult();
  }
}
