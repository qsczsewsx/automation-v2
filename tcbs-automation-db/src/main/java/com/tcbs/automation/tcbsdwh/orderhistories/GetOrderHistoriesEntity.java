package com.tcbs.automation.tcbsdwh.orderhistories;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetOrderHistoriesEntity {
  public static final HibernateEdition hfc = Database.HFC_DATA.getConnection();
  public static final String STATUS_LIST = "statusList";
  public static final String FROM_DATE = "fromDate";
  public static final String TO_DATE = "toDate";
  public static final String ACCOUNT_LIST = "accountList";

  @Step("Get data from db")
  public static List<HashMap<String, Object>> getData(List<String> accountList, List<String> statusList, String from, String to) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select orderid, accountno, exectype , orderqtty, execqtty, symbol, pricetype ");
    queryBuilder.append("   , txtime, cast(txdate as date) as txdate, orstatus, feeacr, quoteprice, matchprice ");
    queryBuilder.append("   , orsorderid, costprice, sectype, foorderid, taxSellAmount ");
    queryBuilder.append(" from stg_flx_vw_secostprice_od ");
    queryBuilder.append("   where txdate BETWEEN TO_DATE(:fromDate, 'yyyy-MM-dd') AND TO_DATE(:toDate, 'yyyy-MM-dd') ");
    if (checkParam(accountList)) {
      queryBuilder.append("  and accountno in :accountList ");
    }
    if (checkParam(statusList)) {
      queryBuilder.append("  and orStatus in :statusList ");
    }
    queryBuilder.append(" and sectype not in ('006') ");
    queryBuilder.append(" order by txdate desc, orderid asc, accountno asc ");

    try {
      if (!checkParam(accountList)) {
        return hfc.getSession()
          .createNativeQuery(queryBuilder.toString())
          .setParameter(STATUS_LIST, statusList)
          .setParameter(FROM_DATE, from)
          .setParameter(TO_DATE, to)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      } else if (!checkParam(statusList)) {
        return hfc.getSession()
          .createNativeQuery(queryBuilder.toString())
          .setParameter(ACCOUNT_LIST, accountList)
          .setParameter(FROM_DATE, from)
          .setParameter(TO_DATE, to)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      }
      return hfc.getSession()
        .createNativeQuery(queryBuilder.toString())
        .setParameter(ACCOUNT_LIST, accountList)
        .setParameter(STATUS_LIST, statusList)
        .setParameter(FROM_DATE, from)
        .setParameter(TO_DATE, to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  private static boolean checkParam(List<String> arrList) {
    if (!arrList.get(0).equalsIgnoreCase("NULL") && !arrList.get(0).equalsIgnoreCase("")) {
      return true;
    }
    return false;
  }

  @Step("Get data from db")
  public static List<HashMap<String, Object>> getSeRetailData(List<String> accountList, List<String> statusList, String from, String to) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select custodycd, symbol, afacctno, qtty, txdate, tradeplace, txnum, price, feeamt, costpricet, priceamt, taxamt, status ");
    queryBuilder.append(" from staging.stg_flx_vw_seretail ");
    queryBuilder.append("   where txdate BETWEEN :fromDate and :toDate ");
    if (!accountList.get(0).equalsIgnoreCase("NULL")) {
      queryBuilder.append("  and afacctno in :accountList ");
    }
    if (!statusList.get(0).equalsIgnoreCase("NULL")) {
      queryBuilder.append("  and status in :statusList ");
    }
    queryBuilder.append(" order by txdate desc, afacctno asc ");

    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession()
        .createNativeQuery(queryBuilder.toString())
        .setParameter(ACCOUNT_LIST, accountList)
        .setParameter(STATUS_LIST, statusList)
        .setParameter(FROM_DATE, from)
        .setParameter(TO_DATE, to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
