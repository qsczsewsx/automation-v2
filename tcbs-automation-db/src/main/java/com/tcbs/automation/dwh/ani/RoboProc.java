package com.tcbs.automation.dwh.ani;

import com.tcbs.automation.dwh.Dwh;
import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoboProc {

  @Step(" Get data ")
  public static List<HashMap<String, Object>> getDataFromProc(String proc, String param) {
    StringBuilder queryBuilder = new StringBuilder()
      .append(" EXEC  ")
      .append(proc).append(" ");
    if (param != null) {
      queryBuilder.append(param);
    }
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    try {
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      List<HashMap<String, Object>> res = session.createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return res;
    } catch (Exception ex) {
      throw ex;
    } finally {
      Tcbsdwh.tcbsDwhDbConnection.closeSession();
    }
  }

  @Step("Get data from sql")
  public static List<HashMap<String, Object>> getSuggestRateFromSql(String tradingCodeList,
                                                                    Integer voucher, String priceType) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" DECLARE @TBL_TradingCode TABLE( ");
    queryBuilder.append("   [TradingCode] nvarchar(300), ");
    queryBuilder.append(" [Quantity] float ");
    queryBuilder.append(" INDEX IDX NONCLUSTERED([TradingCode], [Quantity]) ");
    queryBuilder.append(" ) ");

    queryBuilder.append(" Insert into @TBL_TradingCode(TradingCode, Quantity) ");
    queryBuilder.append("   SELECT * FROM ");
    queryBuilder.append(" OPENJSON ( ' ");
    queryBuilder.append(tradingCodeList);
    queryBuilder.append(" ' ) ");
    queryBuilder.append(" WITH ( ");
    queryBuilder.append("   TradingCode   varchar(300)    '$.TradingCode' , ");
    queryBuilder.append("   Quantity     float         '$.Quantity' ");
    queryBuilder.append(" ); ");

    queryBuilder.append(" select ");
    queryBuilder.append(" t.TradingDate, bo.code as Bond_Code ");
    queryBuilder.append(" , datediff(day, t.TradingDate, cast(getdate() as Date)) as holdingPeriod ");
    queryBuilder.append(" , t.TradingCode as Buy_TradingCode ");
    queryBuilder.append("   , l.Quantity Original_BuyQuantity ");
    queryBuilder.append("   , (l.Quantity/t.Quantity) * t.Principal as BuyPrincipal ");
    queryBuilder.append(" , p.Rate2 as TCBS_Rate ");
    queryBuilder.append(" ,  case when bo.BaseInterestBottom = 1 then 360 ");
    queryBuilder.append(" when bo.BaseInterestBottom = 2 then DATEPART(dy,datefromparts(year(t.TradingDate),12,31)) ");
    queryBuilder.append(" when bo.BaseInterestBottom = 3 then 365 ");
    queryBuilder.append(" 		else 365 end as BaseInterestDays ");
    queryBuilder.append("   , bo.ExpiredDate ");
    queryBuilder.append("   , datediff(day, t.TradingDate,bo.ExpiredDate)/30 as monthToM_fromBuyDate ");
    queryBuilder.append(" , datediff(day, cast(getdate() as Date),bo.ExpiredDate)/30 as monthToM_fromSellDate,  ");
    queryBuilder.append(voucher);
    queryBuilder.append("   as discount ");
    queryBuilder.append(" , (select sum(Quantity) from @TBL_TradingCode) as REMAIN_VOLUME ");
    queryBuilder.append(" into #t ");

    queryBuilder.append(" from rt_tcb_Trading t ");
    queryBuilder.append(" left join RT_tcb_BondProduct bp on t.BondProductId = bp.id ");
    queryBuilder.append(" left join  RT_tcb_bond bo on bp.BondID = bo.id ");
    queryBuilder.append(" left join stg_tcb_customer c on t.CustomerId = c.CustomerId ");
    queryBuilder.append(" left join CheckPricing2 p on bp.code = p.ProductCode and cast(t.TradingDate as Date) = p.Date ");
    queryBuilder.append(" left join @TBL_TradingCode l on t.TradingCode = l.TradingCode ");

    queryBuilder.append(" where t.action = 5 ");
    queryBuilder.append(" and t.AgencyStatus = 1 ");
    queryBuilder.append(" and t.WaitingStatus = 3 ");
    queryBuilder.append(" and l.TradingCode is not null ");
    queryBuilder.append(" ; ");

    queryBuilder.append(" select  sum(BuyPrincipal) as Total_buyPrincipal, sum( dbo.f_getcoupon_perunit(Bond_Code, BaseInterestDays, TradingDate)) as coupon_reinvest_payment ");
    queryBuilder.append(" into #cp_payment ");
    queryBuilder.append("   from ");
    queryBuilder.append(" #t ");
    queryBuilder.append(" ; ");

    queryBuilder.append(" select t.BOND_CODE, t.REMAIN_VOLUME ");
    queryBuilder.append("   , t.discount ");
    queryBuilder.append("   , sum(holdingPeriod * Original_BuyQuantity)/sum(Original_BuyQuantity) as Avg_holding_Period ");
    queryBuilder.append(" , floor((sum(holdingPeriod * Original_BuyQuantity)/(sum(Original_BuyQuantity)))/30) as Avg_Holding_Period_byMonth ");
    queryBuilder.append(" into #avg_holding ");
    queryBuilder.append(" from #t t ");
    queryBuilder.append(" group by t.BOND_CODE, t.REMAIN_VOLUME, t.discount ");
    queryBuilder.append("   ; ");

    queryBuilder.append(" select h.*, case ");
    queryBuilder.append("   when i.Month is NULL then (select max(Total) from Off_tcb_interestRate) ");
    queryBuilder.append(" 			else i.Total end as interestRate ");
    queryBuilder.append(" into #base_interest ");
    queryBuilder.append(" from #avg_holding h ");
    queryBuilder.append(" left join Off_tcb_interestRate i on h.Avg_Holding_Period_byMonth = i.month and i.price_type = '");
    queryBuilder.append(priceType);
    queryBuilder.append("' ");

    queryBuilder.append(" select y.BOND_CODE, REMAIN_VOLUME, Avg_holding_Period, Total_buyPrincipal, coupon_reinvest_payment ");
    queryBuilder.append("   , (Total_buyPrincipal * ( 1 + interestRate * Avg_holding_Period/365) - isnull(coupon_reinvest_payment, 0) - y.DISCOUNT ) as Total_sell_principal ");
    queryBuilder.append(" 	, y.interestRate ");
    queryBuilder.append(
      " 	, case when REMAIN_VOLUME > 0 then (Total_buyPrincipal * ( 1 + interestRate * Avg_holding_Period/365) - isnull(coupon_reinvest_payment, 0) - y.DISCOUNT)/(REMAIN_VOLUME *0.9975)  ");
    queryBuilder.append(" 		else NULL end as Sell_UnitPrice ");
    queryBuilder.append(
      " 	, case when REMAIN_VOLUME > 0 then ((Total_buyPrincipal * ( 1 + interestRate * Avg_holding_Period/365) - isnull(coupon_reinvest_payment, 0) - y.DISCOUNT)/(REMAIN_VOLUME *0.9975) * 1.0015) * REMAIN_VOLUME ");
    queryBuilder.append(" 		else NULL end as Buy_Principal ");
    queryBuilder.append("   , Discount ");
    queryBuilder.append(" into #ytm2_tmp ");
    queryBuilder.append(" from #cp_payment c ");
    queryBuilder.append(" left join #base_interest y on 1=1 ");

    queryBuilder.append(" select *, [dbo].[getBondYTMfromUnitPrice](Sell_UnitPrice, Buy_Principal, REMAIN_VOLUME, cast(getdate() as Date), Bond_Code, 'CN') as ytm2 ");
    queryBuilder.append(" into #ytm2 ");
    queryBuilder.append(" from #ytm2_tmp ");
    queryBuilder.append(" ");
    queryBuilder.append(" select BOND_CODE, REMAIN_VOLUME, Buy_TradingCode, Original_BuyQuantity, TCBS_RATE,TCBS_RATE - sum(ytm_ptime) as ytm3 ");
    queryBuilder.append(" into #ytm3_tmp ");
    queryBuilder.append("   from ");
    queryBuilder.append(" (select t.BOND_CODE, t.REMAIN_VOLUME, t.Buy_TradingCode, t.Original_BuyQuantity, t.TCBS_RATE ");
    queryBuilder.append(" 	,case when (ROW_NUMBER() over( partition by  buy_TradingCode order by FromMonth desc)) = 1 then cast((t.monthToM_fromBuyDate - FromMonth) as float)/ 12 * timepremium ");
    queryBuilder.append(" when (ROW_NUMBER() over( partition by  buy_TradingCode order by FromMonth ) = 1) then cast( (ToMonth - t.monthToM_fromSellDate ) as float)/12   * timepremium ");
    queryBuilder.append(" 		else cast((ToMonth - FromMonth) as float)/12  * timepremium ");
    queryBuilder.append(" end as ytm_ptime ");
    queryBuilder.append(" from #t t ");
    queryBuilder.append(
      " left join off_tcb_ptime p on (p.FromMonth >= t.monthToM_fromSellDate and p.FromMonth <= t.monthToM_fromBuyDate ) or (p.ToMonth >= t.monthToM_fromSellDate and p.ToMonth <= t.monthToM_fromBuyDate ) or (p.FromMonth <= t.monthToM_fromSellDate and t.monthToM_fromBuyDate <= p.ToMonth) ");
    queryBuilder.append(" ) r ");
    queryBuilder.append(" group by BOND_CODE, REMAIN_VOLUME, Buy_TradingCode, Original_BuyQuantity, TCBS_RATE ");
    queryBuilder.append(" ");
    queryBuilder.append(" select BOND_CODE, REMAIN_VOLUME,sum(Original_BuyQuantity * ytm3) / sum(Original_BuyQuantity) as ytm3 ");
    queryBuilder.append(" into #ytm3 ");
    queryBuilder.append(" from #ytm3_tmp ");
    queryBuilder.append(" group by BOND_CODE, REMAIN_VOLUME ");
    queryBuilder.append(" ");
    queryBuilder.append(" select distinct case when isnull(ytm2, 0) >= isnull(ytm3, 0) then ytm2*100 ");
    queryBuilder.append(" when isnull(ytm3, 0)> isnull(ytm2, 0) then ytm3*100 else NULL end as rate ");
    queryBuilder.append(" from #ytm2 y2 ");
    queryBuilder.append(" left join #ytm3 y3 on y2.bond_code = y3.bond_code ");

    try {
      List<HashMap<String, Object>> resultList = Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  @Step("Get data from sql")
  public static List<HashMap<String, Object>> getBuyBackIcnFromSql() {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select  i.Order_ID, i.exec_Quantity, i.ytm1 as ytm, Total_Point ");
    queryBuilder.append(" from [Smy_dwh_tcb_getbuyBackICN] i ");
    queryBuilder.append(" where ETLRunDateTime = (select max(ETLRunDateTime) from [Smy_dwh_tcb_getbuyBackICN]) ");
    queryBuilder.append(" and cast(ETLRunDateTime as Date) = cast (getdate() as Date) ");
    queryBuilder.append(" and (( DATEPART(hh, getdate()) < 12 and  DATEPART(hh, ETLRunDateTime) < 12) or ( DATEPART(hh, getdate()) >= 12 and  DATEPART(hh, ETLRunDateTime) >= 12)) ");
    queryBuilder.append(" order by Total_Point desc ");


    try {
      List<HashMap<String, Object>> resultList = Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("update ETL data")
  public static void updateBuyBackIcnEtlDate(String etlTime) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" UPDATE [tcbs-dwh].dbo.Smy_dwh_tcb_getbuyBackICN ");
    queryBuilder.append(" SET  ETLRunDateTime= ? ");
    queryBuilder.append(" where ETLRunDateTime = (select max(ETLRunDateTime) from [Smy_dwh_tcb_getbuyBackICN]); ");
    Query<?> query = session.createNativeQuery(queryBuilder.toString());
    query.setParameter(1, etlTime);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}

