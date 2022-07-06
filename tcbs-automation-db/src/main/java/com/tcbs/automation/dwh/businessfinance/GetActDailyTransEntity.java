package com.tcbs.automation.dwh.businessfinance;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetActDailyTransEntity {

  @Step("Get Data")
  public static List<HashMap<String, Object>> getActDailyTransInfo(String fromDate, String toDate){
    StringBuilder query = new StringBuilder();
    query.append(" select accountant_time as accountanttime, underlying_code as underlyingcode, b.Price par, open_bl_amount as openblamount ");
    query.append(" , sell_price as sellprice, buy_principal as buyprincipal, open_bl_quantity as openblquantity, sell_quantity as sellquantity ");
    query.append(" , buy_quantity as buyquantity, total_profit as totalprofit, total_loss as totalloss, accrued_interest as accruedinterest ");
    query.append(" , close_unit_price as closeUnitPrice, total_invest as totalInvest, delta_accrued_interest as deltaAccruedInterest, ");
    query.append(" open_total_accrued as openTotalAccrued, close_total_accrued as closeTotalAccrued ");
    query.append(" from stg_tca_ACT_DAILY_TRANSACTION adt ");
    query.append(" left join Stg_tcb_Bond b on adt.UNDERLYING_CODE = b.Code ");
    query.append(" where accountant_time >= :fromDate and accountant_time <= :toDate ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
