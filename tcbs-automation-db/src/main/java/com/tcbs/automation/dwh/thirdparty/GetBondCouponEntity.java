package com.tcbs.automation.dwh.thirdparty;

import com.tcbs.automation.dwh.Dwh;
import lombok.*;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

public class GetBondCouponEntity {
  @Id
  private String bondCode;
  private String nextCouponDate;
  private Double cfi;
  private Double totalReceivedCoupon;
  private Double totalFutureCoupon;
  private String type;

  @Step("select data")
  public static List<HashMap<String, Object>> getData(String custody, String bondCode, String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" with bond as ");
    queryStringBuilder.append("   (select BondCode, ExDate, cast(CFIDate as date) CFIDate, CustodyCode, TCBSID, sum(CFI) CFI ");
    queryStringBuilder.append("     from prc_bond_contract_cfi c ");
    queryStringBuilder.append("     where c.\"tag\" = 'Coupon Payment' ");
    queryStringBuilder.append("     and EtlCurDate = (select max(EtlCurDate) from prc_bond_contract_cfi) ");
    queryStringBuilder.append(" and custodycode = :custody ");
    queryStringBuilder.append(" group by BondCode, ExDate, CFIDate, CustodyCode, TCBSID), ");
    queryStringBuilder.append(" past as ");
    queryStringBuilder.append(" (select sum(CFI) as totalPast, CustodyCode from bond ");
    queryStringBuilder.append(" where CFIDate < cast(getdate() as Date) ");
    queryStringBuilder.append(" group by CustodyCode), ");
    queryStringBuilder.append(" future as ");
    queryStringBuilder.append(" (select sum(CFI) as totalFuture, CustodyCode from bond ");
    queryStringBuilder.append(" where CFIDate >= cast(getdate() as Date) ");
    queryStringBuilder.append(" GROUP by CustodyCode) ");
    queryStringBuilder.append(" select bond.*, past.totalPast, future.totalFuture ");
    queryStringBuilder.append(" from bond ");
    queryStringBuilder.append(" left join past on bond.CustodyCode = past.CustodyCode ");
    queryStringBuilder.append(" left join future on bond.CustodyCode = future.CustodyCode ");

    if (!bondCode.equalsIgnoreCase("")) {
      queryStringBuilder.append(" where BondCode = :bondCode");
    } else {
      queryStringBuilder.append(" where :bondCode is not null ");
    }
    if (!fromDate.equalsIgnoreCase("")) {
      queryStringBuilder.append(" and CFIDate >= :fromDate ");
    } else {
      queryStringBuilder.append(" and :fromDate is not null ");
    }
    if (!toDate.equalsIgnoreCase("")) {
      queryStringBuilder.append(" and CFIDate <= :toDate ");
    } else {
      queryStringBuilder.append(" and :toDate is not null ");
    }

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("custody", custody)
        .setParameter("bondCode", bondCode)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
