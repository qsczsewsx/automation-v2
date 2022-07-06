package com.tcbs.automation.tcinvest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Table(name = "INV_BOND_STATISTIC")
public class InvBondStatistic {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "CODE")
  private String code;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "LISTED_STATUS")
  private String listedStatus;
  @Column(name = "TOTAL_ISSUED_VOLUME")
  private String totalIssuedVolume;
  @Column(name = "TOTAL_ISSUED_VALUE")
  private String totalIssuedValue;
  @Column(name = "PAR_VALUE")
  private String parValue;
  @Column(name = "REPORTED_DATE")
  private Date reportedDate;
  @Column(name = "ISSUED_DATE")
  private Date issuedDate;
  @Column(name = "EXPIRED_DATE")
  private Date expiredDate;

  public static List<String> getListCodes(String rowNum) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    ObjectMapper obm = new ObjectMapper();
    Gson gson = new Gson();
    Map<Object, InvBondStatistic> mapProduct = new HashMap<>();
    StringBuilder sql = new StringBuilder("SELECT a.CODE as code ")
      .append(" FROM ")
      .append(" (select bs.CODE from INV_BOND_STATISTIC bs order by Code asc) a ")
      .append(" where ROWNUM < :rowNum ");
    Query query = session.createNativeQuery(sql.toString()).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .setParameter("rowNum", rowNum);

    List<InvBondStatistic> listResult = query.getResultList();
    List<String> codes = new ArrayList<>();
    for (int i = 0; i < listResult.size(); i++) {
      codes.add(((Map) listResult.get(i)).get("CODE").toString());
    }
    return codes;
  }
}
