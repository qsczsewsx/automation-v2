package com.tcbs.automation.coman;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "BANK_GROUP")
public class BankGroupTbl {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Integer id;
  @Column(name = "NAME")
  private String name;
  @Column(name = "CODE")
  private String code;

  @Step("get bank group")
  public static List<BankGroupTbl> getBankGroup(HashMap<String, Object> params) {
    StringBuilder querySQL = new StringBuilder("FROM BankGroupTbl WHERE 1 = 1");
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      querySQL.append(" and ");
      querySQL.append(entry.getKey() + " = :" + entry.getKey());
    }
    querySQL.append(" order by id asc");
    try {
      Query<BankGroupTbl> query = Connection.comanDbConnection.getSession().createQuery(querySQL.toString(), BankGroupTbl.class);
      for (Map.Entry<String, Object> entry : params.entrySet()) {
        query.setParameter(entry.getKey(), entry.getValue());
      }
      return query.getResultList();
    } catch (Exception ex) {
      return new ArrayList<>();
    }
  }

  public static List<BankGroupTbl> getAllBankGroup() {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<BankGroupTbl> query = session.createQuery("FROM BankGroupTbl order by id asc ", BankGroupTbl.class);
    return query.getResultList();
  }

}
