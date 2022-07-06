package com.tcbs.automation.dwh.baymax;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetOrderHistoriesInternalEntity {

  @Step("Get Data")
  public static List<HashMap<String, Object>> getOrderHistoriesFromDB(String accountNo, String orStatus, String symbol, String execType, String fromDate, String toDate, Integer pageIndex, Integer pageSize) {
    StringBuilder query = new StringBuilder();
    query.append(" with execTypeParm as (\n" +
      "select NVL(:p_execType,'') as execType) \n" +
      ", execTypeLst as (\n" +
      "select SPLIT_PART((select execType from execTypeParm), ',', n) code \n" +
      "from api.cnt_values \n" +
      "where SPLIT_PART((select execType from execTypeParm), ',', n) <> '')\n" +
      ", tmpData as (\n" +
      "select 'orderHistory' \"object\", orderid, accountno, exectype , orderqtty, execqtty, bratio, codeid, symbol, pricetype\n" +
      ", txtime, txdate, expdate, timetype, orstatus, feeacr , limitprice, cancelqtty, via, quoteprice, matchprice \n" +
      ", taxsellamount as taxSellAmout, tradeplace, matchtype, null isdisposal,  null isamend,  null iscancel, null  username, orsorderid, costprice, sectype \n" +
      "from staging.stg_flx_vw_secostprice_od\n" +
      "where accountno = :p_account\n" +
      "and txdate BETWEEN cast(:p_fromDate as timestamp) and cast(:p_toDate as timestamp)\n" +
      "and ((:p_orStatus = 'null') or (:p_orStatus != 'null' and orstatus = :p_orStatus))\n" +
      "and ((:p_symbol = 'null') or (:p_symbol != 'null' and symbol = :p_symbol))\n" +
      "and ((:p_execType = 'null') or (:p_execType != 'null' and exectype in (select code from execTypeLst)))\n" +
      "group by  orderid, accountno, exectype , orderqtty, execqtty, bratio, codeid, symbol, pricetype\n" +
      ", txtime, txdate, expdate, timetype, orstatus, feeacr , limitprice, cancelqtty, via, quoteprice, matchprice \n" +
      ", taxsellamount, tradeplace, matchtype, orsorderid, costprice, sectype ), \n" +
      "total_count  as (select count(*) as totalCount from tmpData)\n" +
      "select a.* , totalCount from tmpData a  left join total_count on 1=1  \n" +
      "order by txdate desc, orderid asc  ");
    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("p_execType", execType)
        .setParameter("p_orStatus", orStatus)
        .setParameter("p_symbol", symbol)
        .setParameter("p_account", accountNo)
        .setParameter("p_fromDate", fromDate)
        .setParameter("p_toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();

  }
}
