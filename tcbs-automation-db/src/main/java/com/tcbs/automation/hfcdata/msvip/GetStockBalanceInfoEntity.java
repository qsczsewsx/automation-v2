package com.tcbs.automation.hfcdata.msvip;

import com.tcbs.automation.hfcdata.HfcData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetStockBalanceInfoEntity {

  @Step("Get stock balance info ms vip from db")
  public static List<HashMap<String, Object>> getStockBalInfo(String custody, String from, String to) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select sb.datereport, sb.symbol, sb.custodycd , sb.afacctno , sb.bondname, CONVERT (BIGINT ,sb.END_TRADE_BAL), sb.END_STANDING_BAL ");
    queryBuilder.append(", sb.END_MORTAGE_BAL , CONVERT (BIGINT , sb.END_BLOCKED_BAL) , sb.END_NETTING_BAL , sb.END_RECEIVING_BAL , sb.CK_CHO_GD , sb.ck_cho_gd_phongtoa ");
    queryBuilder.append(
      ", sb.ck_cho_gd_camco , sb.ck_cho_gd_hccn , CONVERT (BIGINT , sb.END_BAL) , c.customername, c.idnumber, TO_DATE(c.iddate, 'YYYY-MM-DD') as  iddate, c.idplace, c.phone, c.address");
    queryBuilder.append(" from api.iwealth_monthlyport_cf1001 sb");
    queryBuilder.append(" left join api.iwealth_monthlyport_customer c on sb.custodycd = c.custodycd");
    queryBuilder.append(" WHERE c.custodycd = :custody AND sb.datereport BETWEEN :from AND :to");
    try {
      return HfcData.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("custody", custody)
        .setParameter("from", from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
