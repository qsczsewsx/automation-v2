package com.tcbs.automation.dwh.iwealthpartner;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetIBerDirectCusEntity {
  @Step("Get from db")
  public static List<HashMap<String, Object>> getIBerDirectCus(Integer page, Integer size) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select * from ( ");
    queryBuilder.append("	select c.custodyCd, a.tcbsId, c.Is_Ibers ibersType ");
    queryBuilder.append("	from Smy_dwh_IBer_Direct_Customers c  ");
    queryBuilder.append("	left join Smy_dwh_cas_AllUserView a on c.CustodyCD = a.CUSTODYCD and a.EtlCurDate = (select max(Etlcurdate) from Smy_dwh_cas_AllUserView)  ");
    queryBuilder.append("	where a.TCBSID is not null   ");
    queryBuilder.append(") tbl order by tcbsId asc ");
    queryBuilder.append("offset :page * :size rows ");
    queryBuilder.append("fetch next :size rows only ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("page", page)
        .setParameter("size", size)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
