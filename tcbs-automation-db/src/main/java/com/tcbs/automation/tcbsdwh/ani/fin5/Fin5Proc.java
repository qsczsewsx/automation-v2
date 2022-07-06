package com.tcbs.automation.tcbsdwh.ani.fin5;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Fin5Proc {

  public static final HibernateEdition redShiftDb = Database.TCBS_DWH.getConnection();

  @Step("Get data from sql")
  public static List<HashMap<String, Object>> getActDailyTransFromSql(String fromDate, String toDate) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select	accountant_time as accountanttime ");
    queryBuilder.append(" 	, underlying_code as underlyingcode ");
    queryBuilder.append("   , b.Price par ");
    queryBuilder.append("   , open_bl_amount as openblamount ");
    queryBuilder.append(" 	, sell_price as sellprice ");
    queryBuilder.append("   , buy_principal as buyprincipal ");
    queryBuilder.append(" 	, open_bl_quantity as openblquantity ");
    queryBuilder.append("   , sell_quantity as sellquantity ");
    queryBuilder.append(" 	, buy_quantity as buyquantity ");
    queryBuilder.append("   , total_profit as totalprofit ");
    queryBuilder.append(" 	, total_loss as totalloss ");
    queryBuilder.append("   , accrued_interest as accruedinterest ");
    queryBuilder.append(" 	, close_unit_price as closeUnitPrice ");
    queryBuilder.append("   , total_invest as totalInvest ");
    queryBuilder.append(" 	, delta_accrued_interest as deltaAccruedInterest ");
    queryBuilder.append("   , open_total_accrued as openTotalAccrued ");
    queryBuilder.append(" 	, close_total_accrued as closeTotalAccrued ");
    queryBuilder.append(" 	, net_coupon_this_month + delta_accrued_interest + minus_delta_accrued_interest AS accruedCoupon ");
    queryBuilder.append(" from staging.stg_tca_ACT_DAILY_TRANSACTION adt ");
    queryBuilder.append(" left join staging.Stg_tcb_Bond b on adt.UNDERLYING_CODE = b.Code ");
    queryBuilder.append(" where accountant_time >= :fromdate and accountant_time <= :todate ");
    queryBuilder.append(" order by accountant_time, underlying_code, open_bl_quantity, accrued_interest asc");

    try {
      List<HashMap<String, Object>> resultList = Fin5Proc.redShiftDb.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromdate", fromDate)
        .setParameter("todate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data from sql")
  public static List<HashMap<String, Object>> getAccruedCouponBondFromSql(String fromDate, String toDate, List<String> bondCode) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append("select accountant_time");
    queryBuilder.append("		,underlying_code");
    queryBuilder.append("		,book_interest_amount");
    queryBuilder.append("		,close_accrued_extra_perday");
    queryBuilder.append("		,close_accrued_discount_perday");
    queryBuilder.append("		, buy_amount");
    queryBuilder.append("		, buy_quantity");
    queryBuilder.append("		,product_par");
    queryBuilder.append("		,book_interest_amount - close_accrued_extra_perday + close_accrued_discount_perday + isnull(buy_amount,0) - isnull(buy_quantity,0) * product_par as accrued_coupon");
    queryBuilder.append(" from staging.stg_tca_act_daily_portfolio");
    queryBuilder.append(" where (accountant_time >= :fromdate and accountant_time <= :todate) ");
    if (!bondCode.get(0).equalsIgnoreCase("") && !bondCode.get(0).equalsIgnoreCase("null")) {
      queryBuilder.append(" and underlying_code in :bondcode ");
    }
    queryBuilder.append(" order by accountant_time, underlying_code ");
    try {
      List<HashMap<String, Object>> resultList = new ArrayList<>();
      if (!bondCode.get(0).equalsIgnoreCase("") && !bondCode.get(0).equalsIgnoreCase("null")) {
        resultList = Fin5Proc.redShiftDb.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter("fromdate", fromDate)
          .setParameter("todate", toDate)
          .setParameter("bondcode", bondCode)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      } else {
        resultList = Fin5Proc.redShiftDb.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter("fromdate", fromDate)
          .setParameter("todate", toDate)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      }
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}

