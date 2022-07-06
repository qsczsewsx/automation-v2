package com.tcbs.automation.dwh;

import net.thucydides.core.annotations.Step;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;

public class CusOfRmEntity {
  @Step("get cus of RM from db")
  public static List<HashMap<String, Object>> byCondition(String rmCustody) {
    StringBuilder query = new StringBuilder();
    query.append("select pu.cus_tcbsid as tcbsid, pu.cus_custodycode as custodyCode, au.fullname as customerName, ");
    query.append("au.idNumber, cast(au.birthday as date)  birthday from smy_dwh_cas_parentuser pu  ");
    query.append("left join smy_dwh_cas_alluserview au on pu.Cus_custodycode = au.custodycd  ");
    query.append("where pu.etlcurdate = (select max(etlcurdate) from smy_dwh_cas_parentuser)  ");
    query.append("and au.etlcurdate = (select max(etlcurdate) from smy_dwh_cas_alluserview)  ");
    query.append("and rm_custodycode = :rmCustody  ");
    query.append("group by pu.cus_tcbsid, pu.cus_custodycode, au.fullname, au.idnumber, au.birthday;  ");

    return Dwh.dwhDbConnection.getSession().createNativeQuery(query.toString())
      .setParameter("rmCustody", rmCustody)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
  }
}

