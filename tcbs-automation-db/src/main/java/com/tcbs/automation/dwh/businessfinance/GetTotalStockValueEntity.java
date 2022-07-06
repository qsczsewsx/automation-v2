package com.tcbs.automation.dwh.businessfinance;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetTotalStockValueEntity {

  @Step("Get stock value info")
  public static List<HashMap<String, Object>> getStockValue(String from, String to){
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select 'Stock' as product, busDate, case when v.custodycd is not NULL then 'VIP' else 'MASS' end as cusType, sum(isnull(BuyAmt, SellAmt)) as totalValue ");
    queryBuilder.append(" from smy_dwh_flx_allStocktxn s left join off_iw_custodycdvip v on s.CustodyCD = v.custodycd ");
    queryBuilder.append(" where s.ETLCurDate = (select max(ETLCurDate) from smy_dwh_flx_allStocktxn) ");
    queryBuilder.append(" and s.FIELD in ('Mua', N'Bán') and SecType = 'STOCK' and BUSDATE between :from and :to ");
    queryBuilder.append(" group by BUSDATE,  case when v.custodycd is not NULL then 'VIP' else 'MASS' end ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("from", from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get total fee")
  public static List<HashMap<String, Object>> getTotalFee(String from, String to){
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select CAST(TXDATE as date) as TXDATE , case when v.custodycd is not NULL then 'VIP' else 'MASS' end as CusType, sum(FEE_AMT_DETAIL) as TotalFee ");
    queryBuilder.append(" from Prc_Flx_Stock_TradingFee f left join off_iw_custodycdvip v on f.CustodyCD = v.custodycd ");
    queryBuilder.append(" where secType_Name = N'Cổ phiếu thường' and EXECTYPE_NAME in ('Mua', N'Bán') and TXDate between :from and :to ");
    queryBuilder.append(" group by TXDATE , case when v.custodycd is not NULL then 'VIP' else 'MASS' end ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("from", from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
