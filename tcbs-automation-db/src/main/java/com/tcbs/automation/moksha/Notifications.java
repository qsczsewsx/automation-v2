package com.tcbs.automation.moksha;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigInteger;
import java.sql.*;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "notifications")
public class Notifications {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "id")
  private Long id;
  @Column(name = "tcbs_id")
  private String tcbsId;
  @Column(name = "code_stock")
  private String codeStock;
  @Column(name = "content_warning")
  private String contentWarning;
  @Column(name = "price")
  private Double price;
  @Column(name = "price_vs_ref_percent")
  private Double priceVsRefPercent;
  @Column(name = "warning_name")
  private String warningName;
  @Column(name = "warning_type")
  private String warningType;
  @Column(name = "additional_info")
  private String additionalInfo;
  @Column(name = "time_warning")
  private Timestamp timeWarning;
  @Column(name = "created_date")
  private Timestamp createdDate;

  public static List<Notifications> getListNotiByTcbsid(String tcbsid) {
    Session session = MoksaRepository.moksaDbConnection.getSession();
    session.clear();
    Query<Notifications> query = session.createQuery("from Notifications w where w.tcbsId =: tcbsid order by id desc");
    query.setParameter("tcbsid", tcbsid);
    List<Notifications> result = query.getResultList();
    return result;
  }

  public static List<Notifications> getListNotiByTcbsidAndCode(String tcbsid, String codeStock) {
    Session session = MoksaRepository.moksaDbConnection.getSession();
    session.clear();
    Query<Notifications> query = session.createQuery("from Notifications w where w.tcbsId =: tcbsid and w.codeStock =:codeStock order by id desc");
    query.setParameter("tcbsid", tcbsid);
    query.setParameter("codeStock", codeStock);
    List<Notifications> result = query.getResultList();
    return result;
  }

  public static Long countNotiByTcbsId(String tcbsid, String symbol) {
    Session session = MoksaRepository.moksaDbConnection.getSession();

    Query query = session.createNativeQuery(
      String.format("select count(w.id) as count from notifications w where w.tcbs_id = '%s' and w.code_stock = '%s'", tcbsid, symbol)
    );
    BigInteger count = (BigInteger) query.getSingleResult();
    return count.longValue();
  }
}
