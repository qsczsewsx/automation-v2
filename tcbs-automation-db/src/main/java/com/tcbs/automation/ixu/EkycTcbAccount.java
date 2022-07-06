package com.tcbs.automation.ixu;


import com.google.common.collect.Maps;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.assertj.core.util.Lists;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "EKYC_TCB_ACCOUNT")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EkycTcbAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Long id;

  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "CODE_105C")
  private String code105c;

  @Column(name = "EKYC_DATE")
  private Timestamp ekycDate;

  @Column(name = "EXPIRED_DATE")
  private Timestamp expiredDate;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Column(name = "LAST_UPDATED_DATE")
  private Timestamp lastUpdatedDate;


  @Step
  public static void clearEkycTcbAccount(List<String> tcbsIds) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from EkycTcbAccount his where his.tcbsId in :tcbsIds ");
    query.setParameter("tcbsIds", tcbsIds);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void insertEkycTcbAccount(EkycTcbAccount ekyctcbAccount) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(ekyctcbAccount);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }


  @Step
  public static List<HashMap<String, Object>> getEkycTcbAccount(List<String> tcbsIds) {
    ixuDbConnection.getSession().clear();
    Query query = ixuDbConnection.getSession().createNativeQuery(
      "select his.TCBS_ID, Trunc(his.EKYC_DATE) as EKYC_DATE, Trunc(his.EXPIRED_DATE) as EXPIRED_DATE " +
        "from EKYC_TCB_ACCOUNT his where his.TCBS_ID in :tcbsIds ");
    query.setParameter("tcbsIds", tcbsIds);

    List<Object[]> results = query.getResultList();
    String[] responseField = {"tcbsId", "ekycDate", "expiredDate",};

    List<HashMap<String, Object>> response = Lists.newArrayList();

    if (!results.isEmpty()) {
      results.forEach(result -> {
        HashMap<String, Object> map = Maps.newHashMap();
        for (int i = 0; i < responseField.length; i++) {
          map.put(responseField[i], result[i]);
        }
        response.add(map);
      });
    }
    ixuDbConnection.closeSession();
    return response;
  }

}
