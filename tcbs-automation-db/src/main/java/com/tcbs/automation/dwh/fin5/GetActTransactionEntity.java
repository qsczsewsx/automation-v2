package com.tcbs.automation.dwh.fin5;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetActTransactionEntity {

  @Step("Get data")
  public static List<HashMap<String, Object>> getActTransInfo (String from, String to) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select TRADING_ID as tradingId, SALE_TYPE as saleType, trading_code as tradingcode, quantity, accountant_time as accountanttime ");
    queryStringBuilder.append(" , cast(trading_date as date) as tradingdate, underlying_code as underlyingcode ");
    queryStringBuilder.append(" , action, price, principal, cost_good as costgood, round( pnl) as pnl, pnl_type as pnltype ");
    queryStringBuilder.append(" , round(accrued_interest) as accruedinterest, custody_code as custodycode, counterparty_code as counterpartycode ");
    queryStringBuilder.append(" , EXTRA_AMOUNT, DISCOUNT_AMOUNT, TYPE ");
    queryStringBuilder.append(" , case when vip.custodycd is not null then 'VIP' else 'Mass' end as customertype ");
    queryStringBuilder.append(" from staging.Stg_tca_ACT_TRANSACTION_BF_VIEW a ");
    queryStringBuilder.append(" left join staging.vw_smy_cas_alluser cauv on a.custody_code = cauv.tcbsid ");
    queryStringBuilder.append(" left join staging.off_iw_custodycdvip vip on cauv.custodycd = vip.custodycd ");
    queryStringBuilder.append(" where accountant_time >= :p_fromdate and accountant_time <= :p_todate ");
    queryStringBuilder.append(" and act_type in ('1AC1AG','1AC2AG','1AG','1AG1AC') order by accountant_time, underlying_code, custody_code ");
    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setParameter("p_fromdate", from)
        .setParameter("p_todate", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
