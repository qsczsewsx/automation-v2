package com.tcbs.automation.dwh;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RmPerfContractExpiredEntity {
  private static Date fromDate;
  private static Date toDate;

  static {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      fromDate = sdf.parse(sdf.format(new java.util.Date()));
      toDate = sdf.parse(sdf.format(DateUtils.addMonths(new Date(), 1)));
    } catch (ParseException e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    }
  }

  private String customerName;
  @Id
  private String custodyCode;
  private String tradingCode;
  private String bondCode;
  private Date purchaseDate;
  private Double purchasePrincipal;
  private Date expiredDate;
  private Double quantity;
  private Double coupon_received;
  private Double investmentRate;
  private Double totalValue;

  @Step("select contract expired data")
  public static List<Map<String, Object>> getContractExpired(String rmCustodyCD) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select * from (   ");
    queryStringBuilder.append(" select c.customerName, c.custodyCode, cfi.tradingCode, cfi.bondCode, cast(cfi.PDate as date) as purchaseDate,  ");
    queryStringBuilder.append(" cfi.PPrincipal as purchasePrincipal, cast(cfi.CFIDate as date) as expiredDate, cfi.Quantity as quantity ,  ");
    queryStringBuilder.append(" pe.CouponDaNhan as coupon_received, pe.LoiTucNeuTatToan as investmentRate, pe.TienTatToanSeNhan as totalValue  ");
    queryStringBuilder.append(" from (  ");
    queryStringBuilder.append("   select TradingCode, CustodyCode, BondCode, PDate, PPrincipal, OBal_Quantity as Quantity, CFIDate, CFI, Event, Tag  ");
    queryStringBuilder.append("   from Prc_Bond_Contract_CFI  ");
    queryStringBuilder.append("   where EtlCurDate = (select max(EtlCurDate) from Prc_Bond_Contract_CFI)  ");
    queryStringBuilder.append("   and RmId = (select top(1) UserCode  ");
    queryStringBuilder.append("              from stg_tcb_rm  ");
    queryStringBuilder.append("              where tcbsid = (select tcbsid from smy_dwh_cas_alluserview  ");
    queryStringBuilder.append("                             where EtlCurDate = (select max(EtlCurDate) from smy_dwh_cas_alluserview)  ");
    queryStringBuilder.append("                             and CustodyCD = :rmCustodyCD))  ");
    queryStringBuilder.append("   and Tag = 'Pro Expired'  ");
    queryStringBuilder.append("   and CFIDate >= :fromDate and CFIDate <= :toDate  ");
    queryStringBuilder.append("   ) cfi  ");
    queryStringBuilder.append(" left join Prc_bond_proexpired_next90D pe on cfi.TradingCode = pe.TradingCode  ");
    queryStringBuilder.append(" left join Stg_tcb_Customer c on cfi.CustodyCode = c.CustodyCode) a  ");
    queryStringBuilder.append(" order by a.custodyCode asc, a.tradingCode asc ");

    try {
      List<Map<String, Object>> resultList = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter("rmCustodyCD", rmCustodyCD)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      Dwh.dwhDbConnection.closeSession();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("select rmCode data")
  public static String getRmCode(String rmCustodyCD) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select top(1) UserCode from stg_tcb_rm   ");
    queryStringBuilder.append(" where tcbsid = (select tcbsid from smy_dwh_cas_alluserview  ");
    queryStringBuilder.append("                where EtlCurDate = (select max(EtlCurDate) from smy_dwh_cas_alluserview)  ");
    queryStringBuilder.append("                 and CustodyCD = :rmCustodyCD)  ");
    try {
      List<Map<String, Object>> map = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("rmCustodyCD", rmCustodyCD)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      Dwh.dwhDbConnection.closeSession();
      if (map.size() > 0) {
        return map.get(0).get("UserCode").toString();
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  @Step("select contract matured data")
  public static List<Map<String, Object>> getContractMatured(String rmCode) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select * from (   ");
    queryStringBuilder.append(" select bm.custodyCode, c.customerName, bm.tradingCode, cast(bm.PDate as date) as purchaseDate, bm.PPrincipal as purchasePrincipal,    ");
    queryStringBuilder.append(" bm.quantity, cast(bm.CFIDate as date) as maturityDate, bm.CFI as bondParValue, round(cp_onMaturity.CFI,0,1) as coupon_onMaturityDate,     ");
    queryStringBuilder.append(" round(couponPerUnit_all * bm.Quantity - cp_onMaturity.CFI,0,1) as coupon_received  ");
    queryStringBuilder.append(
      " , round(cp_onMaturity.CFI + bm.CFI,0,1) as totalValue, (bm.CFI + couponPerUnit_all * bm.Quantity - bm.PPrincipal) / bm.PPrincipal * 365 / datediff(dd, bm.PDate, bm.CFIDate) as investmentRate   ");
    queryStringBuilder.append(" from (select * from (select TradingCode, CustodyCode, BondCode, PDate, PPrincipal, OBal_Quantity as Quantity, CFIDate, CFI, Event, Tag   ");
    queryStringBuilder.append("   from Prc_Bond_Contract_CFI   ");
    queryStringBuilder.append("   where EtlCurDate = (select max(EtlCurDate) from Prc_Bond_Contract_CFI)   ");
    queryStringBuilder.append("   and RmId = :rmCode   ");
    queryStringBuilder.append("     and Event like 'BM%'   ");
    queryStringBuilder.append(" and CFIDate >= :fromDate and CFIDate <= :toDate   ");
    queryStringBuilder.append("                 ) cfi where Tag = 'Bond Matured') bm   ");
    queryStringBuilder.append(" left join (select *   ");
    queryStringBuilder.append("   from (select TradingCode, CustodyCode, BondCode, PDate, PPrincipal, OBal_Quantity as Quantity, CFIDate, CFI, Event, Tag   ");
    queryStringBuilder.append("     from Prc_Bond_Contract_CFI   ");
    queryStringBuilder.append("     where EtlCurDate = (select max(EtlCurDate) from Prc_Bond_Contract_CFI)   ");
    queryStringBuilder.append(" and RmId = :rmCode   ");
    queryStringBuilder.append("   and Event like 'BM%'   ");
    queryStringBuilder.append(" and CFIDate >= :fromDate and CFIDate <= :toDate   ");
    queryStringBuilder.append("                 ) cfi where Tag = 'Coupon Payment') cp_onMaturity on bm.TradingCode = cp_onMaturity.TradingCode   ");
    queryStringBuilder.append(" left join (select tradingCode, sum(CFIperUnit) as couponPerUnit_all ");
    queryStringBuilder.append("   from Prc_Bond_Contract_CFI ");
    queryStringBuilder.append("   where EtlCurDate = (select max(EtlCurDate) from Prc_Bond_Contract_CFI) ");
    queryStringBuilder.append(" and Tag = 'Coupon Payment' ");
    queryStringBuilder.append(" and TradingCode in (select TradingCode from ");
    queryStringBuilder.append("    (select TradingCode, CustodyCode, BondCode, PDate, PPrincipal, OBal_Quantity as Quantity, CFIDate, CFI, Event, Tag   ");
    queryStringBuilder.append("     from Prc_Bond_Contract_CFI   ");
    queryStringBuilder.append("     where EtlCurDate = (select max(EtlCurDate) from Prc_Bond_Contract_CFI)   ");
    queryStringBuilder.append(" and RmId = :rmCode   ");
    queryStringBuilder.append("   and Event like 'BM%'   ");
    queryStringBuilder.append(" and CFIDate >= :fromDate and CFIDate <= :toDate ) cfi)    ");
    queryStringBuilder.append(" group by TradingCode ) cp on bm.TradingCode = cp.TradingCode   ");
    queryStringBuilder.append(" left join Stg_tcb_Customer c on bm.CustodyCode = c.CustodyCode ) a ");
    queryStringBuilder.append(" order by a.custodyCode asc, a.tradingCode asc   ");

    try {
      List<Map<String, Object>> resultList = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter("rmCode", rmCode)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      Dwh.dwhDbConnection.closeSession();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get coupon payment contract")
  public static List<Map<String, Object>> getCouponPayment(String rmCustodyCD) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select * from (  ");
    queryStringBuilder.append(" select cfi.* , c.customerName from ( ");
    queryStringBuilder.append(" select tradingCode, custodyCode, bondCode, cast(PDate as date) as purchaseDate, OBal_Quantity as quantity, ");
    queryStringBuilder.append(" cast(cfiDate as date) as couponDate, round(CFI,0,1) as coupon");
    queryStringBuilder.append(" from Prc_Bond_Contract_CFI");
    queryStringBuilder.append(" where EtlCurDate = (select max(EtlCurDate) from Prc_Bond_Contract_CFI)");
    queryStringBuilder.append(" and RmId = (select top(1) UserCode from stg_tcb_rm ");
    queryStringBuilder.append(" where tcbsid = (select tcbsid from smy_dwh_cas_alluserview where  ");
    queryStringBuilder.append(" EtlCurDate = (select max(EtlCurDate) from smy_dwh_cas_alluserview) and CustodyCD = :rmCustodyCD))");
    queryStringBuilder.append(" and Tag = 'Coupon Payment' and Event not like 'BM%' ");
    queryStringBuilder.append(" and CFIDate >= :fromDate and CFIDate <= :toDate) cfi");
    queryStringBuilder.append(" left join Stg_tcb_Customer c on cfi.CustodyCode = c.CustodyCode ) a order by a.custodyCode asc, a.tradingcode asc");

    try {
      List<Map<String, Object>> resultList = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter("rmCustodyCD", rmCustodyCD)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      Dwh.dwhDbConnection.closeSession();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  @Step("get cash flow in future ")
  public static List<Map<String, Object>> getCashFlowInFuture(String rmCode) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select * from ( ");
    queryStringBuilder.append(" select cfi.*, c.customerName from  ");
    queryStringBuilder.append(" (select custodyCode, bondCode, cast(cfiDate as date) as paymentDate, round(sum(cfi),0,1) as cashFlow, 'matured' as cashType ");
    queryStringBuilder.append(" from Prc_Bond_Contract_CFI ");
    queryStringBuilder.append(" where EtlCurDate = (select max(EtlCurDate) from Prc_Bond_Contract_CFI) ");
    queryStringBuilder.append(" and RMId = :rmCode ");
    queryStringBuilder.append(" and CFIDate >= :fromDate and CFIDate <= :toDate ");
    queryStringBuilder.append(" and Event like 'BM%' ");
    queryStringBuilder.append(" group by custodyCode, bondCode, cfiDate ");
    queryStringBuilder.append(" union all ");
    queryStringBuilder.append(" select custodyCode, bondCode, cast(cfiDate as date) as paymentDate, round(sum(cfi),0,1) as cashFlow ");
    queryStringBuilder.append(" ,case when Tag = 'Coupon Payment' then 'coupon' else 'proexpired' end as cashType ");
    queryStringBuilder.append(" from Prc_Bond_Contract_CFI ");
    queryStringBuilder.append(" where EtlCurDate = (select max(EtlCurDate) from Prc_Bond_Contract_CFI) ");
    queryStringBuilder.append(" and RMId = :rmCode ");
    queryStringBuilder.append(" and CFIDate >= :fromDate and CFIDate <= :toDate ");
    queryStringBuilder.append(" and Event not like 'BM%' ");
    queryStringBuilder.append(" and Tag in ('Coupon Payment', 'Pro Expired') ");
    queryStringBuilder.append(" group by CustodyCode, bondCode, cfiDate, Tag) cfi ");
    queryStringBuilder.append(" left join stg_tcb_customer c on cfi.CustodyCode = c.CustodyCode ) a");
    queryStringBuilder.append(" order by a.custodyCode asc, a.bondCode asc ");

    try {
      List<Map<String, Object>> resultList = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter("rmCode", rmCode)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      Dwh.dwhDbConnection.closeSession();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
