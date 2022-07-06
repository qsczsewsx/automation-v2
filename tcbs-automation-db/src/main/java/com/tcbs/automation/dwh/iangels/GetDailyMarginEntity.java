package com.tcbs.automation.dwh.iangels;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetDailyMarginEntity {

  @Step("get data daily margin")
  public static List<HashMap<String, Object>> getDailyMargin(List<String> tcbsId, String executedDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select tcbsId, m.custodyCD, autoID, from_date as fromDate, rlsDate, intBal, irRate, intAmt ");
    queryBuilder.append(" from Stg_flx_VW_DAILY_MARGIN_BYCUS m ");
    queryBuilder.append(" left join Smy_dwh_cas_AllUserView c on m.custodycd = c.custodycd and c.ETLCurDate = (select max(ETLCurDate) from Smy_dwh_cas_AllUserView) ");
    queryBuilder.append(" where tcbsid in ( :tcbsId ) ");
    queryBuilder.append(" and from_date = :executedDate order by tcbsid asc ");
    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tcbsId", tcbsId)
        .setParameter("executedDate", executedDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }
}
