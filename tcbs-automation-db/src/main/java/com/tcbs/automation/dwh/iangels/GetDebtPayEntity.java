package com.tcbs.automation.dwh.iangels;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetDebtPayEntity {

  @Step("get debt pay")
  public static List<HashMap<String, Object>> getDebtPay(List<String> tcbsId, String fromDate, String toDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select c.TCBSID as tcbsId , m.custodyCD, autoID, afAcctNo, rlsDate, txDate, loanRate, loanPeriod, be_orglnamt as beOrglnAmt, orgPaidAmt, intPaidAmt ");
    queryBuilder.append(" from Stg_flx_VW_DEBT_PAY_BYCUS m ");
    queryBuilder.append(" left join Smy_dwh_cas_AllUserView c on m.custodycd = c.custodycd and c.ETLCurDate = (select max(ETLCurDate) from Smy_dwh_cas_AllUserView) ");
    queryBuilder.append(" where tcbsid in ( :tcbsId ) ");
    queryBuilder.append(" and txDate between :fromDate and :toDate ");
    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tcbsId", tcbsId)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

}
