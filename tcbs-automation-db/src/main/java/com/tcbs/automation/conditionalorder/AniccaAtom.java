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
@Table(name = "ATOM_CONDITION")
public class AniccaAtom {
  @Id
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "EXPRESSION")
  private String expression;
  @Column(name = "EXPLAINED_EXPRESSION")
  private String explainedExpression;
  @NotNull
  @Column(name = "OPERAND_TYPE")
  private String operandType;
  @NotNull
  @Column(name = "OPERAND_DATA")
  private String operandData;
  @Column(name = "OPERATOR")
  private String operator;
  @Column(name = "PARAM")
  private String param;
  @NotNull
  @Column(name = "COND_ID")
  private String condId;
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;

  @Step
  public static AniccaAtom getAtomById(String id) {
    Query<AniccaAtom> query = TheConditionalOrder.aniccaDbConnection.getSession()
      .createQuery("from AniccaAtom a where a.id=:id", AniccaAtom.class);
    query.setParameter("id", id);

    return query.getSingleResult();
  }

  @Step
  public static AniccaAtom getExpressionId(String condId) {
    Query<AniccaAtom> query = TheConditionalOrder.aniccaDbConnection.getSession()
      .createQuery("from AniccaAtom a where a.condId=:condId", AniccaAtom.class);
    query.setParameter("condId", condId);

    return query.getSingleResult();
  }
}


// hibernate quan li object lay tu db ra vong doi
// jpa hibernate 1 thang la concept, thang con lai de implement