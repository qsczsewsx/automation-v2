package com.tcbs.automation.conditionalorder;

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
@Getter
@Setter
@NoArgsConstructor
@Table(name = "DRAFT_CONDITIONAL_ORDER")
public class DraftConditionalOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "TCBSID")
  private String tcbsid;
  @NotNull
  @Column(name = "CODE105C")
  private String code105C;
  @NotNull
  @Column(name = "ACCOUNT_NO")
  private String accountNo;
  @NotNull
  @Column(name = "ACCOUNT_TYPE")
  private String accountType;
  @NotNull
  @Column(name = "SYMBOL")
  private String symbol;
  @NotNull
  @Column(name = "VOLUME")
  private String volume;
  @NotNull
  @Column(name = "ORDER_TYPE")
  private String orderType;
  @NotNull
  @Column(name = "START_DATE")
  private String startDate;
  @NotNull
  @Column(name = "END_DATE")
  private String endDate;
  @NotNull
  @Column(name = "EXEC_TYPE")
  private String execType;
  @NotNull
  @Column(name = "META_DATA")
  private String metaData;
  @NotNull
  @Column(name = "CREATED_ON")
  private String createdOn;
  @Column(name = "MODIFIED_ON")
  private String modifiedOn;

  @Step
  public static DraftConditionalOrder getDraftConditionalOrderById(String id) {
    Query<DraftConditionalOrder> query = TheConditionalOrder.anattaDbConnection.getSession()
      .createQuery("from DraftConditionalOrder a where a.id=:id", DraftConditionalOrder.class);
    query.setParameter("id", id);

    return query.getSingleResult();
  }

  @Step
  public static List<DraftConditionalOrder> getListDraftConditionalOrderById(String id) {
    Query<DraftConditionalOrder> query = TheConditionalOrder.anattaDbConnection.getSession()
      .createQuery("from DraftConditionalOrder a where a.id=:id", DraftConditionalOrder.class);
    query.setParameter("id", id);

    return query.getResultList();
  }

  @Step
  public static List<DraftConditionalOrder> getListDraftConditionalOrderByTcbsId(String tcbsid) {
    Query<DraftConditionalOrder> query = TheConditionalOrder.anattaDbConnection.getSession()
      .createQuery("from DraftConditionalOrder a where a.tcbsid=:tcbsid", DraftConditionalOrder.class);
    query.setParameter("tcbsid", tcbsid);

    return query.getResultList();
  }

  @Step
  public static List<DraftConditionalOrder> getListDraftConditionalOrderByTcbsIdAndParam(String tcbsid, String param, String value) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * ");
    queryStringBuilder.append("FROM DRAFT_CONDITIONAL_ORDER   ");
    queryStringBuilder.append("WHERE TCBSID=:tcbsid ");
    queryStringBuilder.append(String.format("AND %s like :value ", param));
    Session session = TheConditionalOrder.anattaDbConnection.getSession();
    List<DraftConditionalOrder> result = session.createNativeQuery(queryStringBuilder.toString(), DraftConditionalOrder.class).setParameter("tcbsid", tcbsid)
      .setParameter("value", "%" + value + "%")
      .getResultList();

    return result;
  }

}
