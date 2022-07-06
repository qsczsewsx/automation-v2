package com.tcbs.automation.tcbond;

import com.tcbs.automation.cas.CAS;
import com.tcbs.automation.cas.TcbsBankAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NAMEVALUESTORAGE")
public class Namevaluestorage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "NAME")
  private String name;
  @NotNull
  @Column(name = "TYPE")
  private String type;
  @Column(name = "VALUE")
  private String value;
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;

  public static Namevaluestorage getByName(String name) {
    Query<Namevaluestorage> query = TcBond.tcBondDbConnection.getSession().createQuery(
      "from Namevaluestorage a where a.name=:name", Namevaluestorage.class);
    query.setParameter("name", name);
    return query.getSingleResult();
  }
}
